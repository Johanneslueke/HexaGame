package com.hexagon.game.network.packets;

/**
 * Created by Johannes on 19.02.2018.
 */

import java.util.UUID;

/**
 * This packet will be sent by the client!!
 */
public class PacketLeave extends Packet {

    PacketLeave(PacketType type) {
        super(type);
    }

    public PacketLeave() {
        super(PacketType.LEAVE);
    }

    public PacketLeave(UUID leaverUuid) {
        super(PacketType.LEAVE, leaverUuid);
    }
}
