package com.hexagon.game.graphics.screens.myscreens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.ui.UiImage;
import com.hexagon.game.graphics.ui.WindowManager;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.graphics.ui.buttons.UiSkinButton;
import com.hexagon.game.graphics.ui.windows.DropdownScrollableWindow;
import com.hexagon.game.graphics.ui.windows.FadeWindow;
import com.hexagon.game.graphics.ui.windows.StandardWindow;
import com.hexagon.game.util.MenuUtil;

/**
 * Created by Sven on 16.02.2018.
 */

public class GameManager {

    private ScreenGame      game;
    private WindowManager   windowManager;
    private Stage           stage;

    ShapeRenderer           shapeRenderer;

    StandardWindow          standardWindow;

    FadeWindow              spaceWindow;

    public GameManager(ScreenGame game) {
        this.game = game;
        this.windowManager = game.getWindowManager();
        this.stage = game.getStage();
    }

    public void createAll() {
        shapeRenderer = new ShapeRenderer();
        createButtons();
        createWindows();
    }

    public void createButtons() {
        UiButton button = new UiButton("Hello World", 50, Gdx.graphics.getHeight() - 50, 100, 50);

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
    }

    ///////////////////////
    //// W I N D O W S ////
    ///////////////////////

    public void createWindows() {
        //standardWindow = new StandardWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 800, 600, stage);
        createSpaceWindow();
    }

    public void createSpaceWindow() {
        spaceWindow = new FadeWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 800, 600, stage);
        spaceWindow.add(new UiImage(0, 0, 800, 600, "window.png"), stage);
        game.getWindowManager().getWindowList().add(spaceWindow);
        //standardWindow.getWindowList().add(spaceWindow);
    }


}
