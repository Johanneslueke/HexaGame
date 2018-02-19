package com.hexagon.game.network.packets;

/**
 * Created by Johannes on 19.02.2018.
 */

public enum PacketType {

    KEEPALIVE((byte)(1<<0)),
    JOIN((byte)(1<<1)),
    LEAVE((byte)(1<<2)),
    BUILD((byte)(1<<3)),
    DESTROY((byte)(1<<4)),
    TRADE((byte)(1<<5)),
    MAPUPDATE((byte)(1<<6)),
    TERMINATE((byte) (1<<7));

    public byte ID;

    PacketType(byte id){
        this.ID = id;
    }


}
