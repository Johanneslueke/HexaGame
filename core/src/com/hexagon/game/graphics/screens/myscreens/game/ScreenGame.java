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
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;
import com.hexagon.game.Main;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.input.InputManager;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.MapManager;
import com.hexagon.game.map.Point;
import com.hexagon.game.map.TileLocation;
import com.hexagon.game.map.detail.Car;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.map.structures.resources.StructureResource;
import com.hexagon.game.map.tiles.Biome;
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.models.HexModel;
import com.hexagon.game.models.RenderTile;
import com.hexagon.game.models.Text3D;
import com.hexagon.game.util.HexagonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sven on 14.12.2017.
 */

public class ScreenGame extends HexagonScreen {

    GameManager     gameManager;

    private SpriteBatch     batch;
    private BitmapFont      font;
    private DecalBatch      decalBatch;

    private PerspectiveCamera       camera;
    private ModelBatch              modelBatch;
    private Map<Biome, Model>       biomeModelMap = new HashMap<>();
    private Model                   box;
    private Model                   tree;
    private ModelInstance           bigBox;
    private ModelInstance           treeInstance;

    private Text3D                  text3D;
    private Decal                   text3Ddecal;

    private Environment             environment;
    private InputGame               inputGame;
    private CameraInputController   camController;

    private DirectionalShadowLight  shadowLight;
    private ModelBatch              shadowBatch;

    private HexMap      currentMap;
    private float       mapLength;

    //Map<RenderTile>                  modelInstanceMap = new HashMap<>();
    List<ModelInstance>             debugModels = new ArrayList<>();
    ModelInstance                   selectedInstance;
    private boolean                 selectedGrow = true;
    AnimationController             animationController;
    Model                           selectedModel;

    Model                           truckModel;
    Model                           streetModel;

    private ModelCache      modelCache;


    // Testing
    List<Car> cars = new ArrayList<>();
    /////

    public ScreenGame() {
        super(ScreenType.GAME);
    }

