package com.hexagon.game.network.packets;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketMapUpdate extends Packet {

    /**
     * JSON-String
     */
    private String  RawMapData;

    public PacketMapUpdate(PacketType type,String rawMapData) {
        super(type);
        this.RawMapData = rawMapData;
    }

    public PacketMapUpdate(PacketType type, UUID clientID,String rawMapData) {
        super(type, clientID);
        this.RawMapData = rawMapData;
    }
}
