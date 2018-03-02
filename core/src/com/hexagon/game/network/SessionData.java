package com.hexagon.game.network;

import com.hexagon.game.Logic.Components.HexaComponentOwner;
import com.hexagon.game.Logic.HexaComponents;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.structures.StructureType;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.svdragster.logica.components.Component;
import de.svdragster.logica.components.meta.ComponentType;
import de.svdragster.logica.components.meta.StdComponents;
import de.svdragster.logica.manager.Entity.Entity;
import de.svdragster.logica.util.Pair;
import de.svdragster.logica.util.SystemNotifications.NotificationRemoveEntity;
import de.svdragster.logica.world.Engine;

/**
 * Created by Johannes on 19.02.2018.
 */

public class SessionData implements SessionActions {
    Map<UUID,Entity>        PlayerList = new Hashtable<>();

    public HexMap   currentMap(){
        return GameManager.instance.getGame().getCurrentMap();
    }

    public void addNewPlayer(UUID playerID,String Name){

        if(!PlayerList.containsKey(playerID)){
            PlayerList.put(
                    playerID,
                    Engine.getInstance().getEntityManager()
                            .createID(
                                    new HexaComponentOwner(Name)
                            ));
        }else
            throw new RuntimeException("User already in Game");
    }

    public void removePlayer(UUID   player){
        if(!PlayerList.containsKey(player)){
            Engine.getInstance().BroadcastMessage(
                    new NotificationRemoveEntity(PlayerList.get(player))
            );
            PlayerList.remove(player);
        }else{
            throw new RuntimeException("User does not exist and therefor can not be removed");
        }
    }

    public void buildStructure(UUID playerID, StructureType type){

        if(!PlayerList.containsKey(playerID)){
            switch (type){
                case ORE:{

                }break;
            }
            Engine.getInstance().BroadcastMessage(
                    new NotificationRemoveEntity(PlayerList.get(playerID))
            );
        }else{
            throw new RuntimeException("User does not exist and therefor can not be removed");
        }
    }

    public Map<String,Integer> getPlayerResourceStatus(UUID playerID){
        if(!PlayerList.containsKey(playerID)){
            Map<String,Integer> result = new Hashtable<>();
            List<List<Component>> Components = Engine.getInstance().getComponentManager().groupByTypes(HexaComponents.ORE);

            for(List<Component> Resource : Components){
               for(Component c :  Resource){
                   Entity player = c.getBackAssociation();

                   Pair<Boolean,HexaComponentOwner> Owner = player.hasAssociationWith(HexaComponents.OWNER);
                   if(Owner.getFirst() && (Owner.getSecond().getID() == playerID)){
                       switch ((HexaComponents)c.getType()){
                           case ORE:{
                                result.put("Ore",result.get("Ore")+1);
                           }break;
                       }
                   }
               }
            }
            return result;
        }

        return null;
    }

}
