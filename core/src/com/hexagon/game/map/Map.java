package com.hexagon.game.map;

/**
 * Created by Sven on 08.12.2017.
 */

public class Map {

    private Hexagon hexagon; // reference hexagon

    private Tile[][] tiles;

    public Map(int sizeX, int sizeY) {
        tiles = new Tile[sizeX][sizeY];

    }




}
