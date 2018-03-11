package com.hexagon.game.input;

import com.badlogic.gdx.InputProcessor;

/**
 * Created by Sven on 16.02.2018.
 */

public abstract class HexInput implements InputProcessor {

    private boolean         disableMouse = false;

    public boolean isDisableMouse() {
        return disableMouse;
    }

    public void setDisableMouse(boolean disableMouse) {
        this.disableMouse = disableMouse;
    }
}
