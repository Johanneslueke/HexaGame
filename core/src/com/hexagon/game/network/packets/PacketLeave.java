package com.hexagon.game.network.packets;

/**
 * Created by Johannes on 19.02.2018.
 */

import java.util.UUID;

/**
 * This packet will be sent by the client!!
 */
public class PacketLeave extends Packet {

    private UUID leaverUuid;
    private boolean kick = false;

    public PacketLeave(UUID leaverUuid, boolean kick) {
        super(PacketType.LEAVE);
        this.leaverUuid = leaverUuid;
        this.kick = kick;
    }

    public PacketLeave(UUID senderId, UUID leaverUuid, boolean kick) {
        super(PacketType.LEAVE, senderId);
        this.leaverUuid = leaverUuid;
        this.kick = kick;
    }

    public UUID getLeaverUuid() {
        return leaverUuid;
    }

    public void setLeaverUuid(UUID leaverUuid) {
        this.leaverUuid = leaverUuid;
    }

    public boolean isKick() {
        return kick;
    }

    @Override
    public String serialize() {
        return super.serialize()
                + leaverUuid.toString() + ";"
                + kick + ";";
    }
}
