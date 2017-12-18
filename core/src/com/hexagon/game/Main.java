package com.hexagon.game;

import com.badlogic.gdx.Game;
import com.hexagon.game.graphics.screens.ScreenManager;

public class Main extends Game {

    private static Main instance;

	@Override
	public void create () {
	    instance = this;
		new ScreenManager();
	}

	@Override
	public void render () {
		ScreenManager.getInstance().clearScreen();
		super.render();
	}
	
	@Override
	public void dispose () {

	}

    public static Main getInstance() {
        return instance;
    }
}
