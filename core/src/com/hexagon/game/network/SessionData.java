package com.hexagon.game.network;

import com.hexagon.game.Logic.Components.HexaComponentOwner;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.structures.StructureType;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import de.svdragster.logica.manager.Entity.Entity;
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

}
