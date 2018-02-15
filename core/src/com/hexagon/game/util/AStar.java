package com.hexagon.game.util;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.myscreens.game.ScreenGame;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.TileLocation;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.map.tiles.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven on 13.02.2018.
 */

public class AStar {

    private HexMap map;
    private ATile current;
    private ATile start;
    private ATile destination;
    private List<ATile> open = new ArrayList<>();
    private List<ATile> closed = new ArrayList<>();

    public List<ATile> start(ATile start, ATile destination, HexMap map) {
        this.start          = start;
        this.destination    = destination;
        this.map            = map;
        current             = start;

        closed.add(current.copy());
        open.addAll(getNextWalkable(current));
        if (open.size() == 0) {
            throw new RuntimeException("Error while pathfinding: could not find walkable tiles");
        }

        //// DEBUG
        ScreenGame game = (ScreenGame) ScreenManager.getInstance().getCurrentScreen();
        game.getDebugModels().clear();
        /////////

        find();
        return backtrack();
    }

    private boolean isForest(int x, int y) {
        Tile tile = map.getTiles()[x][y];
        if (tile.getStructure() != null
                && tile.getStructure().getType() == StructureType.FOREST) {
            return true;
        }
        return false;
    }

    public float getCostG(ATile parent, ATile current) {
        float cost = parent.getgCost() + 10;

        if (current.getX() >= 1 && current.getY() >= 1
                && current.getX() < map.getTiles().length - 1) {
            if (current.getY() < map.getTiles()[current.getX()].length - 1) {
                int x = current.getX();
                int y = current.getY();
                Tile tile = map.getTiles()[x][y];
                if (tile.getStructure() != null
                        && tile.getStructure().getType() == StructureType.STREET) {
                    return parent.getgCost() + 1;
                }
                if (isForest(x, y)) {
                    cost += 40;
                } else if (isForest(x, y)
                        || isForest(x, y - 1) // North
                        || isForest(x + 1, y - 1)
                        || isForest(x + 1, y)
                        || isForest(x - 1, y)
                        || isForest(x, y + 1) // South
                        || isForest(x - 1, y - 1)
                        ) {
                    cost += 20;
                }
            }
        }

        return cost;
    }

    public float getCostH(ATile tile) {
        //return Math.abs(destination.getX() - tile.getX()) + Math.abs(destination.getY() - tile.getY());
        return (float) (Math.pow(destination.getX() - tile.getX(), 2) + Math.pow(destination.getY() - tile.getY(), 2));
    }

    public ATile getOpenAt(ATile tile) {
        for (ATile openTile : this.open) {
            if (openTile.equals(tile)) {
                return openTile;
            }
        }
        return null;
    }

    public void find() {
        int iterations = 0;



        while (true) {
            iterations++;
            if (iterations >= 5_000) {
                throw new RuntimeException("Error while pathfinding: Too many iterations!");
            }

            ATile lowestScore = getLowestScore();
            if (lowestScore == null) {
                throw new RuntimeException("Error while pathfinding: lowest score is null???");
            }
            open.remove(lowestScore);
            closed.add(lowestScore);

            // We are now standing on this tile
            current = lowestScore.copy();



            System.out.println(current.getX() + ", " + current.getY());
            if (current.equals(destination)) {
                // We are at the destination! Woo!
                System.out.println("Destination! Woo!");
                break;
            }

            List<ATile> walkableTiles = getNextWalkable(current.copy());

            // Check all tiles around us
            for (ATile walkable : walkableTiles) {
                ATile openTile = getOpenAt(walkable);
                if (openTile == null) {
                    // There is no open tile at this walkable tile yet
                    // So we calculate the score for this tile
                    walkable.setgCost(getCostG(walkable.getParent(), walkable));
                    walkable.sethCost(getCostH(walkable));
                    open.add(walkable);
                } else {
                    // This tile is already in the open list
                    // So we calculate the score for this tile and check if the new score is lower than the old one
                    float g = getCostG(walkable.getParent(), walkable);
                    float h = getCostH(walkable);
                    float cost = g + h;
                    if (cost < openTile.getTotalCost()) {
                        openTile.setgCost(g);
                        openTile.sethCost(h);
                        openTile.setParent(current);
                    }
                }
            }
        }
    }

    public ATile getLowestScore() {
        float lowestCost = Float.MAX_VALUE;
        ATile lowest = null;
        for (ATile tile : open) {
            if (tile.getTotalCost() < lowestCost) {
                lowest = tile;
                lowestCost = tile.getTotalCost();
            }
        }
        return lowest;
    }

