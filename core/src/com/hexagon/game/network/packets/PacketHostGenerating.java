package com.hexagon.game.network.packets;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketHostGenerating extends Packet {


    public PacketHostGenerating() {
        super(PacketType.HOST_GENERATING);
    }

    public PacketHostGenerating(UUID clientID) {
        super(PacketType.HOST_GENERATING, clientID);
    }

    @Override
    public String serialize() {
        return super.serialize();
    }
}
