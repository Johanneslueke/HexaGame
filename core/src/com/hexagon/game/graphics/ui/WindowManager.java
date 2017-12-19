package com.hexagon.game.graphics.ui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hexagon.game.graphics.ui.windows.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven on 19.12.2017.
 */

public class WindowManager {

    private List<Window> windowList;

    public WindowManager() {
        windowList = new ArrayList<>();
    }


    public List<Window> getWindowList() {
        return windowList;
    }

    public void render(ShapeRenderer renderer) {
        for (Window window : windowList) {
            window.render(renderer);
        }
    }
}
