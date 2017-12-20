package com.hexagon.game.graphics.screens.myscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hexagon.game.Logic.Components.Systems.HexaSystemCollision;
import com.hexagon.game.Logic.Components.Systems.HexaSystemMovment;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;

import java.util.List;
import java.util.Random;

import de.svdragster.logica.components.Component;
import de.svdragster.logica.components.ComponentMailbox;
import de.svdragster.logica.components.ComponentMovement;
import de.svdragster.logica.components.ComponentPosition;
import de.svdragster.logica.components.StdComponents;
import de.svdragster.logica.system.SystemManager;
import de.svdragster.logica.system.SystemMessageDelivery;
import de.svdragster.logica.util.Delegate;
import de.svdragster.logica.world.Engine;

/**
 * Created by Johannes Lüke on 18.12.2017.
 */

public class DemoScreen extends HexagonScreen {

    private static Engine Logic;

    private Thread LogicThread;
    private ShapeRenderer render;

    private int id = -1;

    public DemoScreen() {
        super(ScreenType.DEMOJoJo);

    }

    @Override
    public void create() {
        render = new ShapeRenderer();
        Logic = new Engine();

        Logic.getSystemManager().addSystem(new SystemMessageDelivery(Logic.getEntityManager()));

        LogicThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while(true)
                {

                    Logic.run(Gdx.graphics.getRawDeltaTime());
                }
            }
        });

        LogicThread.start();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        try {
           // if(!LogicThread.isAlive())

            //LogicThread.interrupt();
        }catch (IllegalThreadStateException e){
            //e.printStackTrace();
        }





        System.out.println("FPS: " + Gdx.app.getGraphics().getFramesPerSecond()
                          +" delta: " + Gdx.graphics.getRawDeltaTime() + " \t"
                          + " Logic FPS: "+ (long) Engine.FramesPerSecond()+ " \t" + " FrameTime(ns): " + Engine.FrameTime()  + " \t"
                          + " FrameTime(ms) " + Engine.FrameTime() / Math.pow(10,6) + "\t"
                          +" Entity Count: " + Logic.getEntityManager().getEntityContext().size());

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
        try {
            LogicThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
