package com.hexagon.game.util;

import com.badlogic.gdx.Gdx;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.ui.UiImage;

/**
 * Created by Sven on 20.12.2017.
 */

public class MenuUtil {

    private static MenuUtil instance;

    private float sizeX = 800;
    private float sizeY = 600;

    private float x;
    private float y;

    public MenuUtil() {
        instance = this;

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        x = width/2 - sizeX/2;
        y = height/2 - sizeY/2;


    }

    public void createStandardMenu(HexagonScreen screen) {
        //UiButton back = new UiButton("Back", x, y, );
        UiImage image = new UiImage(x, y, sizeX, sizeY, "sidebar.png");
        image.addToStage(screen.getStage());
        //Color color = image.getImage().getColor();
        //color.set(color.r, color.g, color.b, 0.5f);

    }

    public float getSizeX() {
        return sizeX;
    }

    public float getSizeY() {
        return sizeY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public static MenuUtil getInstance() {
        return instance;
    }
}
