package com.hexagon.game.network.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.screens.myscreens.ScreenJoin;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.packets.PacketBuild;
import com.hexagon.game.network.packets.PacketDestroy;
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

public class ServerListener extends PacketListener {

    public long keepAliveSent = 0;

    public ServerListener(HexaServer server) {
        super(server);
    }

    @Override
    public void registerAll() {
        dispatchTable = new Hashtable<PacketType, Delegate>() {{
            put(PacketType.KEEPALIVE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    long diff = System.currentTimeMillis() - server.lastKeepAliveSent;
                    System.out.println("Received Keep Alive (" + diff + " ms)");
                    PacketKeepAlive packet = (PacketKeepAlive) args[0];
                    //server.send(new PacketKeepAlive());
                }
            });

            put(PacketType.JOIN, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    System.out.println("Received JOIN");
                    PacketJoin packet = (PacketJoin) args[0];

                    server.getSessionData().addNewPlayer(packet.getSenderId(), packet.getUsername());
                    System.out.println(packet.getUsername() + " has joined the game (I AM THE SERVER)");

                    // I'm the host, so I have to broadcast to my players that a new player has joined the game
                    server.send(new PacketJoin(packet.getUsername(), packet.getSenderId(), packet.getVersion()));

                }
            });

            put(PacketType.LEAVE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    PacketLeave leave = (PacketLeave) args[0];

                    // Confirm the Leave Packet by sending it to the router
                    // (The router sends it to all clients)
                    server.send(new PacketLeave(leave.getLeaverUuid(), leave.isKick()));

                }
            });

            put(PacketType.BUILD, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    System.out.println("Received BUILD");

                    PacketBuild packetBuild = (PacketBuild) args[0];

                    // TODO: Check if the player who wants to build has enough resources to build
                    // TODO: Check if the player can build at that location
                    // -> Validate Data to prevent crashes (e.g. when someone sends a corrupted packet)

                    // Let the ClientListener handle the clientsided logic for building
                    //server.getClientListener().call(packetBuild);

                    // Respond
                    server.send(new PacketBuild(
                            packetBuild.getArrayPosition(),
                            packetBuild.getStructureType(),
                            packetBuild.getOwner()
                            )
                    );

                }
            });

            put(PacketType.DESTROY, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    System.out.println("Received DESTROY");

                    PacketDestroy packetDestroy = (PacketDestroy) args[0];

                    // TODO: Check if the player who wants to build has enough resources to build
                    // TODO: Check if the player can build at that location
                    // -> Validate Data to prevent crashes (e.g. when someone sends a corrupted packet)

                    // Let the ClientListener handle the clientsided logic for building
                    //server.getClientListener().call(packetDestroy);

                    // Respond
                    server.send(new PacketDestroy(packetDestroy.getArrayPosition()));

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
        }};
    }
}
