package com.hexagon.game.map.structures;

/**
 * Created by Sven on 05.03.2018.
 */

public class StructureCity extends Structure {

    private int level = 0;


    public StructureCity() {
        super(StructureType.CITY);
        level = 0;
    }

    public StructureCity(int level) {
        super(StructureType.CITY);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
