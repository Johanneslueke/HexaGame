package com.hexagon.game.map.detail;

import com.hexagon.game.map.TileLocation;
import com.hexagon.game.map.tiles.Tile;

/**
 * Created by Sven on 15.02.2018.
 */

public class CarTile {

    TileLocation location;
    Tile tile;

    public CarTile(TileLocation location, Tile tile) {
        this.location = location;
        this.tile = tile;
    }
}
