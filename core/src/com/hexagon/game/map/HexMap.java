package com.hexagon.game.map;

import com.hexagon.game.map.tiles.Chunk;
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.models.RenderTile;

/**
 * Created by Sven on 08.12.2017.
 */

public class HexMap {

    private Hexagon hexagon; // reference hexagon

    private Tile[][] tiles;
    private Chunk[][] chunks;


    public HexMap(int sizeX, int sizeY) {
        tiles = new Tile[sizeX][sizeY];
        System.out.println((sizeX >> 4) + ", " + (sizeY >> 4));
        chunks = new Chunk[(sizeX >> 4) + 1][(sizeY >> 4) + 1];
    }

    public void populateChunk(int chunkX, int chunkY) {
        Chunk chunk = chunks[chunkX][chunkY];
        if (chunk == null) {
            chunk = chunks[chunkX][chunkY] = new Chunk();
        }
        int tileX = chunkX << 4;
        int tileY = chunkY << 4;
        for (int x=0; x<16; x++) {
            for (int y=0; y<16; y++) {
                int arrayX = x + tileX;
                int arrayY = y + tileY;
                if (arrayX >= tiles.length
                        || arrayY >= tiles[arrayX].length) {
                    continue;
                }
                RenderTile tile = tiles[arrayX][arrayY].getRenderTile();
                chunk.getRenderTiles().add(tile);
            }
        }
    }

    public void populateChunks() {
        System.out.println("Populating chunks");
        for (int chunkX=0; chunkX<chunks.length; chunkX++) {
            for (int chunkY=0; chunkY<chunks[chunkX].length; chunkY++) {
                populateChunk(chunkX, chunkY);
            }
        }
        /*for (int x=0; x<tiles.length; x++) {
            int chunkX = x >> 4;
            for (int y=0; y<tiles[x].length; y++) {
                int chunkY = y >> 4;
                Chunk chunk = chunks[chunkX][chunkY];
                if (chunk == null) {
                    chunk = chunks[chunkX][chunkY] = new Chunk();
                }
                RenderTile tile = tiles[x][y].getRenderTile();
                chunk.getRenderTiles().add(tile);
            }
        }*/
        System.out.println("Updating");
        for (int x=0; x<chunks.length; x++) {
            for (int y = 0; y < chunks[x].length; y++) {
                Chunk chunk = chunks[x][y];
                chunk.updateBounds();
            }
        }
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

    public Chunk[][] getChunks() {
        return chunks;
    }
}
