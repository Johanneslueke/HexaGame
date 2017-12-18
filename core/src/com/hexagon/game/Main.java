package com.hexagon.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.svdragster.logica.system.SystemMessageDelivery;
import de.svdragster.logica.world.Engine;
import sun.rmi.runtime.Log;

public class Main extends Game {

	private Engine	Logic;

	
	@Override
	public void create () {
		Logic = new Engine();

		Logic.getSystemManager().addSystem(new SystemMessageDelivery(Logic.getEntityManager()));

	}

	@Override
	public void render () {
		/**/
		Logic.run();
		/**/
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	}
	
	@Override
	public void dispose () {
	}
}
