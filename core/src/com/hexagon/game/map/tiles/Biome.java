package com.hexagon.game.map.tiles;

/**
 * Created by Sven on 21.12.2017.
 */

public enum Biome {

    /*PLAINS("plains.g3db"),
    DESERT("desert_lvl0.g3db"),
    ICE("ice.g3db"),
    WATER("water.g3db"),*/
    PLAINS("models/grassland.g3db"),
    DESERT("models/desert.g3db"),
    ICE("models/iceland.g3db"),
    WATER("flat_hexagon.g3db")
    ;

    private String model;

    Biome(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }
}
