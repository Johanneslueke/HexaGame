package com.hexagon.game.network.packets;

/**
 * Created by Johannes on 19.02.2018.
 */

public enum PacketType {

    KEEPALIVE((byte) 0x00),
    REGISTER((byte) 0x01),
    JOIN((byte) 0x02),
    LEAVE((byte) 0x03),
    BUILD((byte) 0x04),
    DESTROY((byte) 0x04),
    TRADE((byte) 0x05),
    MAPUPDATE((byte) 0x06),
    TERMINATE((byte) 0x07);

    public byte ID;

    PacketType(byte id){
        this.ID = id;
    }

    public static PacketType valueOf(byte b) {
        for (int i=0; i<values().length; i++) {
            if (values()[i].ID == b) {
                return values()[i];
            }
        }
        return null;
    }


}
