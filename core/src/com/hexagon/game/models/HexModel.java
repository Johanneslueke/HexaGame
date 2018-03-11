package com.hexagon.game.models;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Created by Sven on 18.12.2017.
 */

public class HexModel {

    ModelInstance modelInstance;
    float x, y, z;


    public HexModel(ModelInstance modelInstance) {
        this.modelInstance = modelInstance;
    }

    public void move(float x, float y, float z) {
        modelInstance.transform.translate(x, y, z);
        this.x += x;
        this.y += y;
        this.z += z;
    }

    /*public void setLocation(float x, float y, float z) {
        modelInstance.transform.set(new float[]{x, y, z});
    }*/

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public void setModelInstance(ModelInstance modelInstance) {
        this.modelInstance = modelInstance;
    }
}
