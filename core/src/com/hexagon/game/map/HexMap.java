package com.hexagon.game.map;

import com.hexagon.game.map.tiles.Tile;

/**
 * Created by Sven on 08.12.2017.
 */

public class HexMap {

    private Hexagon hexagon; // reference hexagon

    private Tile[][] tiles;

    public HexMap(int sizeX, int sizeY) {
        tiles = new Tile[sizeX][sizeY];

    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Tile getTileAt(int x, int y) {
        if (x < 0 || y < 0
                || x > tiles.length
                || y > tiles[x].length) {
            return null;
        }
        return tiles[x][y];
    }
}
