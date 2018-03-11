package com.hexagon.game.network.packets;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketPlayerLoaded extends Packet {


    public PacketPlayerLoaded() {
        super(PacketType.PLAYER_LOADED);
    }

    public PacketPlayerLoaded(UUID clientID) {
        super(PacketType.PLAYER_LOADED, clientID);
    }

    @Override
    public String serialize() {
        return super.serialize();
    }
}
