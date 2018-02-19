package com.hexagon.game.network.packets;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketKeepAlive extends Packet {

    private int sessionID;

    public PacketKeepAlive(PacketType type, int sessionID) {
        super(type);
        this.sessionID = sessionID;
    }

    public PacketKeepAlive(PacketType type, UUID clientID,int sessionID) {
        super(type, clientID);
        this.sessionID = sessionID;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }


}
