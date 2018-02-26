package com.hexagon.game.network.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.screens.myscreens.ScreenJoin;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.packets.PacketJoin;
import com.hexagon.game.network.packets.PacketKeepAlive;
import com.hexagon.game.network.packets.PacketLeave;
import com.hexagon.game.network.packets.PacketServerList;
import com.hexagon.game.network.packets.PacketType;

import java.util.Hashtable;

import de.svdragster.logica.util.Delegate;

/**
 * Created by Sven on 26.02.2018.
 */

public class ClientListener extends PacketListener {


    public ClientListener(HexaServer server) {
        super(server);
    }

    @Override
    public void registerAll() {
        dispatchTable = new Hashtable<PacketType, Delegate>() {{
            put(PacketType.KEEPALIVE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    System.out.println("Received Keep Alive");
                    PacketKeepAlive packet = (PacketKeepAlive) args[0];
                    //server.send(new PacketKeepAlive());
                }
            });

            put(PacketType.JOIN, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    System.out.println("Received JOIN");
                    PacketJoin packet = (PacketJoin) args[0];

                    // I'm not the host, so either a new player has joined the game or I have joined the game
                    System.out.println(HexaServer.senderId.toString() + " ////// " + packet.getHostId().toString());
                    if (packet.getHostId().equals(HexaServer.senderId)) {
                        System.out.println("==== YOU HAVE JOINED THE GAME!! (Username: " + packet.getUsername() + ")");
                        ScreenManager.getInstance().setCurrentScreen(ScreenType.LOBBY);
                    } else {
                        System.out.println(packet.getUsername() + " has joined the game");
                    }

                }
            });

            put(PacketType.LEAVE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    PacketLeave packetLeave = (PacketLeave) args[0];
                    // TODO: Get username somehow
                    System.out.println("USER has left the game");

                }
            });

            put(PacketType.BUILD, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    System.out.println("Received BUILD");

                }
            });

            put(PacketType.DESTROY, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    System.out.println("Received DESTROY");

                }
            });

            put(PacketType.TRADE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    System.out.println("Received TRADE");

                }
            });

            put(PacketType.TERMINATE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    System.out.println("Received TERMINATE");

                }
            });

            put(PacketType.MAPUPDATE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    System.out.println("Received MAPUPDATE");

                }
            });

            put(PacketType.SERVER_LIST, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    PacketServerList packetServerList = (PacketServerList) args[0];
                    System.out.println("Received SERVER_LIST " + packetServerList.entries.size());
                    if (ScreenManager.getInstance().getCurrentScreen().getScreenType() == ScreenType.JOIN) {
                        final ScreenJoin screenJoin = (ScreenJoin) ScreenManager.getInstance().getCurrentScreen();

                        screenJoin.subwindowServers.removeButtons(screenJoin.getStage());

                        for (final PacketServerList.Entry entry : packetServerList.entries) {
                            screenJoin.subwindowServers.add(new UiButton(entry.room,
                                    30, 0, 100, 40,
                                    screenJoin.getStage(),
                                    new ChangeListener() {
                                        @Override
                                        public void changed(ChangeEvent event, Actor actor) {
                                            System.out.println("Clicked room " + entry.room);
                                            screenJoin.joinRoom(entry.host, entry.room);
                                        }
                                    }), screenJoin.getStage());
                        }

                        screenJoin.subwindowServers.orderAllNeatly(1);
                        screenJoin.subwindowServers.updateElements();
                    }
                }
            });
        }}
        ;
    }
}
