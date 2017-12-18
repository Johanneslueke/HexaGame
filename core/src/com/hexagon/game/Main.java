package com.hexagon.game;

import com.badlogic.gdx.Game;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.input.HexMultiplexer;
import com.hexagon.game.input.KeyListener;

public class Main extends Game {

    private static Main instance;

	@Override
	public void create () {
	    instance = this;

	    new HexMultiplexer();

		new ScreenManager();
		// The first screen is the loading screen which will load all other screens
		ScreenManager.getInstance().getCurrentScreen().create();

        HexMultiplexer.getInstance().add(new KeyListener());
        HexMultiplexer.getInstance().multiplex();
    }

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {

	}

    public static Main getInstance() {
        return instance;
    }
}
