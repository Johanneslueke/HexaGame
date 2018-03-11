package com.hexagon.game.network.packets;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketKeepAlive extends Packet {

    private int sessionID;

    public PacketKeepAlive(int sessionID) {
        super(PacketType.KEEPALIVE);
        this.sessionID = sessionID;
    }

    public PacketKeepAlive(UUID clientID, int sessionID) {
        super(PacketType.KEEPALIVE, clientID);
        this.sessionID = sessionID;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    @Override
    public String serialize() {
        return super.serialize() + sessionID + ";";
    }
}
