package com.hexagon.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.input.KeyListener;


public class Main extends Game {

    private static Main instance; //You Shall not PASS

	@Override
	public void create () {
	    instance = this;

		new ScreenManager();
		// The first screen is the loading screen which will load all other screens
		ScreenManager.getInstance().getCurrentScreen().create();

        Gdx.input.setInputProcessor(new KeyListener());
    }

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}

    public static Main getInstance() {
        return instance;
    }
}
