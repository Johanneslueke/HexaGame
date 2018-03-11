package com.hexagon.game.input;

import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven on 20.12.2017.
 */

public class InputManager {

    private static InputManager instance;

    private List<InputProcessor> inputListeners = new ArrayList<>();

    public InputManager() {
        instance = this;
    }

    public void register(InputProcessor processor) {
        inputListeners.add(processor);
    }

    public void unregister(InputProcessor processor) {
        inputListeners.remove(processor);
    }

    public List<InputProcessor> getInputListeners() {
        return inputListeners;
    }

    public static InputManager getInstance() {
        return instance;
    }
}
