package com.hexagon.game.graphics.screens.myscreens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;

/**
 * Created by Sven on 14.12.2017.
 */

public class ScreenLoading extends HexagonScreen {

    public static float loadedIndividual = 0;

    private SpriteBatch batch;
    private Texture img;
    private BitmapFont font;
    private float loaded = 0;
    private String currentlyLoading = "";


    public ScreenLoading() {
        super(ScreenType.LOADING);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        new Thread(() -> {
            int i=0;
            for (HexagonScreen screen : ScreenManager.getInstance().getScreenList()) {
                i++;
                if (screen.getScreenType() == ScreenType.LOADING) {
                    continue;
                }
                loadedIndividual = 0;
                currentlyLoading = screen.getScreenType().name();
                screen.create();
                loaded = ((float) i) / ScreenManager.getInstance().getScreenList().size();
            }
        }).start();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();
        //batch.draw(img, 0, 0);
        font.draw(batch, "Loading " + (Math.round(loaded*100)) + "% (" + currentlyLoading + " " + (Math.round(loadedIndividual*100)) + "%)", 20, 20);
        batch.end();
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
        batch.dispose();
        img.dispose();
    }
}
