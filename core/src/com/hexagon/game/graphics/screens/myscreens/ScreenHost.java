package com.hexagon.game.graphics.screens.myscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.ui.UiImage;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.graphics.ui.windows.DropdownScrollableWindow;
import com.hexagon.game.graphics.ui.windows.FadeWindow;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.packets.PacketRegister;
import com.hexagon.game.util.MenuUtil;

/**
 * Created by Sven on 14.12.2017.
 */

/**
 * Allows you to create a server that other players can join to
 */
public class ScreenHost extends HexagonScreen {

    private SpriteBatch batch;
    private BitmapFont font;

    public ScreenHost() {
        super(ScreenType.HOST);
    }

    private void setupUserInterface(){

        UiButton button = new UiButton("Back", 50, Gdx.graphics.getHeight() - 50, 100, 50);
        button.addToStage(stage);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().setCurrentScreen(ScreenType.MAIN_MENU);
            }
        });


        //MenuUtil.getInstance().createStandardMenu(this);
        final GroupWindow standardWindow = new GroupWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 224, 600, stage);

        FadeWindow fadeWindow = new FadeWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 224, 600, stage);
        fadeWindow.show(stage);
        fadeWindow.add(new UiImage(0, 0, 224, 600, "sidebar.png"), stage);



        standardWindow.getWindowList().add(fadeWindow);

        /*
         * Subwindow 1: Players
         */
        final DropdownScrollableWindow subwindowPlayers = new DropdownScrollableWindow(fadeWindow.getX() + fadeWindow.getWidth() + 10, fadeWindow.getY(), 800 - fadeWindow.getWidth(), 600, 5, 5, 6);
        subwindowPlayers.add(new UiImage(0, 0, 558, 600, "window_small.png"), stage);

        UiButton playersText = new UiButton("You (Host)", 0, 0, 100, 40);
        UiButton playersText2 = new UiButton("Player2", 0, 0, 100, 40);
        UiButton playersText3 = new UiButton("Player3", 0, 0, 100, 40);


        subwindowPlayers.add(playersText, stage);
        subwindowPlayers.add(playersText2, stage);
        subwindowPlayers.add(playersText3, stage);

        subwindowPlayers.orderAllNeatly(1);

        subwindowPlayers.updateElements();
        standardWindow.getWindowList().add(subwindowPlayers);

        /*
         * Sidebar Buttons
         */

        UiButton buttonPlay = new UiButton("Players", 20, fadeWindow.getHeight() - 60, 50, 40);
        UiButton buttonGenerateWorld = new UiButton("Generate\nWorld", 20, buttonPlay.getY() - 80, 50, 40);

        buttonPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (subwindowPlayers.isVisible()) {
                    standardWindow.hide(subwindowPlayers, stage);
                } else {
                    standardWindow.show(subwindowPlayers, stage);
                }
            }
        });

        buttonGenerateWorld.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().setCurrentScreen(ScreenType.GENERATOR);
            }
        });

        fadeWindow.add(buttonPlay, stage);
        fadeWindow.add(buttonGenerateWorld, stage);

        fadeWindow.updateElements();

        this.windowManager.getWindowList().add(standardWindow);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        setupUserInterface();

    }

    @Override
    public void show() {
        super.show();

        GameManager gameManager = GameManager.instance;
        gameManager.connect(true);
        gameManager.server.send(new PacketRegister(
            HexaServer.senderId, // This is the host id
            "Raum " + ((int) (Math.random()*100)+1)
        ));
    }

    private float callEventsTime = 0;
    private void update(float delta) {
        if (GameManager.instance.server != null) {

            callEventsTime += delta;
            if (callEventsTime >= 1.0) {
                GameManager.instance.server.callEvents();
                callEventsTime = 0;
            }
        }
    }

    @Override
    public void render(float delta) {
        this.update(delta);

        ScreenManager.getInstance().clearScreen(0.2f, 0.25f, 0.35f);
        batch.begin();
        font.draw(batch, "Host a game", 20, 20);
        batch.end();
        stage.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
        stage.dispose();
        batch.dispose();
    }
}
