package com.hexagon.game.network.packets;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketRegister extends Packet {

    private String roomName;
    private boolean success;

    public PacketRegister(String roomName, boolean cancelled) {
        super(PacketType.REGISTER);
        roomName = roomName.replace(";", "");
        this.roomName = roomName;
        setCancelled(cancelled);
    }

    public PacketRegister(UUID clientID, String roomName, boolean cancelled) {
        super(PacketType.REGISTER, clientID);
        roomName = roomName.replace(";", "");
        this.roomName = roomName;
        setCancelled(cancelled);
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public String serialize() {
        return super.serialize() + roomName + ";";
    }
}
