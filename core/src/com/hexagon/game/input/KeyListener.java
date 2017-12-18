package com.hexagon.game.input;

import com.badlogic.gdx.InputProcessor;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;

/**
 * Created by Sven on 18.12.2017.
 */

public class KeyListener implements InputProcessor {

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // for testing only
        if (character == '1') {
            ScreenManager.getInstance().setCurrentScreen(ScreenType.LOADING);
        } else if (character == '2') {
            ScreenManager.getInstance().setCurrentScreen(ScreenType.MAIN_MENU);
        } else if (character == '3') {
            ScreenManager.getInstance().setCurrentScreen(ScreenType.GAME);
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
