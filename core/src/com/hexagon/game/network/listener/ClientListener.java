package com.hexagon.game.network.listener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.Logic.Components.HexaComponentOre;
import com.hexagon.game.Logic.Components.HexaComponentOwner;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.screens.myscreens.ScreenJoin;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.JsonHexMap;
import com.hexagon.game.map.MapManager;
import com.hexagon.game.map.Point;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.SessionData;
import com.hexagon.game.network.packets.PacketBuild;
import com.hexagon.game.network.packets.PacketDestroy;
import com.hexagon.game.network.packets.PacketHostGenerating;
import com.hexagon.game.network.packets.PacketJoin;
import com.hexagon.game.network.packets.PacketKeepAlive;
import com.hexagon.game.network.packets.PacketLeave;
import com.hexagon.game.network.packets.PacketMapUpdate;
import com.hexagon.game.network.packets.PacketPlayerLoaded;
import com.hexagon.game.network.packets.PacketPlayerStatus;
import com.hexagon.game.network.packets.PacketRegister;
import com.hexagon.game.network.packets.PacketServerList;
import com.hexagon.game.network.packets.PacketType;
import com.hexagon.game.util.ConsoleColours;

import java.util.Hashtable;
import java.util.UUID;

import de.svdragster.logica.components.Component;
import de.svdragster.logica.components.ComponentProducer;
import de.svdragster.logica.components.ComponentResource;
import de.svdragster.logica.util.Delegate;
import de.svdragster.logica.util.SystemNotifications.NotificationNewEntity;
import de.svdragster.logica.world.Engine;

