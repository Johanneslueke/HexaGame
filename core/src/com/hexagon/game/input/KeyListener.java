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
        for (InputProcessor processor : InputManager.getInstance().getInputListeners()) {
            if (processor.keyDown(keycode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        for (InputProcessor processor : InputManager.getInstance().getInputListeners()) {
            if (processor.keyUp(keycode)) {
                return true;
            }
        }
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
        for (InputProcessor processor : InputManager.getInstance().getInputListeners()) {
            if (processor.keyTyped(character)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for (InputProcessor processor : InputManager.getInstance().getInputListeners()) {
            if (processor.touchDown(screenX, screenY, pointer, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for (InputProcessor processor : InputManager.getInstance().getInputListeners()) {
            if (processor.touchUp(screenX, screenY, pointer, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        for (InputProcessor processor : InputManager.getInstance().getInputListeners()) {
            if (processor.touchDragged(screenX, screenY, pointer)) {
                return true;
            }
        }
        return false;
    }

    // workaround against concurrent modification exception
    private boolean workaround = true;

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (workaround) {
            workaround = false;
            return false;
        }
        for (InputProcessor processor : InputManager.getInstance().getInputListeners()) {
            if (processor.mouseMoved(screenX, screenY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        for (InputProcessor processor : InputManager.getInstance().getInputListeners()) {
            if (processor.scrolled(amount)) {
                return true;
            }
        }
        return false;
    }
}
