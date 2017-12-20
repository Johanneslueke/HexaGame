package com.hexagon.game.graphics.screens.myscreens;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.UBJsonReader;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.graphics.ui.windows.DropdownScrollableWindow;
import com.hexagon.game.input.InputManager;
import com.hexagon.game.map.TileLocation;
import com.hexagon.game.util.HexagonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sven on 14.12.2017.
 */

public class ScreenGame extends HexagonScreen {

    private SpriteBatch batch;
    private BitmapFont font;

    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Model model;
    private Model box;
    private ModelInstance bigBox;
    private Environment environment;
    private CameraInputController camController;

    private Map<TileLocation, ModelInstance> modelInstanceMap = new HashMap<>();

    public ScreenGame() {
        super(ScreenType.GAME);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        modelBatch = new ModelBatch();
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(10f, 10f, 10f);
        camera.lookAt(0,0,0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        // Model loader needs a binary json reader to decode
        UBJsonReader jsonReader = new UBJsonReader();
        // Create a model loader passing in our json reader
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        model = modelLoader.loadModel(Gdx.files.getFileHandle("desert_lvl0.g3db", Files.FileType.Internal));

        box = new ModelBuilder().createBox(100, 2, 100,
                new Material(ColorAttribute.createDiffuse(0.6f, 0.6f, 0.6f, 1)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);


        float height = 0;
        for (int x=0; x<10; x++) {
            for (int y=0; y<10; y++) {
                ModelInstance modelInstance = new ModelInstance(model);
                TileLocation loc = HexagonUtil.getTileLocation(x, y);
                modelInstance.transform.translate((float) loc.getX(), height, (float) loc.getY());
                modelInstance.transform.rotate(0, 1, 0, 90);

                modelInstanceMap.put(loc, modelInstance);
            }
            System.out.println(x);
        }


        TileLocation loc = HexagonUtil.getTileLocation(0, 0);
        bigBox = new ModelInstance(box);
        bigBox.transform.translate((float) loc.getX() + 50, height, (float) loc.getY() + 50);
        bigBox.transform.rotate(0, 1, 0, 90);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.5f, 0.5f, 0f, -0.8f, -0.2f));

        camController = new CameraInputController(camera);
        InputManager.getInstance().register(camController);

        UiButton button = new UiButton("Hello World", 50, Gdx.graphics.getHeight() - 50, 100, 50);

        final DropdownScrollableWindow window = new DropdownScrollableWindow(20, 0, 0, 0, 0, 0, 15);
        windowManager.getWindowList().add(window);

        for (int i=0; i<400; i++) {
            UiButton buttonWindow = new UiButton(String.valueOf(i), 0, 0, 50, 20);
            window.add(buttonWindow, stage);
        }

        window.orderAllNeatly(13, 0, 15);
        window.setY(button.getY() - window.getHeight());
        window.setX(button.getX());
        window.updateElements();

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (window.isVisible()) {
                    window.hide(stage);
                } else {
                    window.show(stage);
                }
            }
        });
        button.addToStage(stage);
    }

    @Override
    public void render(float delta) {
        /*if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            for (TileLocation loc : this.modelInstanceMap.keySet()) {
                ModelInstance model = this.modelInstanceMap.get(loc);
                if (model.model.equals(this.box)) {
                    model = new ModelInstance(this.model);
                    model.transform.translate((float) loc.getX(), 0, (float) loc.getY());
                    model.transform.rotate(0, 1, 0, 90);
                    modelInstanceMap.put(loc, model);
                } else {
                    model = new ModelInstance(this.box);
                    model.transform.translate((float) loc.getX(), 0, (float) loc.getY());
                    modelInstanceMap.put(loc, model);
                }
            }
        }*/

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ScreenManager.getInstance().clearScreen(0.2f, 0.25f, 0.35f);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
        //Gdx.gl.glDisable(GL20.GL_BLEND); // disallow transparent drawing

        camController.update();
        modelBatch.begin(camera);

        for (TileLocation loc : this.modelInstanceMap.keySet()) {
            ModelInstance model = this.modelInstanceMap.get(loc);

            modelBatch.render(model, environment);
        }
        modelBatch.end();

        batch.begin();
        font.draw(batch, "Awesome game", 20, 20);
        batch.end();


        Gdx.gl.glEnable(GL20.GL_BLEND); // allows transparent drawing
        shapeRenderer.begin();
        windowManager.render(shapeRenderer);
        shapeRenderer.end();

        stage.draw();

        System.out.println(Gdx.graphics.getFramesPerSecond() + " FPS");
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
        modelBatch.dispose();
        model.dispose();
        font.dispose();
        batch.dispose();
    }
}
