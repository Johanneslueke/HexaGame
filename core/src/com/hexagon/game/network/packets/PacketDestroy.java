package com.hexagon.game.network.packets;

import com.hexagon.game.map.Point;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketDestroy extends Packet {

    private Point arrayPosition;


    public PacketDestroy(Point arrayPosition) {
        super(PacketType.DESTROY);
        this.arrayPosition = arrayPosition;
    }

    public PacketDestroy(UUID clientID,Point arrayPosition) {
        super(PacketType.DESTROY, clientID);
        this.arrayPosition = arrayPosition;
    }

    public Point getArrayPosition() {
        return arrayPosition;
    }

    public void setArrayPosition(Point arrayPosition) {
        this.arrayPosition = arrayPosition;
    }

    @Override
    public String serialize() {
        return super.serialize() + arrayPosition.getX() + "," + arrayPosition.getY() + ";";
    }
}
