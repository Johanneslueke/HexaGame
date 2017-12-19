package com.hexagon.game.Logic.Components.Systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import de.svdragster.logica.components.ComponentMovement;
import de.svdragster.logica.components.ComponentPosition;
import de.svdragster.logica.components.StdComponents;
import de.svdragster.logica.system.System;
import de.svdragster.logica.world.Engine;

/**
 * Created by z003pksw on 18.12.2017.
 */

public class HexaSystemMovment extends System {

    private List<Integer> idCache;
    private int width,height;

    public HexaSystemMovment(int width,int height) {
        super.setGlobalEntityContext(Engine.getInstance().getEntityManager());
        super.subscribe();

        this.width = width;
        this.height = height;
        idCache = new ArrayList<>();

        //Caching already existing entities in locally
        for( int id : getGlobalEntityContext().getEntityContext().keySet()){
            if(getGlobalEntityContext().hasComponents(id, StdComponents.POSITION,StdComponents.MOVEMENT))
                idCache.add(id);
        }
    }

    @Override
    public void process(double delta) {

        for(int entity : idCache){

            synchronized (this){
                if(getGlobalEntityContext().isEntityAlive(entity)){
                    ComponentPosition position = (ComponentPosition) getGlobalEntityContext().retrieveComponent(entity,StdComponents.POSITION);
                    ComponentMovement movement = (ComponentMovement) getGlobalEntityContext().retrieveComponent(entity,StdComponents.MOVEMENT);

                    if(position.X < 0)
                        movement.dX *= -1;
                    if(position.X > width)
                        movement.dX *= -1;
                    if(position.Y < 0)
                        movement.dY *= -1;
                    if(position.Y > height)
                        movement.dY *= -1;

                    position.lastX = position.X;
                    position.lastY = position.Y;
                    position.X += movement.dX * delta;
                    position.Y += movement.dY * delta;
                 }
            }

        }

    }

    @Override
    public void update(Observable observable, Object o) {
        if( o instanceof Integer){
            int entity = (Integer)o;

            if( getGlobalEntityContext().hasComponents(entity,StdComponents.POSITION,StdComponents.MOVEMENT))
                idCache.add(entity);
        }
    }
}
