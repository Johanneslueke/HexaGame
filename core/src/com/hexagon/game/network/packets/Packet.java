package com.hexagon.game.network.packets;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public abstract class Packet {

    private PacketType      type;
    private boolean         cancelled;
    /**
     * Issued by the server instance which hosts the game
     */
    private UUID            globalClientID;



     Packet(PacketType type){
        this.type = type;
        this.cancelled = false;
        this.globalClientID = UUID.randomUUID();
    }

     Packet(PacketType type, UUID clientID){
        this.type = type;
        this.cancelled = false;
        this.globalClientID = clientID;
    }


    public PacketType getType() {
        return type;
    }

    public void setType(PacketType type) {
        this.type = type;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public UUID getGlobalClientID() {
        return globalClientID;
    }

    public void setGlobalClientID(UUID globalClientID) {
        this.globalClientID = globalClientID;
    }

    public String serialize() {
        return type.ID + ";"
                + globalClientID.toString() + ";"
                + (cancelled ? 1 : 0) + ";";
    }

     public static Packet deserialize(String string) {
         String[] arr = string.split(";");
         byte typeId = Byte.parseByte(arr[0]);
         PacketType packetType = PacketType.valueOf(typeId);
         if (packetType == null) {
             return null;
         }

         UUID clientId = UUID.fromString(arr[1]);

         if (packetType == PacketType.REGISTER) {

             return new PacketRegister(clientId, arr[2]);
         }

         boolean cancelled = arr[2].equals("1");

         int offset = 3;

         switch (packetType) {
             case KEEPALIVE:
                 int sessionId = Integer.parseInt(arr[offset]);
                 return new PacketKeepAlive(clientId, sessionId);
         }

         return null;
     }
}
