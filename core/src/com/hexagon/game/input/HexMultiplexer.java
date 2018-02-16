package com.hexagon.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven on 18.12.2017.
 */

public class HexMultiplexer {

    private static HexMultiplexer instance;

    private List<InputProcessor> processorList;
    private InputMultiplexer multiplexer;

    public HexMultiplexer() {
        instance = this;

        processorList = new ArrayList<>();
    }

    public void remove(InputProcessor processor) {
        processorList.remove(processor);
    }

    public void add(InputProcessor processor) {
        if (processorList.contains(processor)) {
            return;
        }
        processorList.add(processor);
    }

    public void multiplex() {
        multiplexer = new InputMultiplexer();
        for (InputProcessor processor : processorList) {
            multiplexer.addProcessor(processor);
        }
        Gdx.input.setInputProcessor(multiplexer);
    }

    public static HexMultiplexer getInstance() {
        return instance;
    }
}
