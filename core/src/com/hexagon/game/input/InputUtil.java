package com.hexagon.game.input;

import com.badlogic.gdx.Gdx;

/**
 * Created by Sven on 20.12.2017.
 */

public class InputUtil {

    public static float mouseX() {
        return Gdx.input.getX();
    }

    public static float mouseY() {
        return Gdx.graphics.getHeight() - Gdx.input.getY();
    }

}