    public boolean isClosed(ATile tile) {
        for (ATile closedTile : this.closed) {
            if (closedTile.equals(tile)) {
                return true;
            }
        }
        return false;
    }

    public List<ATile> backtrack() {
        // Debug
        Model boxModel = new ModelBuilder().createBox(0.25f, 0.25f, 0.25f,
                new Material(ColorAttribute.createDiffuse(0.6f, 0.6f, 0.6f, 1)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        ScreenGame game = (ScreenGame) ScreenManager.getInstance().getCurrentScreen();
        ///////////

        List<ATile> path = new ArrayList<>();
        ATile next = current;
        while (next != null) {
            System.out.println("Next: " + next.getX() + ", " + next.getY());
            path.add(next);

            // Debug
            ModelInstance box = new ModelInstance(boxModel);
            TileLocation tileLocation = HexagonUtil.getTileLocation(next.getX(), next.getY());
            box.transform.translate((float) tileLocation.getX(), 0.5f, (float) tileLocation.getY());
            game.getDebugModels().add(box);
            ////////////////

            next = next.getParent();
            if (next.equals(start)) {
                break;
            }
        }
        return path;
    }

    public List<ATile> getNextWalkable(ATile parent) {
        ATile north = new ATile(current.x, current.y - 1, parent);

        ATile northEast;
        if ((current.x & 1) == 1) {
            // if x is UNEVEN, add 1 to x for north east
            northEast = new ATile(current.x + 1, current.y, parent);
        } else {
            // if x is EVEN, add 1 to x and remove 1 from y for north east
            northEast = new ATile(current.x + 1, current.y - 1, parent);
        }

        ATile southEast;
        if ((current.x & 1) == 1) {
            // if x is UNEVEN, add 1 to x and 1 to y for south east
            southEast = new ATile(current.x + 1, current.y + 1, parent);
        } else {
            // if x is EVEN, add 1 to x for south east
            southEast = new ATile(current.x + 1, current.y, parent);
        }

        ATile south = new ATile(current.x, current.y + 1, parent);

        ATile southWest;
        if ((current.x & 1) == 1) {
            // if x is UNEVEN, remove 1 from x and add 1 to y for south west
            southWest = new ATile(current.x - 1, current.y + 1, parent);
        } else {
            // if x is EVEN, remove 1 from x for south west
            southWest = new ATile(current.x - 1, current.y, parent);
        }

        ATile northWest;
        if ((current.x & 1) == 1) {
            // if x is UNEVEN, remove 1 from x for north west
            northWest = new ATile(current.x - 1, current.y, parent);
        } else {
            // if x is EVEN, remove 1 from x and y for north west
            northWest = new ATile(current.x - 1, current.y - 1, parent);
        }
        List<ATile> toReturn = new ArrayList<>();

        // TODO: Check if tiles are walkable
        if (!isClosed(north)) {
            toReturn.add(north);
        }
        if (!isClosed(northEast)) {
            toReturn.add(northEast);
        }
        if (!isClosed(southEast)) {
            toReturn.add(southEast);
        }
        if (!isClosed(south)) {
            toReturn.add(south);
        }
        if (!isClosed(southWest)) {
            toReturn.add(southWest);
        }
        if (!isClosed(northWest)) {
            toReturn.add(northWest);
        }

        return toReturn;
    }

    public static class ATile {
        private ATile parent;
        private int x;
        private int y;
        private float gCost;
        private float hCost;
        private Float totalCost = null;

        public ATile(int x, int y, ATile parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public ATile getParent() {
            return parent;
        }

        public void setParent(ATile parent) {
            this.parent = parent;
        }

        public float getgCost() {
            return gCost;
        }

        public void setgCost(float gCost) {
            totalCost = null; // Set totalCost to null so the total cost will be calculated again
            this.gCost = gCost;
        }

        public float gethCost() {
            return hCost;
        }

        public void sethCost(float hCost) {
            totalCost = null; // Set totalCost to null so the total cost will be calculated again
            this.hCost = hCost;
        }

        public float getTotalCost() {
            if (totalCost == null) {
                totalCost = hCost + gCost;
            }
            return totalCost;
        }

        public ATile copy() {
            return new ATile(x, y, parent);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ATile)) {
                return false;
            }
            ATile other = (ATile) o;
            return other.getX() == x && other.getY() == y;
        }
    }

}
