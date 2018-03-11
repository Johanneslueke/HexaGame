package com.hexagon.game.models;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

/**
 * Created by Sven on 18.12.2017.
 */

public class HexModelAnimated extends HexModel {

    private AnimationController animationController;

    private float velY = 0.1f;

    public HexModelAnimated(ModelInstance modelInstance, String animationName) {
        super(modelInstance);
        animationController = new AnimationController(modelInstance);
        /*System.out.println("Animations: " + modelInstance.animations.size);
        for (int i=0; i<modelInstance.animations.size; i++) {
            System.out.println("Animation " + i + ": " + modelInstance.animations.get(i).id);
        }*/
        animationController.setAnimation(animationName);
    }

    public void update(float delta) {
        if (y >= 0) {
            velY = (float) (-0.03f - Math.random()*0.015f);
        } else if (y <= -6) {
            velY = (float) (0.03f + Math.random()*0.015f);
        }
        move(0, velY, 0);
        //animationController.update(delta);
        //modelInstance.transform.setToTranslation(x, y, z);
    }


}
