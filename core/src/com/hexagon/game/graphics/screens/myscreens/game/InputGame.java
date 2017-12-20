package com.hexagon.game.graphics.screens.myscreens.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by Sven on 20.12.2017.
 */

public class InputGame implements InputProcessor {

    private static float SPEED = 2;

    private ScreenGame screenGame;

    public InputGame(ScreenGame screenGame) {
        this.screenGame = screenGame;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.A) {
            screenGame.getCamera().position.add(SPEED, 0, 0);
            screenGame.getCamera().update();
        } else if (keycode == Input.Keys.D) {
            screenGame.getCamera().position.add(-SPEED, 0, 0);
            screenGame.getCamera().update();
        } else if (keycode == Input.Keys.W) {
            screenGame.getCamera().position.add(0, 0, SPEED);
            screenGame.getCamera().update();
        } else if (keycode == Input.Keys.S) {
            screenGame.getCamera().position.add(0, 0, -SPEED);
            screenGame.getCamera().update();
        }
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
