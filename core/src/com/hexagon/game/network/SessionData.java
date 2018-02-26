package com.hexagon.game.network;

import com.hexagon.game.Logic.Components.HexaComponentOwner;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import de.svdragster.logica.manager.Entity.Entity;
import de.svdragster.logica.world.Engine;

/**
 * Created by Johannes on 19.02.2018.
 */

public class SessionData {
    Map<UUID,Entity>        PlayerList = new Hashtable<>();

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

}
