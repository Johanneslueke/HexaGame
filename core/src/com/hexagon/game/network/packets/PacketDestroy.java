package com.hexagon.game.network.packets;

import com.hexagon.game.map.Point;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketDestroy extends Packet {

    private Point   PositionOfStructure;


    PacketDestroy(PacketType type, Point positionOfStructure) {
        super(type);
        this.PositionOfStructure = positionOfStructure;
    }

    PacketDestroy(PacketType type, UUID clientID,Point positionOfStructure ) {
        super(type, clientID);
        this.PositionOfStructure = positionOfStructure;
    }

    public Point getPositionOfStructure() {
        return PositionOfStructure;
    }

    public void setPositionOfStructure(Point positionOfStructure) {
        PositionOfStructure = positionOfStructure;
    }
}