import static java.util.Arrays.asList;

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
            final Object lock = new Object();
            put(PacketType.KEEPALIVE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    PacketKeepAlive packet = (PacketKeepAlive) args[0];
                    server.send(new PacketKeepAlive(packet.getSessionID()));
                }
            });

            put(PacketType.REGISTER, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    PacketRegister packet = (PacketRegister) args[0];
                    ConsoleColours.Print(ConsoleColours.BLACK_BOLD+ConsoleColours.YELLOW_BACKGROUND,"==== RECEIVED REGISTER Answer ==== " + packet.isCancelled() + HexaServer.WhatAmI(server));
                    System.out.println();

                }
            });

            put(PacketType.JOIN, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.BLACK_BOLD+ConsoleColours.YELLOW_BACKGROUND,"Received JOIN" + HexaServer.WhatAmI(server));
                    System.out.println();

                    PacketJoin packet = (PacketJoin) args[0];

                    // I'm not the host, so either a new player has joined the game or I have joined the game
                    System.out.println(HexaServer.senderId.toString() + " ////// " + packet.getHostId().toString());
                    if (packet.getHostId().equals(HexaServer.senderId)) {
                        System.out.println("==== YOU HAVE JOINED THE GAME!! (Username: " + packet.getUsername() + ")");
                        ScreenManager.getInstance().setCurrentScreen(ScreenType.LOBBY);
                    } else {
                        System.out.println(packet.getUsername() + " has joined the game");
                        GameManager.instance.messageUtil.add(packet.getUsername() + " has joined the room!");
                    }

                }
            });

            put(PacketType.LEAVE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.BLACK_BOLD+ConsoleColours.YELLOW_BACKGROUND,"Received LEAVE" + HexaServer.WhatAmI(server));
                    PacketLeave packetLeave = (PacketLeave) args[0];

                    String message;
                    if (packetLeave.isKick()) {
                        message = "(" + packetLeave.getLeaverUuid() + ") has been kicked";
                    } else {
                        message = "(" + packetLeave.getLeaverUuid() + ") has left the game";
                    }

                    if (packetLeave.getLeaverUuid().equals(HexaServer.senderId)) {
                        ConsoleColours.Print(ConsoleColours.BLACK_BOLD+ConsoleColours.YELLOW_BACKGROUND,"##### YOU - " + message + HexaServer.WhatAmI(server));
                        server.disconnect();
                        ScreenManager.getInstance().setCurrentScreen(ScreenType.MAIN_MENU);
                    } else {
                        ConsoleColours.Print(ConsoleColours.BLACK_BOLD+ConsoleColours.YELLOW_BACKGROUND,"##### SOMEONE ELSE - " + message + HexaServer.WhatAmI(server));
                        GameManager.instance.messageUtil.add(message);

                    }


                }
            });

            put(PacketType.BUILD, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.BLACK_BOLD+ConsoleColours.YELLOW_BACKGROUND,"Received BUILD" + HexaServer.WhatAmI(server));

                    PacketBuild build = (PacketBuild) args[0];
                    ConsoleColours.Print(ConsoleColours.BLACK+ConsoleColours.YELLOW_BACKGROUND," Received BUILD " + build.getArrayPosition().getX() + ", " + build.getArrayPosition().getY()
                            + " -> " + build.getStructureType().name() + HexaServer.WhatAmI(server));

                    if(server.isHost() && build.getStructureType() == StructureType.ORE)
                    {
                        ConsoleColours.Print(ConsoleColours.BLACK_BOLD+ConsoleColours.YELLOW_BACKGROUND,"Received Build Packet for: " + build.getOwner() + "|| I am: "+HexaServer.senderId + HexaServer.WhatAmI(server));
                        ConsoleColours.Print(ConsoleColours.BLACK_BOLD+ConsoleColours.YELLOW_BACKGROUND,"    Build structure: " + build.getStructureType() + " at " + build.getArrayPosition());



                        synchronized (lock){
                                Engine.getInstance().BroadcastMessage(
                                        new NotificationNewEntity(
                                                Engine.getInstance().getEntityManager().createID(
                                                        new HexaComponentOwner(build.getOwner().toString(),build.getOwner()),
                                                        new ComponentProducer(),
                                                        new ComponentResource(
                                                                0.000002f,
                                                                1.0f,
                                                                1.0f,
                                                                asList(
                                                                        new Component[]  {
                                                                                new HexaComponentOre()
                                                                        }
                                                                )
                                                        )
                                                )
                                        )
                            );
                        }
                    }else
                        ;

                    Point pos = build.getArrayPosition();
                    HexMap map = GameManager.instance.getGame().getCurrentMap();

                    map.build(pos.getX(), pos.getY(), build.getStructureType(), build.getOwner());
                    GameManager.instance.getInputGame().updateSelectedInfo();
                }
            });

            put(PacketType.DESTROY, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.BLACK_BOLD+ConsoleColours.YELLOW_BACKGROUND,"Received DESTROY" + HexaServer.WhatAmI(server));
                    System.out.println();

                    PacketDestroy destroy = (PacketDestroy) args[0];
                    Point pos = destroy.getArrayPosition();
                    HexMap map = GameManager.instance.getGame().getCurrentMap();

                    map.deconstruct(pos.getX(), pos.getY());
                    GameManager.instance.getInputGame().updateSelectedInfo();
                }
            });


            put(PacketType.PLAYER_STATUS, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.BLACK_BOLD+ConsoleColours.YELLOW_BACKGROUND,"Received PLAYER_STATUS" + HexaServer.WhatAmI(server));
                    System.out.println();
                    PacketPlayerStatus player = (PacketPlayerStatus)args[0];

                    ConsoleColours.Print(ConsoleColours.BLACK+ConsoleColours.YELLOW_BACKGROUND,"Received Packet for(PLAYERID): " + player.PlayerID + "|| I am(SERVERID): "+HexaServer.senderId  + HexaServer.WhatAmI(server));
                    ConsoleColours.Print(ConsoleColours.BLACK+ConsoleColours.YELLOW_BACKGROUND,"   It contains this payload: " + player.Stats + HexaServer.WhatAmI(server));


                    /*if(player.PlayerID == player.getSenderId())
                        System.out.println("Status for HOST");
                    else
                        System.out.println("Status for Client");
                        */

                    if(player.PlayerID.equals(HexaServer.senderId)){
                        ConsoleColours.Print(ConsoleColours.BLACK+ConsoleColours.YELLOW_BACKGROUND,"        Set given stats for:" + player.PlayerID + "|| I am: " + HexaServer.WhatAmI(server));

                        GameManager.instance.setPlayerResources(player.Stats);
                    }
                      //if(!server.isHost())
                        //GameManager.instance.setPlayerResources(player.Stats);


                }
            });

            put(PacketType.TRADE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.BLACK_BOLD+ConsoleColours.YELLOW_BACKGROUND,"Received TRADE" + HexaServer.WhatAmI(server));
                    System.out.println();

                }
            });

            put(PacketType.TERMINATE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.BLACK_BOLD+ConsoleColours.YELLOW_BACKGROUND,"Received TERMINATE" + HexaServer.WhatAmI(server));
                    System.out.println();

                }
            });

            put(PacketType.MAPUPDATE, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    ConsoleColours.Print(ConsoleColours.BLACK_BOLD+ConsoleColours.YELLOW_BACKGROUND,"Received MAPUPDATE" + HexaServer.WhatAmI(server));
                    System.out.println();

                    PacketMapUpdate packetMapUpdate = (PacketMapUpdate) args[0];
                    HexMap hexMap;

                    // If the player is playing offline, the packet contains a list of tiles
                    // If the player is connected to the server it will only contain the raw json
                    if (packetMapUpdate.getTiles() != null) {
                        hexMap = new HexMap(
                                packetMapUpdate.getTiles().length,
                                (packetMapUpdate.getTiles().length == 0) ? (0) : (packetMapUpdate.getTiles()[0].length)
                        );
                        hexMap.setTiles(packetMapUpdate.getTiles());
                    } else {
                        String json = packetMapUpdate.getRawMapData();

                        JsonHexMap jsonHexMap = JsonHexMap.fromJson(json);

                        hexMap = new HexMap(
                                jsonHexMap.getTiles().length,
                                (jsonHexMap.getTiles().length == 0) ? (0) : (jsonHexMap.getTiles()[0].length)
                        );

                        System.out.println("HEX MAP " + hexMap.getTiles().length);
                        hexMap.setTiles(jsonHexMap.getTiles());
                        if (!server.isHost()) {
                            if (server.getSessionData() == null) {
                                server.setSessionData(new SessionData());
                            }
                            for (UUID uuid : jsonHexMap.getColors().keySet()) {
                                server.getSessionData().addNewPlayer(uuid, "SomePlayer",
                                        Color.valueOf(jsonHexMap.getColors().get(uuid)));
                            }
                        }
                    }
                    MapManager.getInstance().setCurrentHexMap(hexMap);

                    if (!GameManager.instance.server.isHost()) {
                        GameManager.instance.server.send(new PacketPlayerLoaded());
                    }

                    //GameManager.instance.getInputGame().updateSelectedInfo();
                }
            });

            put(PacketType.SERVER_LIST, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    PacketServerList packetServerList = (PacketServerList) args[0];
                    ConsoleColours.Print(ConsoleColours.BLACK_BOLD+ConsoleColours.YELLOW_BACKGROUND,"Received SERVICE_LIST"+ packetServerList.entries.size() + HexaServer.WhatAmI(server));
                    System.out.println();


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
                                            ConsoleColours.Print(ConsoleColours.BLACK+ConsoleColours.YELLOW_BACKGROUND,"Clicked room " + entry.room + HexaServer.WhatAmI(server));
                                            screenJoin.joinRoom(entry.host, entry.room);
                                        }
                                    }), screenJoin.getStage());
                        }

                        screenJoin.subwindowServers.orderAllNeatly(1);
                        screenJoin.subwindowServers.updateElements();
                    }
                }
            });

            put(PacketType.HOST_GENERATING, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    PacketHostGenerating packet = (PacketHostGenerating) args[0];
                    ConsoleColours.Print(ConsoleColours.BLACK_BOLD+ConsoleColours.YELLOW_BACKGROUND,"Received HOST_GENERATING"+ HexaServer.WhatAmI(server));

                    System.out.println();
                    ScreenManager.getInstance().setCurrentScreen(ScreenType.GENERATOR);
                }
            });

            put(PacketType.PLAYER_LOADED, new Delegate() {
                @Override
                public void invoke(Object... args) throws Exception {
                    PacketPlayerLoaded packet = (PacketPlayerLoaded) args[0];
                    ConsoleColours.Print(ConsoleColours.BLACK_BOLD+ConsoleColours.YELLOW_BACKGROUND,"Received PLAYER_LOADED"+ HexaServer.WhatAmI(server));

                    System.out.println();

                    if (ScreenManager.getInstance().getCurrentScreen().getScreenType() != ScreenType.GAME) {
                        ScreenManager.getInstance().setCurrentScreen(ScreenType.GAME);
                    }

                }
            });
        }}
        ;
    }
}
