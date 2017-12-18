package com.hexagon.game.graphics.screens.myscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.input.HexMultiplexer;

/**
 * Created by Sven on 14.12.2017.
 */

public class ScreenLoading extends HexagonScreen {

    public static float loadedIndividual = 0;

    private SpriteBatch batch;
    private BitmapFont font;
    private float loaded = 0;
    private String currentlyLoading = "";

    private float brightness = -1;


    public ScreenLoading() {
        super(ScreenType.LOADING);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                for (final HexagonScreen screen : ScreenManager.getInstance().getScreenList()) {
                    i++;
                    if (screen.getScreenType() == ScreenType.LOADING) {
                        continue;
                    }
                    loadedIndividual = 0;
                    currentlyLoading = screen.getScreenType().name();
                    //Gdx.app.postRunnable(screen::create); // run the creation on the libgdx thread
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            screen.create();
                        }
                    });
                    loadedIndividual = 1;
                    loaded = ((float) i) / ScreenManager.getInstance().getScreenList().size();
                    if (i == ScreenManager.getInstance().getScreenList().size() - 1) {
                        // done loading
                        doneLoading();
                    }
                }
            }
        }).start();


        ScreenManager.getInstance().setCurrentScreen(ScreenType.LOADING);
    }

    public void doneLoading() {
        HexMultiplexer.getInstance().add(this.getStage());
        HexMultiplexer.getInstance().multiplex();
    }

    @Override
    public void render(float delta) {
        if (brightness <= 1) {
            if (brightness < 0) {
                brightness = 0;
            } else {
                brightness += 0.45 * delta;
            }
        }

        ScreenManager.getInstance().clearScreen(0.05f * brightness, 0.15f * brightness, 0.2f * brightness);
        batch.begin();
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
    public void dispose() {
        batch.dispose();
    }
}
