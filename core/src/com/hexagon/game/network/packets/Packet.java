package com.hexagon.game.network.packets;

import com.hexagon.game.network.HexaServer;

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
    private UUID senderId;



     Packet(PacketType type){
        this.type = type;
        this.cancelled = false;
        this.senderId = HexaServer.senderId;
    }

     Packet(PacketType type, UUID senderId){
        this.type = type;
        this.cancelled = false;
        this.senderId = senderId;
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

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public String serialize() {
        return type.ID + ";"
                + senderId.toString() + ";"
                + (cancelled ? 1 : 0) + ";";
    }

     public static Packet deserialize(String string) {
         String[] arr = string.split(";");
         byte typeId = Byte.parseByte(arr[0]);
         PacketType packetType = PacketType.valueOf(typeId);
         if (packetType == null) {
             return null;
         }

         UUID senderId = UUID.fromString(arr[1]);

         if (packetType == PacketType.REGISTER) {

             return new PacketRegister(senderId, arr[2]); // arr[2] is the room name
         }

         boolean cancelled = arr[2].equals("1");

         int offset = 3;

         switch (packetType) {
             case KEEPALIVE:
                 int sessionId = Integer.parseInt(arr[offset]);
                 return new PacketKeepAlive(senderId, sessionId);
             case JOIN:
                 UUID hostId = UUID.fromString(arr[3]);
                 String username = arr[4];
                 String version = arr[5];
                 return new PacketJoin(senderId, username, hostId, version);
         }

         return null;
     }
}
