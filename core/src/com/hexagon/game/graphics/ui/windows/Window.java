package com.hexagon.game.graphics.ui.windows;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.ui.UILabel;
import com.hexagon.game.graphics.ui.UiElement;
import com.hexagon.game.graphics.ui.buttons.UiButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven on 19.12.2017.
 */

public class Window implements Comparable<Window> {

    private int priority = 1;

    float x;
    float y;

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
        for (int i=0; i<elementList.size(); i++) {
            elementList.get(i).update();
        }
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0f, 0f, 0.0f, 0.6f);
        renderer.rect(x, y, width, height);
        for (int i=0; i<elementList.size(); i++) {
            elementList.get(i).render(renderer);
        }
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

    public void removeButtons(Stage stage) {
        List<UiElement> toRemove = new ArrayList<>();
        for (UiElement element : elementList) {
            if (element instanceof UiButton) {
                element.removeFromStage(stage);
                toRemove.add(element);
            }
        }
        elementList.removeAll(toRemove);
    }

    public void remove(UiElement element, Stage stage) {
        elementList.remove(element);
        element.removeFromStage(stage);
    }

    public void removeLabels(Stage stage) {
        List<UiElement> toRemove = new ArrayList<>();
        for (UiElement element : elementList) {
            if (element instanceof UILabel) {
                element.removeFromStage(stage);
                toRemove.add(element);
            }
        }
        elementList.removeAll(toRemove);
    }

    public void removeAll(Stage stage) {
        for (UiElement element : elementList) {
            element.removeFromStage(stage);
        }
        elementList.clear();
    }

    public void add(UiElement element, Stage stage) {
        element.addToStage(stage);
        if (visible) {
            element.show(stage);
        } else {
            element.hide(stage);
        }
        elementList.add(element);
        element.setDisplayX(x + element.getX());
        element.setDisplayY(y + element.getY());
    }

    public void updateElements() {
        for (UiElement element : elementList) {
            element.setDisplayX(x + element.getX());
            element.setDisplayY(y + element.getY());
        }
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

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Windows with the highest priority will be drawn LAST!
     * (drawn last == drawn on top of everything else)
     * @param window The window to compare to
     * @return priority diff
     */
    @Override
    public int compareTo(Window window) {
        return this.getPriority() - window.getPriority();
    }
}
