package com.hexagon.game.graphics.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.input.HexMultiplexer;

/**
 * Created by Sven on 18.12.2017.
 */

public abstract class HexagonScreen implements Screen {

    private ScreenType screenType;

    protected Stage stage;

    public HexagonScreen(ScreenType screenType) {
        this.screenType = screenType;
        stage = new Stage();
    }

    public ScreenType getScreenType() {
        return screenType;
    }

    public abstract void create();

    public Stage getStage() {
        return stage;
    }

    @Override
    public void show() {
        HexMultiplexer.getInstance().add(this.getStage());
        HexMultiplexer.getInstance().multiplex();
    }

    @Override
    public void hide() {
        HexMultiplexer.getInstance().remove(this.getStage());
        HexMultiplexer.getInstance().multiplex();
    }
}
