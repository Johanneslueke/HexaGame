package com.hexagon.game.graphics.screens.myscreens.game.GameStates;

/**
 * Created by Johannes on 06.03.2018.
 */

public abstract class State {

    private StateType stateType;

    public State(StateType stateType) {
        this.stateType = stateType;
    }

    public abstract void render();
    public abstract void logic();

    public abstract void show();
    public abstract void hide();

    public StateType getStateType() {
        return stateType;
    }
}
