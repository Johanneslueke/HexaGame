package com.hexagon.game.graphics.screens.myscreens.game;

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
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.MapManager;
import com.hexagon.game.map.TileLocation;
import com.hexagon.game.map.tiles.Biome;
import com.hexagon.game.map.tiles.Tile;
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
    private Map<Biome, Model> biomeModelMap = new HashMap<>();
    private Model box;
    private Model tree;
    private ModelInstance bigBox;
    private ModelInstance treeInstance;
    private Environment environment;
    private InputGame inputGame;
    private CameraInputController camController;

    private HexMap currentMap;
    private float mapLength;

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
        camera.position.set(0, 6f, 0);
        camera.lookAt(0, 0, -3f);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        inputGame = new InputGame(this);
        InputManager.getInstance().register(inputGame);

        // Model loader needs a binary json reader to decode
        UBJsonReader jsonReader = new UBJsonReader();
        // Create a model loader passing in our json reader
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);

        // Load all Biome Models
        for (Biome biome : Biome.values()) {
            biomeModelMap.put(biome, modelLoader.loadModel(Gdx.files.getFileHandle(biome.getModel(), Files.FileType.Internal)));
        }

        box = new ModelBuilder().createBox(100, 2, 100,
                new Material(ColorAttribute.createDiffuse(0.6f, 0.6f, 0.6f, 1)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        tree = modelLoader.loadModel(Gdx.files.getFileHandle("tree.g3db", Files.FileType.Internal));
        treeInstance = new ModelInstance(tree);
        treeInstance.transform.translate((float) 0.5f, 1, (float) 0.5f);



        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.5f, 0.5f, 0f, -0.8f, -0.2f));

        camController = new CameraInputController(camera);
        //InputManager.getInstance().register(camController);

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
    public void show() {
        super.show();

        HexMap hexMap = MapManager.getInstance().getCurrentHexMap();
        if (hexMap == null) {
            return;
        }
        currentMap = hexMap;
        mapLength = (float) (currentMap.getTiles().length*0.75f * HexagonUtil.hexagon.getSideLengthX()*2f);

        float height = 0;
        for (int x=0; x<hexMap.getTiles().length; x++) {
            for (int y=0; y<hexMap.getTiles()[x].length; y++) {
                Tile tile = hexMap.getTiles()[x][y];

                ModelInstance modelInstance = new ModelInstance(biomeModelMap.get(tile.getBiome()));

                TileLocation loc = HexagonUtil.getTileLocation(x, y);
                modelInstance.transform.translate((float) loc.getX(), height, (float) loc.getY());
                if (height == 0) {
                    height = 0.05f;
                } else {
                    height = 0;
                }

                modelInstanceMap.put(loc, modelInstance);
            }
        }


        TileLocation loc = HexagonUtil.getTileLocation(0, 0);
        bigBox = new ModelInstance(box);
        bigBox.transform.translate((float) loc.getX() + 50, height, (float) loc.getY() + 50);
        bigBox.transform.rotate(0, 1, 0, 90);
    }

    @Override
    public void render(float delta) {
        inputGame.update(delta);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ScreenManager.getInstance().clearScreen(0.2f, 0.25f, 0.35f);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
        //Gdx.gl.glDisable(GL20.GL_BLEND); // disallow transparent drawing

        //camController.update();
        modelBatch.begin(camera);

        if (camera.position.x < 10) {
            if (camera.position.x < 0) {
                camera.position.x = mapLength;
            } else {
                for (TileLocation loc : this.modelInstanceMap.keySet()) {
                    if (loc.getX() < mapLength - 10) {
                        continue;
                    }
                    ModelInstance model = this.modelInstanceMap.get(loc);

                    model.transform.translate(-mapLength, 0, 0);
                    modelBatch.render(model, environment);
                    model.transform.translate(mapLength, 0, 0);
                }
            }
        } else if (camera.position.x >= mapLength - 10) {
            if (camera.position.x >= mapLength) {
                camera.position.x = 0;
            } else {
                for (TileLocation loc : this.modelInstanceMap.keySet()) {
                    if (loc.getX() > 10) {
                        continue;
                    }
                    ModelInstance model = this.modelInstanceMap.get(loc);

                    model.transform.translate(mapLength, 0, 0);
                    modelBatch.render(model, environment);
                    model.transform.translate(-mapLength, 0, 0);
                }
            }
        }

        for (TileLocation loc : this.modelInstanceMap.keySet()) {
            if (loc.getX() < camera.position.x - 10
                    || loc.getX() > camera.position.x + 10) {
                continue;
            }
            if (loc.getY() > camera.position.z + 2) {
                continue;
            }
            ModelInstance model = this.modelInstanceMap.get(loc);
            modelBatch.render(model, environment);
        }

        modelBatch.render(treeInstance, environment);
        modelBatch.end();

        batch.begin();
        font.draw(batch, "Awesome game " + Gdx.graphics.getFramesPerSecond(), 20, 20);
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
        modelBatch.dispose();
        for (Model model : biomeModelMap.values()) {
            model.dispose();
        }
        font.dispose();
        batch.dispose();
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }
}
