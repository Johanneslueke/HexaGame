package com.hexagon.game.graphics.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.hexagon.game.Main;

import com.hexagon.game.graphics.screens.myscreens.DemoScreen;
import com.hexagon.game.graphics.screens.myscreens.game.ScreenGame;

import com.hexagon.game.graphics.screens.myscreens.ScreenGenerator;

import com.hexagon.game.graphics.screens.myscreens.ScreenLoading;
import com.hexagon.game.graphics.screens.myscreens.ScreenMainMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven on 18.12.2017.
 */

public class ScreenManager {

    private static ScreenManager instance;

    private List<HexagonScreen> screenList;

    private HexagonScreen currentScreen;

    public ScreenManager() {
        instance = this;

        screenList = new ArrayList<>();
        screenList.add(new ScreenLoading());
        screenList.add(new ScreenMainMenu());
        screenList.add(new ScreenGame());

        screenList.add(new DemoScreen());

        screenList.add(new ScreenGenerator());


        this.currentScreen = screenList.get(0); // loading screen
    }

    public HexagonScreen setCurrentScreen(ScreenType type) {
        for (HexagonScreen screen : screenList) {
            if (screen.getScreenType() == type) {
                this.currentScreen = screen;
                Main.getInstance().setScreen(screen);
                return screen;
            }
        }
        // this shouldn't happen
        Gdx.app.error("Hexagon", "Screen null");
        Gdx.app.exit();
        return null;
    }

    public List<HexagonScreen> getScreenList() {
        return screenList;
    }

    public HexagonScreen getCurrentScreen() {
        return currentScreen;
    }

    public void clearScreen(float red, float green, float blue) {
        Gdx.gl.glClearColor(red, green, blue, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public static ScreenManager getInstance() {
        return instance;
    }
}
