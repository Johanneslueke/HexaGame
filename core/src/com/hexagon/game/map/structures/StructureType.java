package com.hexagon.game.map.structures;

/**
 * Created by Sven on 13.02.2018.
 */

public enum StructureType {

    FOREST(new String[]{"tree2.g3db"}),
    CITY(null),
    STREET(new String[]{"street.g3db"}),
    RESOURCE(null),
    ORE(null),
    CROPS(null);

    private String[] paths;

    StructureType(String[] paths) {
        this.paths = paths;
    }

    public String[] getPaths() {
        return paths;
    }
}
