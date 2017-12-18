package com.hexagon.game.graphics.screens;

import com.badlogic.gdx.Screen;

/**
 * Created by Sven on 18.12.2017.
 */

public abstract class HexagonScreen implements Screen {

    private ScreenType screenType;

    public HexagonScreen(ScreenType screenType) {
        this.screenType = screenType;
    }

    public ScreenType getScreenType() {
        return screenType;
    }

    public abstract void create();
}
