package com.hexagon.game.map.generator;

import com.hexagon.game.map.Tile;

import java.util.Random;


/**
 * Created by Sven on 08.12.2017.
 */

public abstract class TileGenerator {

    public abstract Tile generate(Tile tile, int x, int y, Random random);

}
