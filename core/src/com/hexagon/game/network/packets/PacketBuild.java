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
    private UUID            owner;


    public PacketBuild(Point arrayPosition, StructureType structureType, UUID owner) {
        super(PacketType.BUILD);
        this.arrayPosition = arrayPosition;
        this.structureType = structureType;
        this.owner = owner;
    }

    public PacketBuild(UUID clientID, Point arrayPosition, StructureType structureType, UUID owner) {
        super(PacketType.BUILD, clientID);
        this.arrayPosition = arrayPosition;
        this.structureType = structureType;
        this.owner = owner;
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

    public UUID getOwner() {
        return owner;
    }

    @Override
    public String serialize() {
        return super.serialize() + arrayPosition.getX() + "," + arrayPosition.getY() + ";"
                + structureType.name() + ";"
                + owner.toString() + ";";
    }
}
