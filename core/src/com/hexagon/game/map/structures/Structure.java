package com.hexagon.game.map.structures;

/**
 * Structures are for example Trees, Buildings, Rocks...
 *
 * Created by Sven on 08.12.2017.
 */

public class Structure {

    private StructureType type;

    public Structure(StructureType type) {
        this.type = type;
    }

    public StructureType getType() {
        return type;
    }

    public void setType(StructureType type) {
        this.type = type;
    }
}
