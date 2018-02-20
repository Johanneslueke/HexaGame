package com.hexagon.game.network.packets;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

/**
 * This packet will be send by the client!!
 */
public class PacketJoin extends Packet {


    /**
     * Nickname of the player
     * The combination of username & hostId must be unique!
     */
    private String Username;

    /**
     * Issued by the Client-Server itself
     */
    private UUID hostId;
    private String Version;


    public PacketJoin(String username,UUID hostId,String version) {
        super(PacketType.JOIN);

        this.hostId = hostId;
        this.Username = username;
        this.Version = version;
    }

    public PacketJoin(UUID senderId, String username,UUID hostId,String version) {
        super(PacketType.JOIN, senderId);

        this.hostId = hostId;
        this.Username = username;
        this.Version = version;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID senderId) {
        this.hostId = senderId;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    @Override
    public String serialize() {
        return super.serialize()
                + hostId.toString() + ";" + Username + ";" + Version + ";";
    }
}
