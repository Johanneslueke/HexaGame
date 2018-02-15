package com.hexagon.game;

import com.badlogic.gdx.Game;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.ui.WindowManager;
import com.hexagon.game.input.HexMultiplexer;
import com.hexagon.game.input.InputManager;
import com.hexagon.game.input.KeyListener;
import com.hexagon.game.map.MapManager;
import com.hexagon.game.util.HexVector;
import com.hexagon.game.util.MenuUtil;

import java.util.Observable;

import de.svdragster.logica.system.System;
import de.svdragster.logica.system.SystemMessageDelivery;
import de.svdragster.logica.util.SystemNotifications.NotificationNewEntity;
import de.svdragster.logica.world.Engine;

public class Main extends Game {

    private static Main instance;

    public static Engine engine;

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

        // Init Engine
        engine = new Engine();

        engine.getSystemManager().addSystem(new SystemMessageDelivery());
        engine.getSystemManager().addSystem(new System() {

            @Override
            public void process(double v) {
                // unused
            }

            @Override
            public void update(Observable observable, Object o) {
                java.lang.System.out.println("Update");
                if (o instanceof NotificationNewEntity) {
                    java.lang.System.out.println("Hello");
                }
                if (o instanceof Integer) {
                    engine.getEntityManager().createID();
                }
            }

        });

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
