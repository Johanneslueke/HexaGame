package com.hexagon.game.graphics.screens.myscreens;

import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenType;

/**
 * Created by Sven on 14.12.2017.
 */

public class ScreenGame extends HexagonScreen {

    public ScreenGame() {
        super(ScreenType.GAME);
    }


    @Override
    public void create() {
        int abc = 0;
        for (int i=0; i<0xFFFFFFF; i++) {
            abc += 10;
            abc *= 0.25;
            ScreenLoading.loadedIndividual = ((float) i) / 0xFFFFFFF;
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
