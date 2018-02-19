package com.hexagon.game.network.packets;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

/**
 * This packet will be send by the client!!
 */
public class PacketLeave extends Packet {

    PacketLeave(PacketType type) {
        super(type);
    }

    PacketLeave(PacketType type, UUID clientID) {
        super(type, clientID);
    }
}
