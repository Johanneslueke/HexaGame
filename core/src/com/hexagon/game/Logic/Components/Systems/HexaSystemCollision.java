package com.hexagon.game.Logic.Components.Systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import de.svdragster.logica.components.ComponentMailbox;
import de.svdragster.logica.components.ComponentMovement;
import de.svdragster.logica.components.ComponentPosition;
import de.svdragster.logica.components.StdComponents;
import de.svdragster.logica.system.System;
import de.svdragster.logica.util.Delegate;
import de.svdragster.logica.world.Engine;

/**
 * Created by Johannes Lüke on 19.12.2017.
 */

public class HexaSystemCollision extends System {


    private List<Integer> idCache = new ArrayList<>();


    public HexaSystemCollision(){
        super.setGlobalEntityContext(Engine.getInstance().getEntityManager());
        super.subscribe();


    }
    @Override
    public void process(double v) {
        for(int entity : idCache){
            synchronized (this){
                ComponentPosition position1 = (ComponentPosition) getGlobalEntityContext().retrieveComponent(entity, StdComponents.POSITION);
                ComponentMovement movment1 = (ComponentMovement) getGlobalEntityContext().retrieveComponent(entity, StdComponents.MOVEMENT);
                ComponentMailbox  MailBox1 = (ComponentMailbox) getGlobalEntityContext().retrieveComponent(entity,StdComponents.MESSAGE);

                if(getGlobalEntityContext().isEntityAlive(entity)){
                    for(int entity2 : idCache){
                        if(entity != entity2){
                            ComponentPosition position2 = (ComponentPosition) getGlobalEntityContext().retrieveComponent(entity2, StdComponents.POSITION);
                            ComponentMovement movment2 = (ComponentMovement) getGlobalEntityContext().retrieveComponent(entity, StdComponents.MOVEMENT);
                            if(        position1.X <  position2.X + 10
                                    && position1.X + 10 > position2.X
                                    && position1.Y < position2.Y + 10
                                    && position1.Y + 10 > position2.Y){
                                movment1.dX *= -1;
                                movment1.dY *= -1;



                                position1.X = position1.lastX -5;
                                position1.Y = position1.lastY -5 ;


                                //java.lang.System.out.println("COLLISION DETECTED");
                            }
                        }



                    }
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
