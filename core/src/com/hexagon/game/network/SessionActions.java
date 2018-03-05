package com.hexagon.game.network;

import com.badlogic.gdx.graphics.Color;
import com.hexagon.game.map.structures.StructureType;

import java.util.UUID;

/**
 * Created by Johannes on 02.03.2018.
 */

public interface SessionActions {

     void addNewPlayer(UUID playerID, String Name, Color color);
     void removePlayer(UUID playerID);

     void buildStructure(UUID playerID, StructureType type);


}
