package com.hexagon.game.map.tiles;

import com.hexagon.game.map.TileLocation;

/**
 * Created by Sven on 08.12.2017.
 */

public class Tile {

    private TileLocation tileLocation;
    private int arrayX;
    private int arrayY;

    private Biome biome = Biome.ICE;


    public TileLocation getTileLocation() {
        return tileLocation;
    }

    public Tile(int arrayX, int arrayY) {
        this.arrayX = arrayX;
        this.arrayY = arrayY;
    }

    public void setTileLocation(TileLocation tileLocation) {
        this.tileLocation = tileLocation;
    }

    public Biome getBiome() {
        return biome;
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }
}
