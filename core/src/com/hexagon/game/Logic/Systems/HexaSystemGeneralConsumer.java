package com.hexagon.game.Logic.Systems;

import com.hexagon.game.Logic.Components.HexaComponentOwner;
import com.hexagon.game.Logic.HexaComponents;

import java.util.Observable;

import de.svdragster.logica.components.ComponentResource;
import de.svdragster.logica.components.meta.StdComponents;
import de.svdragster.logica.manager.Entity.Entity;
import de.svdragster.logica.system.SystemConsumerBase;
import de.svdragster.logica.util.Pair;
import de.svdragster.logica.util.SystemNotifications.NotificationNewEntity;
import de.svdragster.logica.util.SystemNotifications.NotificationRemoveEntity;
import de.svdragster.logica.world.Engine;

/**
 * Created by Johannes on 13.02.2018.
 */

/**
 * Any Consumer system is defined by an entity which have the
 * component "Consumer" and "Resource" associated with it.
 *
 * Where "Consumer" simple marks it as an entity removing
 * system and "Resource" defines the actual resource which shall
 * be removed, in which rate and amount.
 *
 * An entity defined in Resource will only be removed if the period
 * of the resource has been reached. this is done by simply accumalate
 * the rate in which the entity shall be removed over time:
 *
 *      Resource.productProgress += Resource.productRate;
 *
 * To define a class of entities you have the field productType in
 * the "Resource" component which is a simple list containing a set
 * of components entities have to match so you are able to identify
 * possible candidates for removal
 */
public class HexaSystemGeneralConsumer extends SystemConsumerBase {

    /**
     * Annoying boilerplate
     * @param engine
     */
    public HexaSystemGeneralConsumer(Engine engine){
        setGlobalEntityContext(engine.getEntityManager());
    }


    @Override
    public void process(double delta) {
        //Iterate through the local logic systems
        for(Entity e : getLocalEntityCache()){
            //asking if the component is there, is a bit abitrary here because
            //we use the systems cache list of entities.
            Pair<Boolean,ComponentResource> res = e.hasAssociationWith(StdComponents.RESOURCE);
            Pair<Boolean,HexaComponentOwner> owner = e.hasAssociationWith(HexaComponents.OWNER);

            if(res.getFirst() && owner.getFirst()){
                if(this.isReady(res.getSecond())) {
                    ConsumesProducts(res.getSecond(), owner.getSecond());
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
            if(e.isOfType(StdComponents.CONSUMER,StdComponents.RESOURCE)){
                this.getLocalEntityCache().add(e.getEntity());
            }

            if(e.isOfType(HexaComponents.OWNER))
                System.out.println(e.getEntity());

        }
        if(o instanceof NotificationRemoveEntity)
        {
            NotificationRemoveEntity e = (NotificationRemoveEntity) o;
            if(e.getEntity().hasAnyAssociations())
                if(e.isOfType(StdComponents.CONSUMER,StdComponents.RESOURCE)){
                    this.getLocalEntityCache().remove(e.getEntity());
                }
        }
    }
}
