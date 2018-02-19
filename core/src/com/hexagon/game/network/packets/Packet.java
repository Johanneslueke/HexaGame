package com.hexagon.game.network.packets;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public abstract class Packet {

    private PacketType      type;
    private boolean         isCancled;
    private UUID            ClientID;



     Packet(PacketType type){
        this.type = type;
        this.isCancled = false;
        this.ClientID = UUID.randomUUID();
    }

     Packet(PacketType type, UUID clientID){
        this.type = type;
        this.isCancled = false;
        this.ClientID = clientID;
    }



}
