package com.hexagon.game.network.packets;

import java.util.UUID;

/**
 * Created by Johannes on 02.03.2018.
 */

public class PacketPlayerStatus extends Packet {


    private UUID PlayerID;

    PacketPlayerStatus(PacketType type) {
        super(type);
    }

    PacketPlayerStatus(PacketType type, UUID senderId) {
        super(type, senderId);
    }
}
