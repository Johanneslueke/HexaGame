package com.hexagon.game.network.packets;

import com.hexagon.game.map.tiles.Tile;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketMapUpdate extends Packet {

    /**
     * JSON-String
     */
    private String  RawMapData;
    private transient Tile[][] tiles;

    public PacketMapUpdate(String rawMapData) {
        super(PacketType.MAPUPDATE);
        this.RawMapData = rawMapData;
    }

    public PacketMapUpdate(UUID clientID, String rawMapData) {
        super(PacketType.MAPUPDATE, clientID);
        this.RawMapData = rawMapData;
    }

    public String getRawMapData() {
        return RawMapData;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    @Override
    public String serialize() {
        return super.serialize() + RawMapData + ";";
    }
}
