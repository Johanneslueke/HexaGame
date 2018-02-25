package com.hexagon.game.graphics.screens.myscreens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.screens.myscreens.ScreenJoin;
import com.hexagon.game.graphics.screens.myscreens.game.GameUI.IngameMenu;
import com.hexagon.game.graphics.screens.myscreens.game.GameUI.StatusBar;
import com.hexagon.game.graphics.ui.UILabel;
import com.hexagon.game.graphics.ui.UiImage;
import com.hexagon.game.graphics.ui.UpdateEvent;
import com.hexagon.game.graphics.ui.WindowManager;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.graphics.ui.windows.DropdownWindow;
import com.hexagon.game.graphics.ui.windows.FadeWindow;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.graphics.ui.windows.Window;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.packets.PacketJoin;
import com.hexagon.game.network.packets.PacketKeepAlive;
import com.hexagon.game.network.packets.PacketServerList;
import com.hexagon.game.network.packets.PacketType;
import com.hexagon.game.util.MenuUtil;

import java.io.IOException;
import java.util.Hashtable;
import java.util.UUID;

import de.svdragster.logica.util.Delegate;

/**
 * Created by Sven on 16.02.2018.
 */

public class GameManager {

    public static GameManager instance;

    private ScreenGame      game;
    private WindowManager   windowManager;
    private Stage           stage;
    ShapeRenderer           shapeRenderer;
    GroupWindow             standardWindow ;
    FadeWindow              spaceWindow;


    public HexaServer                  server;

    public GameManager() {
        instance = this;

        standardWindow = new GroupWindow(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),stage);

    }

    public void startGame(ScreenGame game) {
        this.game = game;
        this.windowManager = game.getWindowManager();
        this.stage = game.getStage();
    }

    public void connect(boolean isHost) {
        server = new HexaServer(
                "localhost",
                25565,
                isHost
        );

        if (isHost) {
            HexaServer.senderId = UUID.fromString("e84223f7-f8dd-4ea4-8494-25ef9d27a1a9");
        } else {
            HexaServer.senderId = UUID.fromString("525183d9-1a5a-40e1-a712-e3099282c341");
        }

        server.setDispatchTable(new Hashtable<PacketType, Delegate>() {{
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
                        if (server.isHost()) {
                            server.getSessionData().addNewPlayer(packet.getSenderId(), packet.getUsername());
                            System.out.println(packet.getUsername() + " has joined the game (I AM THE SERVER)");

                            // I'm the host, so I have to broadcast to my players that a new player has joined the game
                            server.send(new PacketJoin(packet.getUsername(), packet.getSenderId(), packet.getVersion()));
                        } else {

                            // I'm not the host, so either a new player has joined the game or I have joined the game
                            System.out.println(HexaServer.senderId.toString() + " ////// " + packet.getHostId().toString());
                            if (packet.getHostId().equals(HexaServer.senderId)) {
                                System.out.println("==== YOU HAVE JOINED THE GAME!! (Username: " + packet.getUsername() + ")");
                                ScreenManager.getInstance().setCurrentScreen(ScreenType.LOBBY);
                            } else {
                                System.out.println(packet.getUsername() + " has joined the game");
                            }
                        }

                    }
                });

                put(PacketType.LEAVE, new Delegate() {
                    @Override
                    public void invoke(Object... args) throws Exception {
                        System.out.println("Received LEAVE");

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
        );

        try {
            server.connect(1000);
        } catch (IOException e) {
            System.out.println("Could not connect!");
            //e.printStackTrace();
        }
    }

    public void createAll() {
        shapeRenderer = new ShapeRenderer();
        createButtons();
        createWindows();
    }

    public void createButtons() {
        /*UiButton button = new UiButton("Hello World", 50, Gdx.graphics.getHeight() - 50, 100, 50);

        final DropdownScrollableWindow window = new DropdownScrollableWindow(20, 0, 0, 0, 0, 0, 15);
        windowManager.getWindowList().add(window);

        for (int i=0; i<20; i++) {
            UiButton buttonWindow = new UiButton(String.valueOf(i), 0, 0, 50, 25);
            window.add(buttonWindow, stage);
        }

        window.orderAllNeatly(13, 0, 15);
        window.setY(button.getY() - window.getHeight());
        window.setX(button.getX());
        window.updateElements();

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (window.isVisible()) {
                    window.hide(stage);
                } else {
                    window.show(stage);
                }
            }
        });
        button.addToStage(stage);

        UiSkinButton skinButton = new UiSkinButton("Das ist ein Text", Gdx.graphics.getWidth() - 250, 50, 250, 100);
        skinButton.addToStage(stage);

        */
    }

    ///////////////////////
    //// W I N D O W S ////
    ///////////////////////

    public void createWindows() {
        //standardWindow = new GroupWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 800, 600, stage);
        createSpaceWindow();
        createStatusbar();
    }

    public void createSpaceWindow() {
        spaceWindow = new FadeWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 800, 600, stage);
        spaceWindow.add(new UiImage(0, 0, 800, 600, "window.png"), stage);
        windowManager.getWindowList().add(spaceWindow);
        //standardWindow.getWindowList().add(spaceWindow);
    }

    public void createStatusbar() {
        StatusBar statusBar = new StatusBar(stage,standardWindow);

        windowManager.getWindowList().add(standardWindow);
        statusBar.MainMenu.MenuContent.hide(stage);

    }



    public ScreenGame getGame() {
        return game;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
