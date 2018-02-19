package com.hexagon.game.network.packets;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public abstract class Packet {

    private PacketType      type;
    private boolean         isCancled;
    /**
     * Issued by the server instance which hosts the game
     */
    private UUID            globalClientID;



     Packet(PacketType type){
        this.type = type;
        this.isCancled = false;
        this.globalClientID = UUID.randomUUID();
    }

     Packet(PacketType type, UUID clientID){
        this.type = type;
        this.isCancled = false;
        this.globalClientID = clientID;
    }



}
