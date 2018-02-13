package com.hexagon.game.Logic.Systems;

import com.hexagon.game.Logic.Components.HexaComponentOwner;
import com.hexagon.game.Logic.HexaComponents;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import de.svdragster.logica.components.Component;
import de.svdragster.logica.components.ComponentResource;
import de.svdragster.logica.components.meta.StdComponents;
import de.svdragster.logica.manager.Entity.Entity;
import de.svdragster.logica.system.System;
import de.svdragster.logica.system.SystemProducerBase;
import de.svdragster.logica.util.Pair;
import de.svdragster.logica.util.SystemNotifications.NotificationNewEntity;
import de.svdragster.logica.util.SystemNotifications.NotificationRemoveEntity;

/**
 * Created by Johannes on 13.02.2018.
 */

public class HexaSystemGeneralProducer extends SystemProducerBase {

    @Override
    public void process(double delta) {
        for(Entity e : getLocalEntityCache()){
            //asking if the component is there, is a bit abitrary here because
            //we use the systems cache list of entities.
            Pair<Boolean,ComponentResource> res = e.hasAssociationWith(StdComponents.RESOURCE);
            if(res.getFirst()){
                if(this.isReady(res.getSecond())) {
                    EmitProducts(res.getSecond(),new Component[]{(Component) e.hasAssociationWith(HexaComponents.OWNER).getSecond()});
                    resetProductionProgress(res.getSecond());
                }
                advanceProgress(res.getSecond(), (float) delta);
            }
        }
    }

    @Override
    public void update(Observable observable, Object o) {

        if(o instanceof NotificationNewEntity)
        {
            NotificationNewEntity e = (NotificationNewEntity) o;
            if(e.isOfType(StdComponents.PRODUCER,StdComponents.RESOURCE, HexaComponents.OWNER)){
                this.getLocalEntityCache().add(e.Entity());
            }
        }
        if(o instanceof NotificationRemoveEntity)
        {
            NotificationRemoveEntity e = (NotificationRemoveEntity) o;
            if(e.isOfType(StdComponents.PRODUCER,StdComponents.RESOURCE)){
                this.getLocalEntityCache().remove(e.getEntity());
            }
        }
    }
}
