package com.hexagon.game.util;

/**
 * Created by Sven on 14.02.2018.
 */

public class HexVector {

    private double x;
    private double y;

    public HexVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public HexVector rotate90() {
        double oldX = x;
        // noinspection SuspiciousNameCombination
        this.x = y;
        this.y = -oldX;
        return this;
    }

    public HexVector rotate180() {
        rotate90();
        rotate90();
        return this;
    }

    public HexVector rotate270() {
        double oldX = x;
        this.x = -y;
        // noinspection SuspiciousNameCombination
        this.y = oldX;
        return this;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public HexVector normalize() {
        double len = length();
        if (len == 0) {
            return this;
        }
        x /= len;
        y /= len;
        return this;
    }

    public HexVector subtract(HexVector other) {
        x -= other.x;
        y -= other.y;
        return this;
    }

    public HexVector add(HexVector other) {
        x += other.x;
        y += other.y;
        return this;
    }

    public HexVector multiply(double m) {
        x *= m;
        y *= m;
        return this;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public HexVector copy() {
        return new HexVector(x, y);
    }

    @Override
    public String toString() {
        return "HexVector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
