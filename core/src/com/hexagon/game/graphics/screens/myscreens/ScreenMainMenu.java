package com.hexagon.game.graphics.screens.myscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.ui.UiImage;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.graphics.ui.windows.DropdownScrollableWindow;
import com.hexagon.game.graphics.ui.windows.FadeWindow;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.util.MenuUtil;

/**
 * Created by Sven on 14.12.2017.
 */

/**
 * Displays the first two Menus visible to the User
 */

/**
 * Concrete implementation of an Screen.
 * according to the constructor of type >MAIN_MENU<
 *
 */
public class ScreenMainMenu extends HexagonScreen {

    private SpriteBatch batch;
    private BitmapFont font;

    public ScreenMainMenu() {
        super(ScreenType.MAIN_MENU);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        UiButton button = new UiButton("Hello World", 50, Gdx.graphics.getHeight() - 50, 100, 50);

        final DropdownScrollableWindow window = new DropdownScrollableWindow(20, 0, 0, 0, 0, 0, 15);
        windowManager.getWindowList().add(window);

        /*for (int i=0; i<400; i++) {
            UiButton buttonWindow = new UiButton(String.valueOf(i), 0, 0, 50, 20);
            window.add(buttonWindow, stage);
        }*/

        window.orderAllNeatly(13, 0, 15);
        window.setY(button.getY() - window.getHeight());
        window.setX(button.getX());
        window.updateElements();

        /*UiButton buttonWindow = new UiButton("Inside Window Button", 0, 0, 100, 50);
        buttonWindow.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Clicked inside Window Button");
            }
        });
        window.add(buttonWindow, stage);*/

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


        //MenuUtil.getInstance().createStandardMenu(this);
        final GroupWindow standardWindow = new GroupWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 224, 600, stage);

        FadeWindow fadeWindow = new FadeWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 224, 600, stage);
        fadeWindow.show(stage);
        fadeWindow.add(new UiImage(0, 0, 224, 600, "sidebar.png"), stage);



        standardWindow.getWindowList().add(fadeWindow);

        /*
         * Subwindow 1: Play
         */
        final FadeWindow subwindowPlay = new FadeWindow(fadeWindow.getX() + fadeWindow.getWidth() + 10, fadeWindow.getY(), 800 - fadeWindow.getWidth(), 600, stage);
        subwindowPlay.add(new UiImage(0, 0, 558, 600, "window_small.png"), stage);

        UiButton playText = new UiButton("> Play", 20, subwindowPlay.getHeight() - 60, 100, 40);
        UiButton playHost = new UiButton("Host Game", 40, subwindowPlay.getHeight() - 100, 200, 40);
        UiButton playJoin = new UiButton("Join Game", 40, subwindowPlay.getHeight() - 140, 200, 40);

        playHost.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().setCurrentScreen(ScreenType.HOST);
            }
        });

        subwindowPlay.add(playText, stage);
        subwindowPlay.add(playHost, stage);

        subwindowPlay.updateElements();
        standardWindow.getWindowList().add(subwindowPlay);

         /*
         * Subwindow 2
         */
        final FadeWindow subwindow2 = new FadeWindow(fadeWindow.getX() + fadeWindow.getWidth() + 10, fadeWindow.getY(), 800 - fadeWindow.getWidth(), 600, stage);
        subwindow2.add(new UiImage(0, 0, 558, 600, "window_small.png"), stage);

        UiButton text2 = new UiButton("This is subwindow 2", 40, subwindow2.getHeight() - 60, 100, 40);
        subwindow2.add(text2, stage);

        subwindow2.updateElements();
        standardWindow.getWindowList().add(subwindow2);

        /*
         * Sidebar Buttons
         */

        UiButton buttonPlay = new UiButton("Play", 20, fadeWindow.getHeight() - 60, 50, 40);
        UiButton buttonSubwindow2 = new UiButton("Subwindow 2", 20, buttonPlay.getY() - 50, 50, 40);

        buttonPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (subwindowPlay.isVisible()) {
                    standardWindow.hide(subwindowPlay, stage);
                } else {
                    standardWindow.show(subwindowPlay, stage);
                }
            }
        });

        buttonSubwindow2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (subwindow2.isVisible()) {
                    standardWindow.hide(subwindow2, stage);
                } else {
                    standardWindow.show(subwindow2, stage);
                }
            }
        });

        fadeWindow.add(buttonPlay, stage);
        fadeWindow.add(buttonSubwindow2, stage);

        fadeWindow.updateElements();

        this.windowManager.getWindowList().add(standardWindow);

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            //windowManager.getWindowList().get(0).setX(windowManager.getWindowList().get(0).getX() + 10);
            //windowManager.getWindowList().get(0).updateElements();
            /*if (windowManager.getWindowList().get(1).isVisible()) {
                windowManager.getWindowList().get(1).hide(stage);
            } else {
                windowManager.getWindowList().get(1).show(stage);
            }*/
        }

        ScreenManager.getInstance().clearScreen(0.2f, 0.25f, 0.35f);
        batch.begin();
        font.draw(batch, "Main menu", 20, 20);
        batch.end();


        Gdx.gl.glEnable(GL20.GL_BLEND); // allows transparent drawing
        shapeRenderer.begin();
        windowManager.render(shapeRenderer);
        shapeRenderer.end();

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
