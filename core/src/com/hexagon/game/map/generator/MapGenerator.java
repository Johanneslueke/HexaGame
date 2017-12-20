package com.hexagon.game.map.generator;

import com.hexagon.game.map.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Sven on 08.12.2017.
 */

public class MapGenerator implements Runnable {

    //////////////////
    //// SETTINGS ////
    //////////////////
    private Thread thread;
    private int sizeX;
    private int sizeY;

    private List<TileGenerator> tileGeneratorList;
    private Random random;

    ////////////////////
    //// GENERATING ////
    ////////////////////
    private int tilesPerTick = 1;
    private int curX = 0;
    private int curY = 0;

    private boolean finished = false;

    private List<Tile> generatedTiles;

    private GeneratorCallback callback;

    ///////////////////////////
    ///////////////////////////

    public MapGenerator(int sizeX, int sizeY, long seed, GeneratorCallback callback) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.random = new Random(seed);

        tileGeneratorList = new ArrayList<>();
        generatedTiles = new ArrayList<>();
        thread = new Thread();
    }

    public void startGenerating() {
        thread.start();
    }

    private void generateTileAt(int x, int y) {
        Tile tile = new Tile();
        for (TileGenerator tileGenerator : tileGeneratorList) {
            generatedTiles.add(tileGenerator.generate(tile, x, y, random));
        }
    }


    @Override
    public void run() {
        while (!finished) {
            curX++;
            if (curX >= sizeX) {
                curX = 0;
                curY++;
                if (curY >= sizeY) {
                    finished = true;
                    callback.generatorFinished();
                    return;
                }
            }
            Tile tile = new Tile();
            for (TileGenerator tileGenerator : tileGeneratorList) {
                generatedTiles.add(tileGenerator.generate(tile, curX, curY, random));
            }
        }
    }
}
