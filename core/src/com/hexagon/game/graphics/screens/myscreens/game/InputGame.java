package com.hexagon.game.graphics.screens.myscreens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;

/**
 * Created by Sven on 20.12.2017.
 */

public class InputGame implements InputProcessor {

    private static float SPEED = 5;

    private float velX = 0;
    private float velY = 0;

    private ScreenGame screenGame;

    public InputGame(ScreenGame screenGame) {
        this.screenGame = screenGame;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.A) {
            velX = -SPEED;
        } else if (keycode == Input.Keys.D) {
            velX = SPEED;
        }
        if (keycode == Input.Keys.W) {
            velY = -SPEED;
        } else if (keycode == Input.Keys.S) {
            velY = SPEED;
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
        screenGame.getCamera().fieldOfView += amount*3f;
        screenGame.getCamera().update();
        return true;
    }

    public void update(float delta) {
        if (velX == 0 && velY == 0) {
            return;
        }
        Camera camera = screenGame.getCamera();
        camera.position.add(velX * delta, 0, velY * delta);

        camera.update();

        if (!Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
            velY *= 0.8;
            if (velY >= -0.01 && velY <= 0.01) {
                velY = 0;
            }
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
            velX *= 0.8;
            if (velX >= -0.01 && velX <= 0.01) {
                velX = 0;
            }
        }
    }
}
