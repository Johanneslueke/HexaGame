package com.hexagon.game.graphics.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Sven on 19.12.2017.
 */

public abstract class UiElement {

    private float x;
    private float y;

    private float displayX;
    private float displayY;

    private float width;
    private float height;

    private UpdateEvent updateEvent;

    public UiElement(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.displayX = x;
        this.displayY = y;
    }

    public abstract void addToStage(Stage stage);

    public abstract void removeFromStage(Stage stage);

    public abstract void show(Stage stage);

    public abstract void hide(Stage stage);

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

    public float getDisplayX() {
        return displayX;
    }

    public void setDisplayX(float displayX) {
        this.displayX = displayX;
    }

    public float getDisplayY() {
        return displayY;
    }

    public void setDisplayY(float displayY) {
        this.displayY = displayY;
    }

    public void update() {
        if (updateEvent != null) {
            updateEvent.onUpdate();
        }
    }

    public void setUpdateEvent(UpdateEvent updateEvent) {
        this.updateEvent = updateEvent;
    }
}
