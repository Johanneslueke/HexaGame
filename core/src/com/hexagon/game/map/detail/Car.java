package com.hexagon.game.map.detail;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.myscreens.game.ScreenGame;
import com.hexagon.game.map.TileLocation;
import com.hexagon.game.map.structures.Structure;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.models.HexModel;
import com.hexagon.game.models.RenderTile;
import com.hexagon.game.util.AStar;
import com.hexagon.game.util.HexVector;
import com.hexagon.game.util.HexagonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sven on 14.02.2018.
 */

public class Car {

    private List<Car>   collisionCars = new ArrayList<>();
    private Tile        lastTile;

    private double MAX_SPEED = 2f;

    private int                     i = 0;
    private List<CarTile>           path = new ArrayList<>();
    private ModelInstance           instance;
    private TileLocation            currentLocation;

    private double                  velocity = 0;

    private boolean forward = true;

    public Car(List<AStar.ATile> aTiles) {
        Model boxModel2 = new ModelBuilder().createBox(0.15f, 0.15f, 0.15f,
                new Material(ColorAttribute.createDiffuse(0.7f, 0.3f, 0.3f, 1)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        ScreenGame game = (ScreenGame) ScreenManager.getInstance().getCurrentScreen();

        HexVector before = null;
        for (AStar.ATile aTile : aTiles) {
            TileLocation location = HexagonUtil.getTileLocation(aTile.getX(), aTile.getY());
            HexVector vector = new HexVector(location.getX(), location.getY());

            Tile tile = game.getCurrentMap().getTileAt(aTile.getX(), aTile.getY());

            if (before != null) {
                HexVector diff = vector.copy().subtract(before).normalize();

                // Create street models along the path
                //Tile tile = game.getCurrentMap().getTileAt(aTile.getX(), aTile.getY());
                tile.setStructure(new Structure(StructureType.STREET));
                RenderTile renderTile = tile.getRenderTile();
                if (renderTile == null) {
                    throw new RuntimeException("RenderTile is null");
                }
                if (renderTile.getStructures() != null) {
                    renderTile.getStructures().clear();
                } else {
                    renderTile.setStructures(new ArrayList<HexModel>());
                }
                HexModel street = new HexModel(new ModelInstance(game.getStreetModel()));
                street.move((float) location.getX(), 0.01f, (float) location.getY());
                renderTile.getStructures().add(street);


                HexVector right = diff.copy().rotate270().multiply(0.2).add(vector);

                ModelInstance box = new ModelInstance(boxModel2);
                box.transform.translate((float) right.getX(), 0, (float) right.getY());
                game.getDebugModels().add(box);

                path.add(
                    new CarTile(
                        new TileLocation(right.getX(), right.getY()),
                        tile
                    )
                );
                //path.add(tile);
                //path.add(new TileLocation(right.getX(), right.getY()));
            } else {
                //path.add(location);
                path.add(
                        new CarTile(location, tile)
                );
            }

            before = vector;
        }


        Collections.reverse(aTiles);

        boxModel2 = new ModelBuilder().createBox(0.15f, 0.15f, 0.15f,
                new Material(ColorAttribute.createDiffuse(0.3f, 0.7f, 0.3f, 1)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        before = null;
        for (AStar.ATile aTile : aTiles) {
            TileLocation location = HexagonUtil.getTileLocation(aTile.getX(), aTile.getY());
            HexVector vector = new HexVector(location.getX(), location.getY());

            Tile tile = game.getCurrentMap().getTileAt(aTile.getX(), aTile.getY());
            if (before != null) {
                HexVector diff = vector.copy().subtract(before).normalize();
                HexVector left = diff.copy().rotate270().multiply(0.2).add(vector);

                ModelInstance box = new ModelInstance(boxModel2);
                box.transform.translate((float) left.getX(), 0, (float) left.getY());
                game.getDebugModels().add(box);
                //path.add(new TileLocation(left.getX(), left.getY()));
                path.add(
                        new CarTile(
                                new TileLocation(left.getX(), left.getY()),
                                tile
                        )
                );
            } else {
                path.add(
                        new CarTile(location, tile)
                );
            }



            before = vector;
        }



        Model boxModel = new ModelBuilder().createBox(0.2f, 0.2f, 0.2f,
                new Material(ColorAttribute.createDiffuse(0.3f, 0.3f, 0.6f, 1)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        currentLocation = path.get(0).location.copy();
        instance = new ModelInstance(boxModel);
        instance.transform.translate((float) currentLocation.getX(), 0, (float) currentLocation.getY());

        MAX_SPEED = Math.random() + 1.0;

    }

    private float ignoreCollision = 0;

    public void update(float delta) {

        Tile tile = path.get(i).tile;
        TileLocation destination = path.get(i).location;

        boolean collided = false;
        if (ignoreCollision > 0) {
            ignoreCollision -= delta;
        }
        if (ignoreCollision < 1.0f) {
            collided = isCollided(tile);
            if (!collided && i < path.size() - 1) {
                collided = isCollided(path.get(i + 1).tile);
            }
            if (collided) {
                ignoreCollision += delta + 0.01;
            }
        }


        double deltaX = destination.getX() - currentLocation.getX();
        double deltaY = destination.getY() - currentLocation.getY();
        double len = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (len != 0) {
            deltaX = deltaX / len;
            deltaY = deltaY / len;

            if (collided) {
                if (velocity > 0) {
                    velocity *= 0;
                }
            } else {
                if (velocity <= MAX_SPEED) {
                    velocity *= 1.01;
                    velocity += 0.01;
                    if (velocity > MAX_SPEED) {
                        velocity = MAX_SPEED;
                    }
                }
            }

            deltaX *= delta * velocity;
            deltaY *= delta * velocity;

            currentLocation.setX(currentLocation.getX() + deltaX);
            currentLocation.setY(currentLocation.getY() + deltaY);

            //System.out.println("Going to " + destination.getX() + ", " + destination.getY() + " /// " + currentLocation.getX() + ", " + currentLocation.getY());

            instance.transform.translate((float) deltaX, 0, (float) deltaY);
        }

        if (currentLocation.distanceSquared(destination) <= 0.1 * 0.1) {
            /*if (forward) {
                i++;
                if (i >= path.size()) {
                    forward = false;
                    i = path.size()-1;
                }
            } else {
                i--;
                if (i < 0) {
                    forward = true;
                    i = 0;
                }
            }*/
            i++;
            if (i >= path.size()) {
                i = 0;
                velocity = 0;
            }

            lastTile = tile;
            Tile nextTile = path.get(i).tile;
            tile.getCars().remove(this);
            nextTile.getCars().add(this);
        }
    }

    public boolean isCollided(Tile tile) {
        collisionCars.clear();
        collisionCars.addAll(tile.getCars());
        if (lastTile != null) {
            collisionCars.addAll(lastTile.getCars());
        }
        for (int i=0; i<collisionCars.size(); i++) {
            Car otherCar = collisionCars.get(i);
            if (otherCar.equals(this)) {
                continue;
            }
            double distance = otherCar.getCurrentLocation().distanceSquared(this.currentLocation);
            if (distance <= 0.25*0.25) {
                /*HexVector vector = new HexVector(
                        otherCar.getCurrentLocation().getX() - this.currentLocation.getX(),
                        otherCar.getCurrentLocation().getY() - this.currentLocation.getY()
                );*/
                // Only collide with cars that come from the right
                //if (vector.getX() > 0.1) {
                if (distance > 0.05) {
                    return true;
                }
                ignoreCollision = 2.0f;
                //}
            }
        }
        return false;
    }

    public ModelInstance getInstance() {
        return instance;
    }

    public TileLocation getCurrentLocation() {
        return currentLocation;
    }
}
