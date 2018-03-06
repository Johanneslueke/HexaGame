package com.hexagon.game.network;

import com.badlogic.gdx.Gdx;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.ui.windows.WindowNotification;
import com.hexagon.game.network.listener.ClientListener;
import com.hexagon.game.network.listener.ServerListener;
import com.hexagon.game.network.packets.Packet;
import com.hexagon.game.network.packets.PacketKeepAlive;
import com.hexagon.game.network.packets.PacketPlayerStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class HexaServer {

    private static final int    TIMEOUT = 20_000;
    public static final String  VERSION = "0.1";

    public static UUID          senderId = UUID.randomUUID();;//UUID.fromString("525183d9-1a5a-40e1-a712-e3099282c341");

    /**
     * connection stuff
     */
    private Socket              socket;
    private InetSocketAddress   address;

    private List<Packet>        toSend = new ArrayList<>();
    private final Object        sendingLock = new Object();


    private List<Packet>        toCall = new ArrayList<>();
    private final Object        receivingLock = new Object();

    private boolean             running = false;

    private ServerListener      hostListener;
    private ClientListener      clientListener;
    public UUID                LocalClientID = senderId;

    public long                 lastKeepAliveSent = System.currentTimeMillis();

    private SessionData         sessionData;

    public static String WhatAmI(HexaServer server) {
        return (server.isHost() ? " (Host)" : " (Client) ");
    }

    public HexaServer(String address, int port) {
        if (address == null) return; // If the address is null, the player wants to play offline
        this.address    = new InetSocketAddress(address, port);
        this.socket     = new Socket();
    }

    public HexaServer(String address, int port, boolean isHost) {
        this(address, port);
        if (isHost) {
            sessionData = new SessionData();
        }
    }

    public void hostOffline() {
        System.out.println("=== Hosting offline game ===");
        hostListener = new ServerListener(this);
        hostListener.registerAll();
        clientListener = new ClientListener(this);
        clientListener.registerAll();
        sessionData = new SessionData();
        socket = new Socket(); // Just an empty socket instance to prevent nullpointers
    }

    public void connect(int timeout) throws IOException {
        socket.connect(address, timeout);
        running = true;
        startSendingThread();
        startReceivingThread();
        System.out.println("=======   Connected to Router   =======");
        //serverSocket = new ServerSocket(this.address.getPort());
        //serverSocket.setSoTimeout(TIMEOUT);

        if (isHost()) {
            hostListener = new ServerListener(this);
            hostListener.registerAll();
        }
        clientListener = new ClientListener(this);
        clientListener.registerAll();

    }

    public Packet receive() {
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try {
                        // Blocking method - Timeouts after TIMEOUT seconds
                        receivingSocket = serverSocket.accept();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (receivingSocket != null )
                            try { receivingSocket.close(); } catch ( IOException ignored) { }
                    }
                }
            }
        });
         */
        synchronized (receivingLock){
            if(!toCall.isEmpty())
                return toCall.remove(toCall.size());
        }

        return null;
    }

    public void send(Packet packet) {
        if (isHost() && packet.getSenderId().equals(HexaServer.senderId)) {
            try {
                clientListener.call(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (socket == null || !socket.isConnected()) {
            return;
        }
        synchronized (sendingLock) {
            toSend.add(packet);
        }
    }

    /**
     * Starts asynchronous thread. continuously sends everything contained in the "toSend" buffer.
     */
    public void startSendingThread() {
        final List<Packet>        sendBuffer = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    synchronized (sendingLock) {
                        sendBuffer.addAll(toSend);
                        toSend.clear();
                    }
                    if (sendBuffer.isEmpty()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    try {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        //ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                        for (int i = 0; i < sendBuffer.size(); i++) {

                            Packet packet = sendBuffer.get(i);

                            System.out.println("(" + isHost() + ") Sending " + packet.getClass().getName() + " --> '" + packet.serialize() + "'");
                            out.println(packet.serialize());
                            out.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {

                        sendBuffer.clear();
                    }

                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Starts asynchronous thread. continuously receives everything and saves it in the "toCall" buffer.
     */
    public void startReceivingThread() {
        final List<Packet>        receiveBuffer = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String disconnectReason = "Disconnected";
                while (running) {
                    receiveBuffer.clear();

                    try {
                        Scanner in = new Scanner(socket.getInputStream());
                        //ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                        int readAmount = 0;

                        socket.setSoTimeout(TIMEOUT);

                        while (in.hasNext()) {
                            socket.setSoTimeout(TIMEOUT);
                            readAmount++;
                            String stringPacket = in.nextLine();
                            if (stringPacket.equals("")) {
                                System.out.println("I received an empty packet.");
                            } else {
                                System.out.println("I received a packet! " + stringPacket);
                                Packet packet = Packet.deserialize(stringPacket);
                                if (packet == null) {
                                    System.out.println("Packet is null :(");
                                } else {
                                    System.out.println(packet.getType().name());
                                    synchronized (receivingLock) {
                                        toCall.add(packet);
                                    }
                                }
                            }
                        }

                        if (readAmount == 0) {
                            running = false;
                            //System.out.println("Connection lost: Server closed connection");
                            disconnectReason = "Disconnected: Server closed connection";
                        }

                    } catch (SocketTimeoutException e) {
                        running = false;
                        //System.out.println("Connection to the Server lost: Timeout (" + TIMEOUT + ")");
                        disconnectReason = "Disconnected: Timeout (" + TIMEOUT + ")";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    receiveBuffer.clear();
                }

                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final String finalDisconnectReason = disconnectReason;
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(finalDisconnectReason);
                        ScreenManager.getInstance().setCurrentScreen(ScreenType.MAIN_MENU);
                        new WindowNotification(finalDisconnectReason,
                                ScreenManager.getInstance().getCurrentScreen().getStage(),
                                ScreenManager.getInstance().getCurrentScreen().getWindowManager());
                    }
                });
            }
        }).start();
    }

    public void callEvents() {
        if (socket.isConnected()) {
            if (System.currentTimeMillis() - lastKeepAliveSent >= 8_000) {
                broadcastKeepAlive();

                if(getSessionData() != null && isHost())
                    for(UUID id : getSessionData().PlayerList.keySet()){
                    broadcastPlayerStatus(id);
                    }
            }
        }

        synchronized (receivingLock) {
            for (int i=0; i<toCall.size(); i++) {
                try {
                    //packetListener.call(toCall.get(i));
                    if (isHost()) {
                        System.out.println("calling host " + toCall.get(i).getType().name());
                        hostListener.call(toCall.get(i));
                    } else {
                        System.out.println("calling client " + toCall.get(i).getType().name());
                        clientListener.call(toCall.get(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            toCall.clear();
        }
    }

    public boolean disconnect() {
        if (socket.isClosed()) {
            System.out.println("Could not disconnect because the socket is already closed");
            return false;
        }
        running = false;
        try {
            socket.close();
            System.out.println("You have disconnected from the game");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public UUID getLocalClientID() {
        return LocalClientID;
    }

    public void broadcastKeepAlive() {
        lastKeepAliveSent = System.currentTimeMillis();
        send(new PacketKeepAlive(senderId, 1));
    }

    public void broadcastPlayerStatus(UUID playerID) {
      try{
          send(new PacketPlayerStatus(senderId,playerID,this.getSessionData().getPlayerResourceStatus(playerID)));
      }catch (RuntimeException e){
          System.out.println(e.getMessage());
          send(new PacketPlayerStatus(senderId,playerID,new Hashtable<String, Integer>(){{
              put("ORE",-1);
              put("WOOD",-1);
              put("STONE",-1);
          }}));
      }
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    public boolean isHost() {
        return sessionData != null;
    }

    public ServerListener getHostListener() {
        return hostListener;
    }

    public ClientListener getClientListener() {
        return clientListener;
    }

    public boolean isRunning() {
        return running;
    }
}
