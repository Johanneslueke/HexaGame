package com.hexagon.game.graphics.screens.myscreens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.hexagon.game.models.HexModel;
import com.hexagon.game.util.HexagonUtil;

/**
 * Created by Sven on 20.12.2017.
 */

public class InputGame implements InputProcessor {

    private static float SPEED = 5;

    private float velX = 0;
    private float velY = 0;

    private HexModel selected;
    private Material selectionMaterial;
    private Array<Material> originalMaterial;

    private ScreenGame screenGame;

    public InputGame(ScreenGame screenGame) {
        this.screenGame = screenGame;

        selectionMaterial = new Material();
        selectionMaterial.set(ColorAttribute.createDiffuse(Color.ORANGE));
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.A) {
            velX = -SPEED;
        } else if (keycode == Input.Keys.D) {
            velX = SPEED;
        }
        if (keycode == Input.Keys.W) {
            velY = -SPEED;
        } else if (keycode == Input.Keys.S) {
            velY = SPEED;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        HexModel model = getObject(screenX, screenY);
        if (model == null) {
            System.out.println("null");
        } else {
            select(model);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        HexModel model = getObject(screenX, screenY);
        if (model != null) {
            select(model);
        }
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        screenGame.getCamera().fieldOfView += amount*3f;
        screenGame.getCamera().update();
        return true;
    }

    public void update(float delta) {
        if (velX == 0 && velY == 0) {
            return;
        }
        Camera camera = screenGame.getCamera();
        camera.position.add(velX * delta, 0, velY * delta);

        camera.update();

        if (!Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
            velY *= 0.8;
            if (velY >= -0.01 && velY <= 0.01) {
                velY = 0;
            }
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
            velX *= 0.8;
            if (velX >= -0.01 && velX <= 0.01) {
                velX = 0;
            }
        }
    }

    private Vector3 position = new Vector3();

    public HexModel getObject(float x, float y) {
        Ray ray = screenGame.getCamera().getPickRay(x, y);

        for (HexModel model : screenGame.getModelInstanceMap().values()) {
            model.getModelInstance().transform.getTranslation(position);
            position.add((float) (HexagonUtil.hexagon.getSideLengthX()/2), 0.5f, (float) (HexagonUtil.hexagon.getSideLengthY()/2));
            //float distance = ray.origin.dst2(position);
            if (Intersector.intersectRaySphere(ray, position, 1.0f, null)) {
                return model;
            }
        }

        return null;
    }

    public void select(HexModel model) {
        if (selected != null) {
            //selected.getModelInstance().materials.clear();
            //selected.getModelInstance().materials.addAll(originalMaterial);
            for (int i=0; i<selected.getModelInstance().materials.size; i++) {
                if (i >= originalMaterial.size) {
                    break;
                }
                selected.getModelInstance().materials.get(i).clear();
                selected.getModelInstance().materials.get(i).set(originalMaterial.get(i));
            }
            /*Array<Material> newMaterials = new Array<>();
            for (Material m : selected.getModelInstance().materials) {
                if (!m.equals(selectionMaterial)) {
                    newMaterials.add(m);
                }
            }
            selected.getModelInstance().materials.clear();
            selected.getModelInstance().materials.addAll(newMaterials);*/
        }
        //Material material = model.getModelInstance().materials.get(0);
        //originalMaterial = material;
        //material.clear();
        originalMaterial = model.getModelInstance().materials;
        Material material = model.getModelInstance().materials.get(0);
        //material.clear();
        material.set(selectionMaterial);
        selected = model;
    }
}
