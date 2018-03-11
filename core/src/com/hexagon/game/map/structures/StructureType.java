package com.hexagon.game.map.structures;

/**
 * Created by Sven on 13.02.2018.
 */

public enum StructureType {

    FOREST(new String[]{"forest.g3db"}),
    CITY(new String[]{"city_lvl1.g3db", "city_lvl2.g3db", "city_lvl3.g3db", "city_lvl4.g3db", "city_lvl5.g3db"}),
    STREET(new String[]{"street.g3db"}),
    RESOURCE(null),
    ORE(null),
    MINE(new String[]{"mine.g3db"}),
    CROPS(null);

    private String[] paths;

    StructureType(String[] paths) {
        this.paths = paths;
    }

    public String[] getPaths() {
        return paths;
    }
}
