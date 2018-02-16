package com.hexagon.game.input;

import com.badlogic.gdx.InputProcessor;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven on 18.12.2017.
 */

public class KeyListener implements InputProcessor {

    private List<InputProcessor> processorList = new ArrayList<>();

    private boolean mouseEnabled(InputProcessor inputProcessor) {
        if (!(inputProcessor instanceof HexInput)) {
            return true;
        }
        return ! ((HexInput) inputProcessor).isDisableMouse();
    }

    @Override
    public boolean keyDown(int keycode) {
        processorList.clear();
        processorList.addAll(InputManager.getInstance().getInputListeners());
        for (InputProcessor processor : processorList) {
            if (processor.keyDown(keycode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        processorList.clear();
        processorList.addAll(InputManager.getInstance().getInputListeners());
        for (InputProcessor processor : processorList) {
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
        } else if (character == '4') {
            ScreenManager.getInstance().setCurrentScreen(ScreenType.DEMOJoJo);
        }
        processorList.clear();
        processorList.addAll(InputManager.getInstance().getInputListeners());
        for (InputProcessor processor : processorList) {
            if (processor.keyTyped(character)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        processorList.clear();
        processorList.addAll(InputManager.getInstance().getInputListeners());
        for (InputProcessor processor : processorList) {
            if (mouseEnabled(processor)
                && processor.touchDown(screenX, screenY, pointer, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        processorList.clear();
        processorList.addAll(InputManager.getInstance().getInputListeners());
        for (InputProcessor processor : processorList) {
            if (mouseEnabled(processor)
                    && processor.touchUp(screenX, screenY, pointer, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        processorList.clear();
        processorList.addAll(InputManager.getInstance().getInputListeners());
        for (InputProcessor processor : processorList) {
            if (mouseEnabled(processor)
                    && processor.touchDragged(screenX, screenY, pointer)) {
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
        processorList.clear();
        processorList.addAll(InputManager.getInstance().getInputListeners());
        for (InputProcessor processor : processorList) {
            if (mouseEnabled(processor)
                    && processor.mouseMoved(screenX, screenY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        processorList.clear();
        processorList.addAll(InputManager.getInstance().getInputListeners());
        for (InputProcessor processor : processorList) {
            if (mouseEnabled(processor)
                    && processor.scrolled(amount)) {
                return true;
            }
        }
        return false;
    }
}
