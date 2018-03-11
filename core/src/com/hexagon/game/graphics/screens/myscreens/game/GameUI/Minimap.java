package com.hexagon.game.graphics.screens.myscreens.game.GameUI;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hexagon.game.graphics.ui.windows.Window;

/**
 * Created by Sven on 01.03.2018.
 */

public class Minimap extends Window {

    public Minimap(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void render(ShapeRenderer renderer) {
        if (!isVisible()) {
            return;
        }
        super.render(renderer);

    }
}
