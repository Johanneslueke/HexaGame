package com.hexagon.game.graphics.screens.myscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hexagon.game.Logic.Systems.InterfaceSystem;
import com.hexagon.game.Logic.Systems.TestSystem;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenType;


import de.svdragster.logica.components.Component;
import de.svdragster.logica.components.ComponentConsumer;
import de.svdragster.logica.components.ComponentConverter;
import de.svdragster.logica.components.ComponentExchanger;
import de.svdragster.logica.components.ComponentMailbox;
import de.svdragster.logica.components.ComponentProducer;
import de.svdragster.logica.components.ComponentResource;
import de.svdragster.logica.components.meta.ComponentType;
import de.svdragster.logica.manager.Entity.Entity;
import de.svdragster.logica.system.SystemMessageDelivery;
import de.svdragster.logica.world.Engine;

/**
 * Created by Johannes Lüke on 18.12.2017.
 */

public class DemoScreen extends HexagonScreen {

    private static Engine Logic;

    private Thread LogicThread;
    private ShapeRenderer render;

    private Entity id ;

    public DemoScreen() {
        super(ScreenType.DEMOJoJo);
        if(Logic == null)
            Logic = new Engine();
    }

    @Override
    public void create() {
        render = new ShapeRenderer();
        if(Logic == null)
            Logic = new Engine();

        Logic.getSystemManager().addSystem(new SystemMessageDelivery());
        Logic.getSystemManager().addSystem(new InterfaceSystem());
        Logic.getSystemManager().addSystem(new TestSystem());

        id = Logic.getEntityManager().createID(new ComponentMailbox());

        /*
        //Mine
        Logic.getEntityManager().createID(
                new ComponentResource(1,2,3,4),
                new ComponentProducer(),
                new ComponentPosition(TileNummer),
                new ComponentOwner(EntityOfPlayer),
                new ComponentMeta("EisenMine")
        );

        //Schmiede
        Logic.getEntityManager().createID(
                new ComponentResource(1,2,3,4),
                new ComponentConverter(ComponentType X, ComponentType Y, ....),
                new ComponentPosition(TileNummer),
                new ComponentOwner(EntityOfPlayer),
                new ComponentMeta("EisenMine")
        );

        //Trader
        Logic.getEntityManager().createID(
                new ComponentExchanger(EntityFromPlayer, EntityToPlayer),
                new ComponentDeal(ComponentType From, ComponentType To) ,
                new ComponentPosition(TileNummer),
                new ComponentOwner(EntityOfPlayer)
        );

        //City
        Logic.getEntityManager().createID(
                new ComponentConsumer(),
                new ComponentPopulus(),
                new ComponentStorage(),
                new ComponentModifier(x,y),
                new ComponentModifier(z,w),
                new ComponentPosition(TileNummer),
                new ComponentOwner(EntityOfPlayer)
        );

        Logic.BroadcastMessage(new Message{

            Entity getEntity(){

                return Logic.getEntityManager().createID(
                        new ComponentExchanger(EntityFromPlayer, EntityToPlayer),
                        new ComponentDeal(ComponentType From, ComponentType To),
                )
            }

        });

*/
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        try {
           // if(!LogicThread.isAlive())

            Entity t = Logic.getEntityManager().createID(new ComponentMailbox());


            Logic.getSystemManager().BroadcastMessage(new ComponentMailbox.Message(id,t,"test",null));

            //LogicThread.interrupt();
        }catch (IllegalThreadStateException e){
            //e.printStackTrace();
        }

        Logic.run(Gdx.graphics.getRawDeltaTime());



        /*System.out.println("FPS: " + Gdx.app.getGraphics().getFramesPerSecond()
                          +" delta: " + Gdx.graphics.getRawDeltaTime() + " \t"
                          + " Logic FPS: "+ (long) Engine.FramesPerSecond()+ " \t" + " FrameTime(ns): " + Engine.FrameTime()  + " \t"
                          + " FrameTime(ms) " + Engine.FrameTime() / Math.pow(10,6) + "\t"
                          +" Entity Count: " + Logic.getEntityManager().getEntityContext().size());*/

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
