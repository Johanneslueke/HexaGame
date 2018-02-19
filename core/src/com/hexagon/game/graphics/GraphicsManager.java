package com.hexagon.game.graphics;

/**
 * Created by Sven on 18.12.2017.
 */

/**
 * TODO(SVEN): Is this class needed for something? seems quite useless at least in this state!
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
