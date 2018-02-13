package com.hexagon.game.Logic.Systems;

import java.util.Observable;

import de.svdragster.logica.components.Component;
import de.svdragster.logica.components.meta.ComponentType;
import de.svdragster.logica.system.System;
import de.svdragster.logica.util.SystemNotifications.NotificationNewEntity;
import de.svdragster.logica.world.Engine;

/**
 * Created by Johannes on 12.02.2018.
 */

public class InterfaceSystem extends System {

    public InterfaceSystem(){
        subscribe();
        setGlobalEntityContext(null);
    }

    public static class Message{

        public String           requiredSystem;
        Message(){

        }

    }

    @Override
    public void process(double delta) {
        //NOT USED
    }

    @Override
    public void update(Observable observable, Object o) {
        //naive implementation
        if(o instanceof NotificationNewEntity)
        {
            NotificationNewEntity n = (NotificationNewEntity)o;


        }
        if(o instanceof Message){
            Message Data = (Message)o;
            if(Data.requiredSystem != null){
                String[] classes = Data.requiredSystem.split(";");
                for(String ClassName : classes){
                    try {
                        Object instance = Class.forName(ClassName).newInstance();
                        if(instance instanceof System)
                            Engine.getInstance().getSystemManager().addSystem((System)instance);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }

            Engine.getInstance().BroadcastMessage(o);
        }


    }
}
