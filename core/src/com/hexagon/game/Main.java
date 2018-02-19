package com.hexagon.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.hexagon.game.Logic.Systems.HexaSystemGeneralConsumer;
import com.hexagon.game.Logic.Systems.HexaSystemGeneralProducer;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.ui.WindowManager;
import com.hexagon.game.input.HexMultiplexer;
import com.hexagon.game.input.InputManager;
import com.hexagon.game.input.KeyListener;
import com.hexagon.game.map.MapManager;
import com.hexagon.game.util.FontManager;
import com.hexagon.game.util.HexVector;
import com.hexagon.game.util.MenuUtil;

import java.util.Observable;

import de.svdragster.logica.system.System;
import de.svdragster.logica.system.SystemMessageDelivery;
import de.svdragster.logica.util.SystemNotifications.NotificationNewEntity;
import de.svdragster.logica.world.Engine;


public class Main extends Game {

    private static Main instance; //You Shall not PASS

    public static  Engine engine = Engine.getInstance();

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

        FontManager.init();

		// The first screen is the loading screen which will load all other screens
		ScreenManager.getInstance().getCurrentScreen().create();


        Gdx.input.setInputProcessor(new KeyListener());

        engine = Engine.getInstance();;
        engine.getSystemManager().addSystem(
                new SystemMessageDelivery(),
                new HexaSystemGeneralProducer(engine),
                new HexaSystemGeneralConsumer(engine)
        );


        HexVector vector = new HexVector(0, 1);
        java.lang.System.out.println("0: " + vector.toString());
        vector.rotate270();
        java.lang.System.out.println("270: " + vector.toString());
        vector.rotate90();
        java.lang.System.out.println("360: " + vector.toString());


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
