package com.hexagon.game.map;

/**
 * Created by Sven on 08.12.2017.
 */

public class Hexagon {

    private double sideLengthX;
    private double sideLengthY;

    public Hexagon(double sideLengthX, double sideLengthY) {
        this.sideLengthX = sideLengthX;
        this.sideLengthY = sideLengthY;
    }

    public double getSideLengthX() {
        return sideLengthX;
    }

    public void setSideLengthX(double sideLengthX) {
        this.sideLengthX = sideLengthX;
    }

    public double getSideLengthY() {
        return sideLengthY;
    }

    public void setSideLengthY(double sideLengthY) {
        this.sideLengthY = sideLengthY;
    }
}
