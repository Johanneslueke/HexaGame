package com.hexagon.game.graphics;

/**
 * Created by Sven on 18.12.2017.
 */

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.UBJsonReader;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.map.tiles.Biome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles all model loading and accessing, aswell as disposing models
 */
public class ModelManager {

    private static ModelManager instance;

    private List<Model>                         allModels;
    private Map<StructureType, List<Model>>     structureModels;
    private Map<Biome, Model>                   biomeModels;

    // Model loader needs a binary json reader to decode
    private UBJsonReader                 jsonReader;
    // Create a model loader passing in our json reader
    private G3dModelLoader               modelLoader;

    public ModelManager() {
        instance = this;

        structureModels = new HashMap<>();
        allModels       = new ArrayList<>();
        biomeModels     = new HashMap<>();

        jsonReader      = new UBJsonReader();
        modelLoader     = new G3dModelLoader(jsonReader);
    }

    public static ModelManager getInstance() {
        return instance;
    }

    public void loadAllModels() {
        disposeModels();
        structureModels.clear();
        biomeModels.clear();

        for (StructureType structureType : StructureType.values()) {
            List<Model> models = new ArrayList<>();
            if (structureType.getPaths() != null) {
                for (String path : structureType.getPaths()) {
                    models.add(loadModel(path));
                }
            }
            structureModels.put(structureType, models);
        }

        for (Biome biome : Biome.values()) {
            biomeModels.put(biome, loadModel(biome.getModel()));
        }
    }

    public void disposeModels() {
        if (structureModels == null) {
            return;
        }
        for (Model model : allModels) {
            model.dispose();
        }
    }

    public Model loadModel(String path) {
        Model model = modelLoader.loadModel(Gdx.files.getFileHandle(path, Files.FileType.Internal));
        allModels.add(model);
        return model;
    }

    public List<Model> getAllModels() {
        return allModels;
    }

    public Map<StructureType, List<Model>> getStructureModels() {
        return structureModels;
    }

    public List<Model> getStructureModels(StructureType structureType) {
        return structureModels.get(structureType);
    }

    public Map<Biome, Model> getBiomeModels() {
        return biomeModels;
    }
}
