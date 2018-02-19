package com.hexagon.game.graphics.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.ui.WindowManager;
import com.hexagon.game.input.InputManager;

/**
 * Created by Sven on 18.12.2017.
 */

public abstract class HexagonScreen implements Screen {

    /**
     * Defines which of the implented screens should be displayed
     */
    private ScreenType          screenType;

    /**
     * GUI specific -> Holds an list of User Interface Windows
     */
    protected WindowManager     windowManager;

    /**
     * Element of the SceneGraph representing the Application and everything which
     * is visible.
     */
    protected Stage             stage;

    /**
     * Render specific see libGDX documentation
     * https://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/graphics/glutils/ShapeRenderer.html
     */
    protected ShapeRenderer     shapeRenderer;

    /**
     * Constructor which takes as argument the enum type of the implemented display.
     * @param screenType
     */
    public HexagonScreen(ScreenType screenType) {
        this.screenType = screenType;
        stage = new Stage();
        windowManager = new WindowManager();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
    }

    public ScreenType getScreenType() {
        return screenType;
    }

    public WindowManager getWindowManager() {
        return windowManager;
    }

    public Stage getStage() {
        return stage;
    }


    /**
     * Each concrete Display has to implement this on its own
     */
    public abstract void create();

    /**
     *
     */
    @Override
    public void show() {
        InputManager.getInstance().register(this.windowManager);
        InputManager.getInstance().register(this.getStage());
        //HexMultiplexer.getInstance().add(this.getStage());
        //HexMultiplexer.getInstance().add(this.windowManager);
        //HexMultiplexer.getInstance().multiplex();
    }

    /**
     *
     */
    @Override
    public void hide() {
        //HexMultiplexer.getInstance().remove(this.getStage());
        //HexMultiplexer.getInstance().remove(this.windowManager);
        //HexMultiplexer.getInstance().multiplex();
        InputManager.getInstance().unregister(this.getStage());
        InputManager.getInstance().unregister(this.windowManager);
    }

    /**
     *
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
