package com.hexagon.game.graphics.ui.windows;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.ui.UiElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven on 19.12.2017.
 */

public class Window {

    private float x;
    private float y;

    private float width;
    private float height;

    private boolean visible = false;

    List<UiElement> elementList;

    public Window(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        elementList = new ArrayList<>();
    }

    public void render(ShapeRenderer renderer) {
        if (!visible) {
            return;
        }
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0f, 0f, 0.0f, 0.2f);
        renderer.rect(x, y, width, height);
    }

    public void show(Stage stage) {
        visible = true;
        for (UiElement element : elementList) {
            element.show(stage);
        }
    }

    public void hide(Stage stage) {
        visible = false;
        for (UiElement element : elementList) {
            element.hide(stage);
        }
    }

    public void add(UiElement element, Stage stage) {
        element.addToStage(stage);
        if (visible) {
            element.show(stage);
        } else {
            element.hide(stage);
        }
        elementList.add(element);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public List<UiElement> getElementList() {
        return elementList;
    }

    public boolean isVisible() {
        return visible;
    }
}
