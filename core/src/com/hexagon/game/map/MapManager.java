package com.hexagon.game.map;

/**
 * Created by Sven on 21.12.2017.
 */

public class MapManager {

    private static MapManager instance;

    private HexMap currentHexMap;

    public MapManager() {
        instance = this;
    }

    public HexMap getCurrentHexMap() {
        return currentHexMap;
    }

    public void setCurrentHexMap(HexMap currentHexMap) {
        this.currentHexMap = currentHexMap;
    }

    public static MapManager getInstance() {
        return instance;
    }
}
