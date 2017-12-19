package com.hexagon.game.graphics.screens.myscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.ui.UiButton;
import com.hexagon.game.graphics.ui.WindowManager;
import com.hexagon.game.graphics.ui.windows.Window;

/**
 * Created by Sven on 14.12.2017.
 */

public class ScreenMainMenu extends HexagonScreen {

    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private WindowManager windowManager;


    public ScreenMainMenu() {
        super(ScreenType.MAIN_MENU);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        windowManager = new WindowManager();


        final Window window = new Window(20, Gdx.graphics.getHeight() - 50 - 200, 200, 200);
        windowManager.getWindowList().add(window);

        UiButton buttonWindow = new UiButton("Inside Window Button", 40, Gdx.graphics.getHeight() - 100, 100, 50);
        buttonWindow.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Clicked inside Window Button");
            }
        });
        window.add(buttonWindow, stage);

        UiButton button = new UiButton("Hello World", 50, Gdx.graphics.getHeight() - 50, 100, 50);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (window.isVisible()) {
                    System.out.println("Hiding");
                    window.hide(stage);
                } else {
                    window.show(stage);
                    System.out.println("Showing");
                }
            }
        });
        button.addToStage(stage);




    }

    @Override
    public void render(float delta) {


        ScreenManager.getInstance().clearScreen(0.2f, 0.25f, 0.35f);
        batch.begin();
        font.draw(batch, "Main menu", 20, 20);
        batch.end();


        Gdx.gl.glEnable(GL20.GL_BLEND); // allows transparent drawing
        shapeRenderer.begin();
        windowManager.render(shapeRenderer);
        shapeRenderer.end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
        stage.dispose();
        batch.dispose();
    }
}
