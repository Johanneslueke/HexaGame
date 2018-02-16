package com.hexagon.game.graphics.screens.myscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.ui.UiImage;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.graphics.ui.windows.FadeWindow;
import com.hexagon.game.graphics.ui.windows.StandardWindow;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.MapManager;
import com.hexagon.game.map.structures.Structure;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.map.structures.resources.ResourceType;
import com.hexagon.game.map.structures.resources.StructureResource;
import com.hexagon.game.map.tiles.Biome;
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.map.generator.GeneratorCallback;
import com.hexagon.game.map.generator.MapGenerator;
import com.hexagon.game.map.generator.TileGenerator;
import com.hexagon.game.util.MenuUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Sven on 14.12.2017.
 */

public class ScreenGenerator extends HexagonScreen {

    private SpriteBatch batch;
    private BitmapFont font;

    private UiButton buttonProgress;

    public ScreenGenerator() {
        super(ScreenType.GENERATOR);
    }

    private List<TileGenerator> setupBiomeGenerator(final MapGenerator mapGenerator){

        // Tile Generators
        List<TileGenerator> res = new ArrayList<>();

        //biomeGenerator
        res.add(new TileGenerator() {
            @Override
            public Tile generate(Tile tile, int x, int y, Random random) {
                if (x == 0 && y == 0) {
                    return tile;
                }
                Biome biomeLast;
                if (y > 0) {
                    biomeLast = mapGenerator.getGeneratedTiles()[x][y - 1].getBiome();
                } else {
                    biomeLast = mapGenerator.getGeneratedTiles()[x - 1][y].getBiome();
                }
                if (biomeLast != Biome.WATER
                        && biomeLast != Biome.ICE
                        && random.nextInt(100) < 20) {
                    tile.setBiome(biomeLast);
                } else {
                    int r = random.nextInt(2);
                    if (r == 0) {
                        tile.setBiome(Biome.DESERT);
                    } else if (r == 1) {
                        tile.setBiome(Biome.PLAINS);
                    }
                }
                return tile;
            }
        });

        // Add the ice generator last! Highest Priority == called last
        //iceGenerator
        res.add( new TileGenerator() {
            @Override
            public Tile generate(Tile tile, int x, int y, Random random) {
                if (x < 5 || x > mapGenerator.getSizeX() - 5) {
                    if (x <= 2 || x >= mapGenerator.getSizeX() - 2) {
                        tile.setBiome(Biome.ICE);
                    } else if (tile.getBiome() != Biome.ICE){
                        tile.setBiome(Biome.WATER);
                    }
                }
                if (y < 5 || y > mapGenerator.getSizeY() - 5) {
                    if (y <= 2 || y >= mapGenerator.getSizeY() - 2) {
                        tile.setBiome(Biome.ICE);
                    } else if (tile.getBiome() != Biome.ICE){
                        tile.setBiome(Biome.WATER);
                    }
                }

                return tile;
            }
        });

        //resourceGenerator
        res.add( new TileGenerator() {
            @Override
            public Tile generate(Tile tile, int x, int y, Random random) {
                if (tile.getBiome() != Biome.PLAINS) {
                    return tile;
                }
                if (tile.getStructure() != null) {
                    return tile;
                }
                if (random.nextInt(100) <= 20) {
                    int r = random.nextInt(ResourceType.values().length - 1);
                    ResourceType resourceType = ResourceType.values()[r];
                    tile.setStructure(new StructureResource(resourceType));
                }
                return tile;
            }
        });

        //treeGenerator
        res.add( new TileGenerator() {
            @Override
            public Tile generate(Tile tile, int x, int y, Random random) {
                if (tile.getBiome() != Biome.PLAINS) {
                    // Trees can only spawn on grassland
                    return tile;
                }
                if (tile.getStructure() != null) {
                    return tile;
                }
                if (random.nextInt(100) <= 80) {
                    tile.setStructure(new Structure(StructureType.FOREST));
                }
                return tile;
            }
        });

        return res;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        final UiButton button = new UiButton("<= Back", 50, Gdx.graphics.getHeight() - 50, 100, 50);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().setCurrentScreen(ScreenType.MAIN_MENU);
            }
        });

        button.addToStage(stage);

        /*
         * Main Generator Window
         */

        final StandardWindow standardWindow = new StandardWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 800, 600, stage);

        FadeWindow fadeWindow = new FadeWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 800, 600, stage);
        fadeWindow.show(stage);
        fadeWindow.add(new UiImage(0, 0, 800, 600, "window.png"), stage);
        standardWindow.getWindowList().add(fadeWindow);

        buttonProgress = new UiButton("0%", 40, fadeWindow.getHeight() - 60, 100, 40);

        fadeWindow.add(buttonProgress, stage);

        fadeWindow.updateElements();

        this.windowManager.getWindowList().add(standardWindow);

    }

    @Override
    public void show() {
        super.show();

        System.out.println("Showing generator");

        /*
         * Start creating the world
         */

        final MapGenerator mapGenerator = new MapGenerator(100, 20, 2);
        List<TileGenerator> Biomes = setupBiomeGenerator(mapGenerator);
        for(TileGenerator generator : Biomes)
            mapGenerator.getTileGeneratorList().add(generator);



        mapGenerator.setCallback(new GeneratorCallback() {
            @Override
            public void generatorFinished() {
                buttonProgress.getTextButton().setText("100%");

                final HexMap hexMap = new HexMap(mapGenerator.getSizeX(), mapGenerator.getSizeY());
                hexMap.setTiles(mapGenerator.getGeneratedTiles());

                MapManager.getInstance().setCurrentHexMap(hexMap);

                ScreenManager.getInstance().setCurrentScreen(ScreenType.GAME);
            }
        });

        mapGenerator.startGenerating();
    }

    @Override
    public void render(float delta) {
        ScreenManager.getInstance().clearScreen(0.2f, 0.25f, 0.35f);
        batch.begin();
        font.draw(batch, "Generator", 20, 20);
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
