package com.hexagon.game.graphics.screens.myscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hexagon.game.Logic.Components.Systems.HexaSystemMovment;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;

import java.util.Random;

import de.svdragster.logica.components.ComponentMailbox;
import de.svdragster.logica.components.ComponentMovement;
import de.svdragster.logica.components.ComponentPosition;
import de.svdragster.logica.components.StdComponents;
import de.svdragster.logica.system.SystemMessageDelivery;
import de.svdragster.logica.util.Delegate;
import de.svdragster.logica.world.Engine;

/**
 * Created by z003pksw on 18.12.2017.
 */

public class DemoScreen extends HexagonScreen {

    private Engine Logic;

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
        Logic.getSystemManager().addSystem(new HexaSystemMovment(300,400));

        Logic.getEntityManager().createEntity(new ComponentMovement(2,2),new ComponentPosition(0,0));

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        id = Logic.getEntityManager().getEntityContext().size();

        ComponentMailbox msg = null;
        if(id > 0)
            msg = (ComponentMailbox) Logic.getEntityManager().retrieveComponent(id,StdComponents.MESSAGE);
        if(msg != null && !msg.Inbox.isEmpty())
            msg.Inbox.poll().InvokeCallback();




        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            Random rn = new Random();
            id = Logic.getEntityManager().createEntity(
                    new ComponentPosition(rn.nextInt(400),rn.nextInt(300)),
                    new ComponentMovement(1+rn.nextInt(9),1+rn.nextInt(9)),
                    new ComponentMailbox());

            Logic.getSystemManager().BroadcastMessage(id);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            if(id > 0)
                Logic.getEntityManager().removeEntity(id);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
        {
            ComponentMailbox.Message  SpeedUp = new ComponentMailbox.Message(id, id, "", new Delegate()
            {
                @Override
                public void invoke(Object... args) {
                    ComponentMovement speed = (ComponentMovement) Logic.getEntityManager().retrieveComponent(id,StdComponents.MOVEMENT);
                    if(speed.dX < 0)
                        speed.dX -= 1.1;
                    else
                        speed.dX += 1.1;

                    if(speed.dY < 0)
                        speed.dY -= 1.1;
                    else
                        speed.dY += 1.1;
                }
            });

            Logic.getSystemManager().BroadcastMessage(SpeedUp);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
        {
            ComponentMailbox.Message  SpeedUp = new ComponentMailbox.Message(id, id, "", new Delegate()
            {
                @Override
                public void invoke(Object... args) {
                    ComponentMovement speed = (ComponentMovement) Logic.getEntityManager().retrieveComponent(id,StdComponents.MOVEMENT);
                    if(speed.dX < 0)
                        speed.dX += 1.1;
                    else
                        speed.dX -= 1.1;

                    if(speed.dY < 0)
                        speed.dY += 1.1;
                    else
                        speed.dY -= 1.1;
                }
            });

            Logic.getSystemManager().BroadcastMessage(SpeedUp);
        }


        Logic.run();
        ScreenManager.getInstance().clearScreen(0.2f, 0.25f, 0.35f);

        for(int entity : Logic.getEntityManager().getEntityContext().keySet()){


            ComponentPosition pos = (ComponentPosition) Logic.getEntityManager().retrieveComponent(entity, StdComponents.POSITION);
            //batch.draw(img, (int)pos.X, (int)pos.Y);
            render.begin(ShapeRenderer.ShapeType.Line);

            Random rn = new Random();
            render.setColor(rn.nextInt(255)/2, rn.nextInt(255), rn.nextInt(255), 1);
            render.rect((float)pos.X, (float)pos.Y,10,10);

            render.flush();
            render.end();
        }

        System.out.println("FPS: "+Gdx.app.getGraphics().getFramesPerSecond()+ " Entity Count: " + id);



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

    }
}
