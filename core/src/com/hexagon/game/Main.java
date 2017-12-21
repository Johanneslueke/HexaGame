package com.hexagon.game;

import com.badlogic.gdx.Game;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.ui.WindowManager;
import com.hexagon.game.input.HexMultiplexer;
import com.hexagon.game.input.InputManager;
import com.hexagon.game.input.KeyListener;
import com.hexagon.game.map.MapManager;
import com.hexagon.game.util.MenuUtil;

public class Main extends Game {

    private static Main instance;

	@Override
	public void create () {
	    instance = this;

	    new MenuUtil();
        new InputManager();
        new MapManager();
	    new HexMultiplexer();
        HexMultiplexer.getInstance().add(new KeyListener());
        HexMultiplexer.getInstance().multiplex();

	    new WindowManager();
		new ScreenManager();

		// The first screen is the loading screen which will load all other screens
		ScreenManager.getInstance().getCurrentScreen().create();


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
