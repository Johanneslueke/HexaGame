package com.hexagon.game.graphics.ui.windows;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven on 20.12.2017.
 */

public class GroupWindow extends Window {

    private List<Window> windowList;

    public GroupWindow(float x, float y, float width, float height, Stage stage) {
        super(x, y, width, height);

        windowList = new ArrayList<>();

    }

    public List<Window> getWindowList() {
        return windowList;
    }

    @Override
    public void render(ShapeRenderer renderer) {
        for (Window window : windowList) {
            window.render(renderer);
        }
    }

    @Override
    public void show(Stage stage) {
        super.show(stage);
        for (Window window : windowList) {
            window.show(stage);
        }
    }

    @Override
    public void hide(Stage stage) {
        super.hide(stage);
        for (Window window : windowList) {
            window.hide(stage);
        }
    }

    @Override
    public void removeAll(Stage stage) {
        for (Window window : windowList) {
            window.removeAll(stage);
        }
        windowList.clear();

        super.removeAll(stage);
    }

    public void show(int index, Stage stage) {
        // start at 1 so we don't hide the main window
        for (int i=1; i<windowList.size(); i++) {
            windowList.get(i).hide(stage);
        }
        windowList.get(index).show(stage);
    }

    public void hide(int index, Stage stage) {
        windowList.get(index).hide(stage);
    }

    public void show(Window w, Stage stage) {
        // start at 1 so we don't hide the main window
        for (int i=1; i<windowList.size(); i++) {
            windowList.get(i).hide(stage);
        }
        w.show(stage);
    }

    public void hide(Window w, Stage stage) {
        w.hide(stage);
    }
}
