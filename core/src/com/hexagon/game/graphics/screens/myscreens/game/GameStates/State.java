package com.hexagon.game.graphics.screens.myscreens.game.GameStates;

/**
 * Created by Johannes on 06.03.2018.
 */

public interface State {

    void render();
    void logic();

    void show();
    void hide();
}
