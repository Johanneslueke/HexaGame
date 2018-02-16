package com.hexagon.game.models;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.hexagon.game.map.TileLocation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * HexTiles are only for rendering purposes
 *
 * Created by Sven on 22.12.2017.
 */

public class RenderTile {

    private TileLocation tileLocation;
    private HexModel model;
    private List<HexModel> structures;

    public RenderTile(TileLocation location, HexModel model) {
        this.tileLocation = location;
        this.model = model;
        this.structures = new ArrayList<>();
    }

    public void render(ModelBatch batch, Environment environment) {
        batch.render(model.getModelInstance(), environment);
        for (int i=0; i<structures.size(); i++) {
            batch.render(structures.get(i).getModelInstance(), environment);
        }
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

    public TileLocation getTileLocation() {
        return tileLocation;
    }

    public void setTileLocation(TileLocation tileLocation) {
        this.tileLocation = tileLocation;
    }

    public void setStructures(List<HexModel> structures) {
        this.structures = structures;
    }
}
