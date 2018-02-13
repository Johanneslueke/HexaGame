package com.hexagon.game.Logic.Systems;

import com.hexagon.game.Logic.HexaComponents;

import java.util.Observable;

import de.svdragster.logica.components.ComponentResource;
import de.svdragster.logica.components.meta.StdComponents;
import de.svdragster.logica.manager.Entity.Entity;
import de.svdragster.logica.system.SystemConsumerBase;
import de.svdragster.logica.util.Pair;
import de.svdragster.logica.util.SystemNotifications.NotificationNewEntity;
import de.svdragster.logica.util.SystemNotifications.NotificationRemoveEntity;

/**
 * Created by Johannes on 13.02.2018.
 */

public class HexaSystemGeneralConsumer extends SystemConsumerBase {
    @Override
    public void process(double delta) {
        for(Entity e : getLocalEntityCache()){
            //asking if the component is there, is a bit abitrary here because
            //we use the systems cache list of entities.
            Pair<Boolean,ComponentResource> res = e.hasAssociationWith(StdComponents.RESOURCE);
            if(res.getFirst()){
                if(this.isReady(res.getSecond())) {
                    ConsumesProducts(res.getSecond());
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
            if(e.isOfType(StdComponents.CONSUMER,StdComponents.RESOURCE, HexaComponents.OWNER)){
                this.getLocalEntityCache().add(e.Entity());
            }
        }
        if(o instanceof NotificationRemoveEntity)
        {
            NotificationRemoveEntity e = (NotificationRemoveEntity) o;
            if(e.isOfType(StdComponents.CONSUMER,StdComponents.RESOURCE,HexaComponents.OWNER)){
                this.getLocalEntityCache().remove(e.getEntity());
            }
        }
    }
}
