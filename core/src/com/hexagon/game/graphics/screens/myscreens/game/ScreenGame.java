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
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
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
import com.hexagon.game.models.HexModel;
import com.hexagon.game.models.HexTile;
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

    private DirectionalShadowLight shadowLight;
    private ModelBatch shadowBatch;

    private HexMap currentMap;
    private float mapLength;

    private Map<TileLocation, HexTile> modelInstanceMap = new HashMap<>();

    private ModelCache modelCache;

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

        //modelCache = new ModelCache();

        // Load all Biome Models
        for (Biome biome : Biome.values()) {
            biomeModelMap.put(biome, modelLoader.loadModel(Gdx.files.getFileHandle(biome.getModel(), Files.FileType.Internal)));
        }

        box = new ModelBuilder().createBox(100, 2, 100,
                new Material(ColorAttribute.createDiffuse(0.6f, 0.6f, 0.6f, 1)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        tree = modelLoader.loadModel(Gdx.files.getFileHandle("tree2.g3db", Files.FileType.Internal));
        treeInstance = new ModelInstance(tree);
        treeInstance.transform.translate((float) 0.5f, 1, (float) 0.5f);



        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.5f, 0.5f, 0f, -0.8f, -0.2f));
        environment.add((shadowLight = new DirectionalShadowLight(1024*2, 1024*2, 60f*4, 60f*4, 1f, 300f))
                .set(1f, 1f, 1f, 40.0f, -35f, -35f));
        environment.shadowMap = shadowLight;
        shadowBatch = new ModelBatch(new DepthShaderProvider());

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

        modelCache = new ModelCache();

        modelCache.begin();

        float height = 0;
        for (int x=0; x<hexMap.getTiles().length; x++) {
            for (int y=0; y<hexMap.getTiles()[x].length; y++) {
                Tile tile = hexMap.getTiles()[x][y];

                HexModel hexModel = new HexModel(new ModelInstance(biomeModelMap.get(tile.getBiome())));

                TileLocation loc = HexagonUtil.getTileLocation(x, y);
                hexModel.move((float) loc.getX(), height, (float) loc.getY());
                /*if (height == 0) {
                    height = 0.05f;
                } else {
                    height = 0;
                }*/

                HexTile hexTile = new HexTile(hexModel);
                if (tile.getBiome() == Biome.PLAINS) {
                    if (Math.random() < 0.4) {
                        HexModel model1 = new HexModel(new ModelInstance(tree));
                        model1.move((float) loc.getX() + 0.3f, height, (float) loc.getY() + 0.2f);
                        hexTile.getStructures().add(model1);
                    }
                    if (Math.random() < 0.4) {
                        HexModel model2 = new HexModel(new ModelInstance(tree));
                        model2.move((float) loc.getX() - 0.3f, height, (float) loc.getY());
                        hexTile.getStructures().add(model2);

                    }
                    if (Math.random() < 0.4) {
                        HexModel model3 = new HexModel(new ModelInstance(tree));
                        model3.move((float) loc.getX() + 0.3f, height, (float) loc.getY() - 0.3f);
                        hexTile.getStructures().add(model3);
                    }
                }

                modelInstanceMap.put(loc, hexTile);
                modelCache.add(hexModel.getModelInstance());
                for (HexModel structure : hexTile.getStructures()) {
                    modelCache.add(structure.getModelInstance());
                }
            }
        }


        /*TileLocation loc = HexagonUtil.getTileLocation(0, 0);
        bigBox = new ModelInstance(box);
        bigBox.transform.translate((float) loc.getX() + 50, height, (float) loc.getY() + 50);
        bigBox.transform.rotate(0, 1, 0, 90);*/

        modelCache.end();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    long a = 0;
    long b = 0;

    @Override
    public void render(float delta) {
        inputGame.update(delta);

        ScreenManager.getInstance().clearScreen(0.2f, 0.25f, 0.35f);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
        //Gdx.gl.glDisable(GL20.GL_BLEND); // disallow transparent drawing

        //camController.update();

        shadowLight.begin(Vector3.Zero, camera.direction);
        shadowBatch.begin(shadowLight.getCamera());

        shadowBatch.render(modelCache);

        shadowBatch.end();
        shadowLight.end();

        modelBatch.begin(camera);



        /*if (camera.position.x < 10) {
            if (camera.position.x < 0) {
                camera.position.x = mapLength;
            } else {
                for (TileLocation loc : this.modelInstanceMap.keySet()) {
                    if (loc.getX() < mapLength - 10) {
                        continue;
                    }
                    HexModel model = this.modelInstanceMap.get(loc).getModel();

                    model.move(-mapLength, 0, 0);
                    modelBatch.render(model.getModelInstance(), environment);
                    model.move(mapLength, 0, 0);
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
                    HexModel model = this.modelInstanceMap.get(loc).getModel();

                    model.move(mapLength, 0, 0);
                    modelBatch.render(model.getModelInstance(), environment);
                    model.move(-mapLength, 0, 0);
                }
            }
        }*/


        /*a = System.nanoTime();
        for (TileLocation loc : this.modelInstanceMap.keySet()) {
            if (loc.getX() < camera.position.x - 20
                    || loc.getX() > camera.position.x + 20) {
                continue;
            }
            if (loc.getY() > camera.position.z + 5) {
                continue;
            }
            HexTile tile = this.modelInstanceMap.get(loc);
            HexModel model = tile.getModel();
            //modelBatch.render(model.getModelInstance(), environment);
            if (tile.getStructures().size() > 0) {
                for (HexModel structure : tile.getStructures()) {
                    modelBatch.render(structure.getModelInstance());
                }
            }
        }
        a = System.nanoTime() - a;*/



        //modelBatch.render(treeInstance, environment);
        a = System.currentTimeMillis();
        modelBatch.render(modelCache, environment);
        a = System.currentTimeMillis() - a;
        b = System.currentTimeMillis();
        modelBatch.end();
        b = System.currentTimeMillis() - b;


        batch.begin();
        font.draw(batch, "Awesome game " + Gdx.graphics.getFramesPerSecond(), 20, 20);
        batch.end();


        /*Gdx.gl.glEnable(GL20.GL_BLEND); // allows transparent drawing
        shapeRenderer.begin();
        windowManager.render(shapeRenderer);
        shapeRenderer.end();*/

        //stage.draw();
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
        modelCache.dispose();
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    public DirectionalShadowLight getShadowLight() {
        return shadowLight;
    }

    public Map<TileLocation, HexTile> getModelInstanceMap() {
        return modelInstanceMap;
    }
}
