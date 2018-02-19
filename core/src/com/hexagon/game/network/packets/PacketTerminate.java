package com.hexagon.game.network.packets;

/**
 * Created by Johannes on 19.02.2018.
 */

import java.util.UUID;

/**
 * This packet will be send by the Server!!
 */
public class PacketTerminate extends Packet {


    PacketTerminate(PacketType type) {
        super(type);
    }

    PacketTerminate(PacketType type, UUID clientID) {
        super(type, clientID);
    }
}
