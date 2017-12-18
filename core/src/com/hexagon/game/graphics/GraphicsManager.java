package com.hexagon.game.graphics;

/**
 * Created by Sven on 18.12.2017.
 */

public class GraphicsManager {

    private static GraphicsManager instance;

    public GraphicsManager() {
        instance = this;
    }

    public static GraphicsManager getInstance() {
        return instance;
    }
}
