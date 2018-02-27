package com.hexagon.game.network.packets;

import com.hexagon.game.map.Point;
import com.hexagon.game.map.structures.StructureType;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketBuild extends Packet{

    private Point           arrayPosition;
    private StructureType   structureType;


    public PacketBuild(PacketType type, Point arrayPosition, StructureType structureType) {
        super(type);
        this.arrayPosition = arrayPosition;
        this.structureType = structureType;
    }

    public PacketBuild(PacketType type, UUID clientID, Point arrayPosition, StructureType structureType) {
        super(type, clientID);
    }

    public Point getArrayPosition() {
        return arrayPosition;
    }

    public void setArrayPosition(Point arrayPosition) {
        this.arrayPosition = arrayPosition;
    }

    public StructureType getStructureType() {
        return structureType;
    }

    public void setStructureType(StructureType structureType) {
        this.structureType = structureType;
    }
}
