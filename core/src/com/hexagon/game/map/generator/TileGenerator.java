package com.hexagon.game.map.generator;

import com.hexagon.game.map.tiles.Tile;

import java.util.List;
import java.util.Random;


/**
 * Created by Sven on 08.12.2017.
 */

public abstract class TileGenerator {

    public abstract Tile generate(List<Tile> generatedTiles, Tile tile, int x, int y, Random random);

}
