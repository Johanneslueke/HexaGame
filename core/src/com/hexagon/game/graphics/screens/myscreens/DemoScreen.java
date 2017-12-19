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
        Logic.getSystemManager().addSystem(new HexaSystemCollision());
        Logic.getSystemManager().addSystem(new HexaSystemMovment(300,400));

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



        id = Logic.getEntityManager().getEntityContext().size();

        ComponentMailbox msg = null;
        if (id > 0)
            msg = (ComponentMailbox) Logic.getEntityManager().retrieveComponent(id, StdComponents.MESSAGE);
        if (msg != null && !msg.Inbox.isEmpty())
            msg.Inbox.poll().InvokeCallback();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            Random rn = new Random();
            id = Logic.getEntityManager().createEntity(
                    new ComponentPosition(rn.nextInt(400), rn.nextInt(300)),
                    new ComponentMovement(1 + rn.nextInt(9), 1 + rn.nextInt(9)),
                    new ComponentMailbox());

            SystemManager.getInstance().BroadcastMessage(id);
            java.lang.System.out.println(id);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            if (id > 0)
                Logic.getEntityManager().removeEntity(id);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            ComponentMailbox.Message SpeedUp = new ComponentMailbox.Message(id, id, "", new Delegate() {
                @Override
                public void invoke(Object... args) {
                    ComponentMovement speed = (ComponentMovement)Logic.getEntityManager() .retrieveComponent(id, StdComponents.MOVEMENT);
                    if (speed.dX < 0)
                        speed.dX -= 1.1;
                    else
                        speed.dX += 1.1;

                    if (speed.dY < 0)
                        speed.dY -= 1.1;
                    else
                        speed.dY += 1.1;
                }
            });

            SystemManager.getInstance().BroadcastMessage(SpeedUp);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            ComponentMailbox.Message SpeedUp = new ComponentMailbox.Message(id, id, "", new Delegate() {
                @Override
                public void invoke(Object... args) {
                    ComponentMovement speed = (ComponentMovement) Logic.getEntityManager().retrieveComponent(id, StdComponents.MOVEMENT);
                    if (speed.dX < 0)
                        speed.dX += 1.1;
                    else
                        speed.dX -= 1.1;

                    if (speed.dY < 0)
                        speed.dY += 1.1;
                    else
                        speed.dY -= 1.1;
                }
            });

            SystemManager.getInstance().BroadcastMessage(SpeedUp);
        }


        //Logic.run();
        ScreenManager.getInstance().clearScreen(0.2f, 0.25f, 0.35f);

        List<Component> entity = Logic.getComponentManager().getComponentList().get(StdComponents.POSITION);


        for (Component com : entity) {
            ComponentPosition pos = (ComponentPosition) com;
            //batch.draw(img, (int)pos.X, (int)pos.Y);
            render.begin(ShapeRenderer.ShapeType.Line);

            Random rn = new Random();
            render.setColor(rn.nextInt(255) / 2, rn.nextInt(255), rn.nextInt(255), 1);
            render.rect((float) pos.X, (float) pos.Y, 10, 10);

            render.flush();
            render.end();
        }



        System.out.println("FPS: " + Gdx.app.getGraphics().getFramesPerSecond()
                          +" delta: " + Gdx.graphics.getRawDeltaTime() + " \t"
                          + " Logic FPS: "+ (long)Logic.FramesPerSecond()+ " \t" + " FrameTime(ns): " + Logic.FrameTime()  + " \t"
                          + " FrameTime(ms) " + Logic.FrameTime() / Math.pow(10,6) + "\t"
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
