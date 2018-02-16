package com.hexagon.game.map.generator;

import com.badlogic.gdx.Gdx;
import com.hexagon.game.map.tiles.Tile;

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

    private Tile[][] generatedTiles;

    private GeneratorCallback callback;

    ///////////////////////////
    ///////////////////////////

    public MapGenerator(int sizeX, int sizeY, long seed) {
        this.sizeX = sizeX;

        sizeY += 10; // Add 5 at the bottom and top for the ice layers

        this.sizeY = sizeY;
        this.random = new Random(seed);

        tileGeneratorList = new ArrayList<>();
        generatedTiles = new Tile[sizeX][sizeY];
        thread = new Thread(this);
    }

    public void setCallback(GeneratorCallback callback) {
        this.callback = callback;
    }


    public void startGenerating() {
        thread.start();
    }

    private void generateTileAt(int x, int y) {
        Tile tile = new Tile(x, y);
        for (TileGenerator tileGenerator : tileGeneratorList) {
            generatedTiles[x][y] = tileGenerator.generate(tile, x, y, random);
        }
    }


    @Override
    public void run() {
        while (!finished) {
            Tile tile = new Tile(curX, curY);
            for (TileGenerator tileGenerator : tileGeneratorList) {
                generatedTiles[curX][curY] = tileGenerator.generate(tile, curX, curY, random);
            }
            curX++;
            if (curX >= sizeX) {
                curX = 0;
                curY++;
                if (curY >= sizeY) {
                    finished = true;
                    // run on main thread to prevent concurrent modification exception
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            callback.generatorFinished();
                        }
                    });
                    return;
                }
            }
        }
    }

    public List<TileGenerator> getTileGeneratorList() {
        return tileGeneratorList;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public Tile[][] getGeneratedTiles() {
        return generatedTiles;
    }
}
