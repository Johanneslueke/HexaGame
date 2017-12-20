package com.hexagon.game.graphics.ui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hexagon.game.graphics.ui.windows.DropdownScrollableWindow;
import com.hexagon.game.graphics.ui.windows.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven on 19.12.2017.
 */

public class WindowManager implements InputProcessor {

    private List<Window> windowList;

    public WindowManager() {
        windowList = new ArrayList<>();
    }


    public List<Window> getWindowList() {
        return windowList;
    }

    public void render(ShapeRenderer renderer) {
        for (Window window : windowList) {
            window.render(renderer);
        }
    }

    public void registerInput() {

    }

    public void unregisterInput() {

    }

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
        return false;
    }


    private int startTouchY = -1;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        startTouchY = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }


    int lastDiff = 0;

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        int diff = screenY - startTouchY;
        int amount = 0;
        if (diff > 10) {
            amount = 1;
            startTouchY = screenY;
        } else if (diff < -10) {
            amount = -1;
            startTouchY = screenY;
        }
        if (lastDiff < 0 && diff >= 0) {
            startTouchY = screenY;
        } else if (lastDiff > 0 && diff <= 0) {
            startTouchY = screenY;
        }
        lastDiff = diff;
        for (Window window : windowList) {
            if (window instanceof DropdownScrollableWindow) {
                if (((DropdownScrollableWindow) window).scroll(amount)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        for (Window window : windowList) {
            if (window instanceof DropdownScrollableWindow) {
                if (((DropdownScrollableWindow) window).scroll(amount)) {
                    return true;
                }
            }
        }
        return false;
    }
}