    private void setupCamera(Point pos, Point lookTo,float fieldOfView, float near,float far){

        camera = new PerspectiveCamera(fieldOfView, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set((float)pos.getX(),(float)pos.getY(), (float)pos.getZ());
        camera.lookAt((float)lookTo.getX(), (float)lookTo.getY(), (float)lookTo.getZ());
        camera.near = near;
        camera.far = far;
        camera.update();
    }

    private void setupModels(){
        batch = new SpriteBatch();
        font = new BitmapFont();
        // Model loader needs a binary json reader to decode
        UBJsonReader jsonReader = new UBJsonReader();
        // Create a model loader passing in our json reader
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        // Load all Biome Models
        for (Biome biome : Biome.values()) {
            biomeModelMap.put(biome, modelLoader.loadModel(Gdx.files.getFileHandle(biome.getModel(), Files.FileType.Internal)));
        }

        tree = modelLoader.loadModel(Gdx.files.getFileHandle("tree2.g3db", Files.FileType.Internal));
        treeInstance = new ModelInstance(tree);
        treeInstance.transform.translate((float) 0.5f, 1, (float) 0.5f);

        selectedModel = modelLoader.loadModel(Gdx.files.getFileHandle("selection_all_anim.g3db", Files.FileType.Internal));

        truckModel = modelLoader.loadModel(Gdx.files.getFileHandle("truck.g3db", Files.FileType.Internal));

        streetModel = modelLoader.loadModel(Gdx.files.getFileHandle("street.g3db", Files.FileType.Internal));

        shadowBatch = new ModelBatch(new DepthShaderProvider());
        modelBatch = new ModelBatch();

        box = new ModelBuilder().createBox(0.3f, 0.3f, 0.3f,
                new Material(ColorAttribute.createDiffuse(0.7f, 0.3f, 0.3f, 1)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

    }

    private void setupEviroment(ColorAttribute attr,DirectionalLight light, DirectionalShadowLight shadow){
        shadowLight = shadow;
        environment = new Environment();
        environment.set(attr);
        environment.add();
        environment.add(shadowLight.set(1f, 1f, 1f, 40.0f, -35f, -35f));
        environment.shadowMap = shadowLight;
    }

    private void createMap(HexMap hexMap){
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

                RenderTile renderTile = new RenderTile(loc, hexModel);
                if (tile.getStructure() != null) {
                    StructureType type = tile.getStructure().getType();
                    if (type == StructureType.FOREST) {
                        boolean placedTrees = false;
                        if (Math.random() < 0.6) {
                            HexModel model1 = new HexModel(new ModelInstance(tree));
                            model1.move((float) loc.getX() + 0.3f, height, (float) loc.getY() + 0.2f);
                            renderTile.getStructures().add(model1);
                            placedTrees = true;
                        }
                        if (Math.random() < 0.6) {
                            HexModel model2 = new HexModel(new ModelInstance(tree));
                            model2.move((float) loc.getX() - 0.3f, height, (float) loc.getY());
                            renderTile.getStructures().add(model2);
                            placedTrees = true;

                        }
                        if (Math.random() < 0.6) {
                            HexModel model3 = new HexModel(new ModelInstance(tree));
                            model3.move((float) loc.getX() + 0.3f, height, (float) loc.getY() - 0.3f);
                            renderTile.getStructures().add(model3);
                            placedTrees = true;
                        }
                        if (!placedTrees) {
                            tile.setStructure(null);
                        }

                    } else if (type == StructureType.RESOURCE) {
                        StructureResource resource = (StructureResource) tile.getStructure();
                        HexModel model = new HexModel(new ModelInstance(box));
                        model.move((float) loc.getX() + 0.5f, 0.3f, (float) loc.getY() - 0.5f);
                        renderTile.getStructures().add(model);
                    }
                }

                tile.setRenderTile(renderTile);
                /*modelCache.add(hexModel.getModelInstance());
                for (HexModel structure : renderTile.getStructures()) {
                    modelCache.add(structure.getModelInstance());
                }*/
            }
        }

         /*TileLocation loc = HexagonUtil.getTileLocation(0, 0);
        bigBox = new ModelInstance(box);
        bigBox.transform.translate((float) loc.getX() + 50, height, (float) loc.getY() + 50);
        bigBox.transform.rotate(0, 1, 0, 90);*/

        modelCache.end();
    }

    private void renderShadow(){
        shadowLight.begin(Vector3.Zero, camera.direction);
        shadowBatch.begin(shadowLight.getCamera());

        for (int x=0; x<currentMap.getTiles().length; x++) {
            for (int y=0; y<currentMap.getTiles()[x].length; y++) {
                RenderTile tile = currentMap.getTiles()[x][y].getRenderTile();
                HexModel model = tile.getModel();
                shadowBatch.render(model.getModelInstance());
                if (tile.getStructures().size() > 0) {
                    for (HexModel structure : tile.getStructures()) {
                        shadowBatch.render(structure.getModelInstance(), environment);
                    }
                }
            }
        }

        shadowBatch.render(selectedInstance, environment);

        shadowBatch.end();
        shadowLight.end();
    }

    private void renderModels(){
        modelBatch.begin(camera);

        for (int x=0; x<currentMap.getTiles().length; x++) {
            for (int y=0; y<currentMap.getTiles()[x].length; y++) {
                RenderTile tile = currentMap.getTiles()[x][y].getRenderTile();
                if (tile.getTileLocation().getX() < camera.position.x - 21
                        || tile.getTileLocation().getX() > camera.position.x + 21) {
                    continue;
                }
                if (tile.getTileLocation().getY() > camera.position.z + 5
                        || tile.getTileLocation().getY() < camera.position.z - 18) {
                    continue;
                }
                HexModel model = tile.getModel();
                modelBatch.render(model.getModelInstance(), environment);
                if (tile.getStructures().size() > 0) {
                    for (HexModel structure : tile.getStructures()) {
                        modelBatch.render(structure.getModelInstance(), environment);
                    }
                }
            }
        }

        for (ModelInstance debug : debugModels) {
            modelBatch.render(debug, environment);
        }

        for (int i=0; i<cars.size(); i++) {
            Car car = cars.get(i);
            car.update(0.0016f);
            modelBatch.render(car.getInstance(), environment);
        }

        if (selectedInstance.transform.getScaleX() > 1.05f) {
            selectedGrow = false;
        } else if (selectedInstance.transform.getScaleX() < 0.95f) {
            selectedGrow = true;
        }
        if (selectedGrow) {
            float x = selectedInstance.transform.getScaleX();
            float z = selectedInstance.transform.getScaleZ();
            selectedInstance.transform.setToTranslationAndScaling(
                    selectedInstance.transform.getTranslation(Vector3.Zero),
                    new Vector3(x + 0.0025f, 1, z + 0.0025f)
            );
        } else {
            float x = selectedInstance.transform.getScaleX();
            float z = selectedInstance.transform.getScaleZ();
            selectedInstance.transform.setToTranslationAndScaling(
                    selectedInstance.transform.getTranslation(Vector3.Zero),
                    new Vector3(x - 0.0025f, 1, z - 0.0025f)
            );
        }
        modelBatch.render(selectedInstance, environment);


        //modelBatch.render(modelCache, environment);

        modelBatch.end();
    }

    private void renderUI(){

        Gdx.gl.glEnable(GL20.GL_BLEND); // allows transparent drawing
        shapeRenderer.begin();
        windowManager.render(shapeRenderer);
        shapeRenderer.end();
    }

    private void renderDEBUGMETA(){
        batch.begin();
        font.draw(batch, "" + Gdx.graphics.getFramesPerSecond() + " FPS", 20, 20);
        batch.end();
    }

    @Override
    public void create() {


        gameManager = new GameManager(this);
        setupModels();
        setupCamera(new Point(0,6,0),new Point(0,0, -3),67,1,300);
        setupEviroment(
                new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f),
                new DirectionalLight().set(0.8f, 0.5f, 0.5f, 0f, -0.8f, -0.2f),
                new DirectionalShadowLight(1024*2, 1024*2, 60f*4, 60f*4, 1f, 300f)
                );

        inputGame = new InputGame(this);
        InputManager.getInstance().register(inputGame);
        gameManager.createAll();

    }

    @Override
    public void show() {
        super.show();
        createMap(MapManager.getInstance().getCurrentHexMap());

        selectedInstance = new ModelInstance(selectedModel);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        inputGame.update(delta);

        ScreenManager.getInstance().clearScreen(0.2f, 0.25f, 0.35f);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
        //Gdx.gl.glDisable(GL20.GL_BLEND); // disallow transparent drawing
        Gdx.gl.glEnable(GL20.GL_BLEND);


        renderShadow();
        renderModels();
        renderUI();
        renderDEBUGMETA();

        Main.engine.run(delta);



        stage.draw();
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
        selectedModel.dispose();
        text3D.dispose();
        decalBatch.dispose();

        if (cars.size() > 0) {
            cars.get(0).getInstance().model.dispose();
        }

        streetModel.dispose();
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    public DirectionalShadowLight getShadowLight() {
        return shadowLight;
    }

    /*public Map<TileLocation, RenderTile> getModelInstanceMap() {
        return modelInstanceMap;
    }*/

    public List<ModelInstance> getDebugModels() {
        return debugModels;
    }

    public HexMap getCurrentMap() {
        return currentMap;
    }

    public Model getTruckModel() {
        return truckModel;
    }

    public Model getStreetModel() {
        return streetModel;
    }
}
