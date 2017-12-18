package com.hexagon.game.graphics.screens.myscreens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;

/**
 * Created by Sven on 14.12.2017.
 */

public class ScreenMainMenu extends HexagonScreen {

    private SpriteBatch batch;
    private BitmapFont font;

    public ScreenMainMenu() {
        super(ScreenType.MAIN_MENU);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenManager.getInstance().clearScreen(0.2f, 0.25f, 0.35f);
        batch.begin();
        font.draw(batch, "Main menu", 20, 20);
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

    }
}
