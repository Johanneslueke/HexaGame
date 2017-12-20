package com.hexagon.game.graphics.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.ui.WindowManager;
import com.hexagon.game.input.InputManager;

/**
 * Created by Sven on 18.12.2017.
 */

public abstract class HexagonScreen implements Screen {

    private ScreenType screenType;

    protected WindowManager windowManager;

    protected Stage stage;

    public HexagonScreen(ScreenType screenType) {
        this.screenType = screenType;
        stage = new Stage();
        windowManager = new WindowManager();
    }

    public ScreenType getScreenType() {
        return screenType;
    }

    public WindowManager getWindowManager() {
        return windowManager;
    }

    public abstract void create();

    public Stage getStage() {
        return stage;
    }

    @Override
    public void show() {
        InputManager.getInstance().register(this.getStage());
        InputManager.getInstance().register(this.windowManager);
        //HexMultiplexer.getInstance().add(this.getStage());
        //HexMultiplexer.getInstance().add(this.windowManager);
        //HexMultiplexer.getInstance().multiplex();
    }

    @Override
    public void hide() {
        //HexMultiplexer.getInstance().remove(this.getStage());
        //HexMultiplexer.getInstance().remove(this.windowManager);
        //HexMultiplexer.getInstance().multiplex();
        InputManager.getInstance().unregister(this.getStage());
        InputManager.getInstance().unregister(this.windowManager);
    }
}
