package com.hexagon.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.hexagon.game.Logic.Systems.HexaSystemGeneralConsumer;
import com.hexagon.game.Logic.Systems.HexaSystemGeneralProducer;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.ui.WindowManager;
import com.hexagon.game.input.HexMultiplexer;
import com.hexagon.game.input.InputManager;
import com.hexagon.game.input.KeyListener;
import com.hexagon.game.map.MapManager;
import com.hexagon.game.util.FontManager;
import com.hexagon.game.util.MenuUtil;

import de.svdragster.logica.system.SystemMessageDelivery;
import de.svdragster.logica.world.Engine;


public class Main extends Game {

    private static Main instance; //You Shall not PASS

    public static  Engine engine = Engine.getInstance();

	@Override
	public void create () {
	    instance = this;

		new GameManager();
	    new MenuUtil();
        new InputManager();
        new MapManager();
	    new HexMultiplexer();

        HexMultiplexer.getInstance().add(new KeyListener());
        HexMultiplexer.getInstance().multiplex();

	    new WindowManager();
		new ScreenManager();

        FontManager.init();

		// The first screen is the loading screen which will load all other screens
		ScreenManager.getInstance().getCurrentScreen().create();


        Gdx.input.setInputProcessor(new KeyListener());

        engine = Engine.getInstance();
        engine.getSystemManager().addSystem(
                new SystemMessageDelivery(),
                new HexaSystemGeneralProducer(engine),
                new HexaSystemGeneralConsumer(engine)
        );




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
