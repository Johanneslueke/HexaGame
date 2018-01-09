package com.hexagon.game.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * HexTiles are only for rendering purposes
 *
 * Created by Sven on 22.12.2017.
 */

public class HexTile {

    private HexModel model;
    private List<HexModel> structures;

    public HexTile(HexModel model) {
        this.model = model;
        this.structures = new ArrayList<>();
    }

    public HexModel getModel() {
        return model;
    }

    public void setModel(HexModel model) {
        this.model = model;
    }

    public List<HexModel> getStructures() {
        return structures;
    }
}
