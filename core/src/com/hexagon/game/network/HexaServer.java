package com.hexagon.game.network;

import com.hexagon.game.network.packets.Packet;
import com.hexagon.game.network.packets.PacketListener;
import com.hexagon.game.network.packets.PacketType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import de.svdragster.logica.util.Delegate;

/**
 * Created by Johannes on 19.02.2018.
 */

public class HexaServer {

    private static final int TIMEOUT = 30_000;

    /**
     * connection stuff by Sven ;)
     */
    private Socket              socket;
    private InetSocketAddress   address;

    private List<Packet>        toSend = new ArrayList<>();
    private final Object        sendingLock = new Object();


    private List<Packet>        toCall = new ArrayList<>();
    private final Object        receivingLock = new Object();

    private boolean             running = false;

    private PacketListener      packetListener;
    private UUID                LocalClientID = UUID.randomUUID();

    /**
     * Acts on Events
     */
    private PacketListener listener;


    public HexaServer(String address, int port, Map<PacketType,Delegate> DispatchTable) {
        this(DispatchTable);
        this.address =   new InetSocketAddress(address, port);
    }

    public HexaServer(Map<PacketType,Delegate> DispatchTable) {
        socket = new Socket();
        packetListener = new PacketListener(DispatchTable);
    }

    public void connect(int timeout) throws IOException {
        socket.connect(address, timeout);
        running = true;
        startSendingThread();
        //startReceivingThread();
        //serverSocket = new ServerSocket(this.address.getPort());
        //serverSocket.setSoTimeout(TIMEOUT);

    }

    public void receive() {
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
    }

    public void send(Packet packet) {
        synchronized (sendingLock) {
            toSend.add(packet);
        }
    }

    /**
     * Starts asynchronous thread. continuously sends everything contained in the "toSend" buffer.
     *
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
                        continue;
                    }
                    try {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        //ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                        for (int i = 0; i < sendBuffer.size(); i++) {
                            System.out.println("Sending " + sendBuffer.get(i).getClass().getName());

                            out.println();
                            out.flush();
                            System.out.println("done");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {

                        sendBuffer.clear();
                    }

                }
            }
        }).start();
    }

    /**
     * Starts asynchronous thread. continuously receives everything and saves it in the "toCall" buffer.
     *
     */
    public void startReceivingThread() {
        final List<Packet>        receiveBuffer = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    receiveBuffer.clear();

                    try {
                        //Scanner in = new Scanner(socket.getInputStream());
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        Packet packet = (Packet) in.readObject();

                        receiveBuffer.add(packet);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    synchronized (receivingLock) {
                        toCall.addAll(receiveBuffer);
                    }

                    receiveBuffer.clear();
                }
            }
        }).start();
    }

    public void callEvents() {
        synchronized (receivingLock) {
            for (int i=0; i<toCall.size(); i++) {
                try {
                    listener.call(toCall.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public UUID getLocalClientID() {
        return LocalClientID;
    }
}
