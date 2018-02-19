package com.hexagon.game.network.packets;

import com.hexagon.game.map.Point;
import com.hexagon.game.map.structures.Structure;
import com.hexagon.game.map.structures.StructureType;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketBuild extends Packet {

    private Point           PositionOnMap;
    private StructureType   Structure;


    PacketBuild(PacketType type, Point positionOnMap,StructureType structure) {
        super(type);
        this.PositionOnMap = positionOnMap;
        this.Structure = structure;
    }

    PacketBuild(PacketType type, UUID clientID, Point positionOnMap,StructureType structure) {
        super(type, clientID);
    }

    public Point getPositionOnMap() {
        return PositionOnMap;
    }

    public void setPositionOnMap(Point positionOnMap) {
        PositionOnMap = positionOnMap;
    }
}
