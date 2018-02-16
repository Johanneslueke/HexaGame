package com.hexagon.game.util;


import com.hexagon.game.map.Hexagon;
import com.hexagon.game.map.Point;
import com.hexagon.game.map.TileLocation;

/**
 * Created by Sven on 08.12.2017.
 */

public class HexagonUtil {

    public static Hexagon hexagon = new Hexagon(1, 0.85);//new Hexagon(90.5*0.02, 54*0.02);

    /**
     * get the tiles location from the array indexes
     * @param indexX
     * @param indexY
     * @return
     */
    public static TileLocation getTileLocation(int indexX, int indexY) {
        double x;
        double y;
        if ((indexX & 1) == 1) {
            // if indexY is odd
            x = (double) indexX * hexagon.getSideLengthX()*1.5;
            y = indexY * (hexagon.getSideLengthY()) * 2 + hexagon.getSideLengthY();
        } else {
            // if indexY is even
            x = (double) indexX * hexagon.getSideLengthX()*1.5;
            y = indexY * (hexagon.getSideLengthY()) * 2;
        }
        return new TileLocation(x, y);
    }

    /**
     * Get the array indexes from the tiles location
     * @param tileLocation
     * @return
     */
    public static Point getArrayLocation(TileLocation tileLocation) {
        int sectX = (int) (tileLocation.getX() / (1.5 * hexagon.getSideLengthX()));
        int sectY = (int) (tileLocation.getY() / (2 * hexagon.getSideLengthY()));

        int sectPixelX = (int) (tileLocation.getX() % (1.5 * hexagon.getSideLengthX()));

        Point point = new Point(sectX, sectY);

        double gradient = hexagon.getSideLengthY() / hexagon.getSideLengthX();

        if ((sectX & 1) == 0) {
            int sectPixelY = (int) (tileLocation.getY() % (2 * hexagon.getSideLengthY()));
            //System.out.println(tileLocation.getX() + "///" + tileLocation.getY() + "    " + sectX + ", " + sectY + "; gradient: " + gradient + ", sx, sy " + sectPixelX + ", " + sectPixelY);
            if (sectPixelY < (-sectPixelX + hexagon.getSideLengthY())) {
                // bottom left edge
                //System.out.println("bottom left " + (-(sectPixelX + hexagon.getSideLengthY())));
                point.setX(sectX - 1);
                point.setY(sectY - 1);
            } else if (sectPixelY > (sectPixelX + hexagon.getSideLengthY())) {
                // top left edge
                //System.out.println("top left " + ((sectPixelX + hexagon.getSideLengthY())));
                point.setX(sectX - 1);
                point.setY(sectY);
            }
            return point;
        }
        int sectPixelY = (int) ((tileLocation.getY() + hexagon.getSideLengthY()) % (2 * hexagon.getSideLengthY()));
        sectY = (int) ((tileLocation.getY() - hexagon.getSideLengthY()) / (2 * hexagon.getSideLengthY()));
        //System.out.println(tileLocation.getX() + "///" + tileLocation.getY() + "    " + sectX + ", " + sectY + "; gradient: " + gradient + ", sx, sy " + sectPixelX + ", " + sectPixelY);
        point.setY(sectY);
        if (sectPixelY < (-sectPixelX + hexagon.getSideLengthY())) {
            // bottom left edge
            //System.out.println("bottom left " + (-(sectPixelX + hexagon.getSideLengthY())));
            point.setX(sectX - 1);
            point.setY(sectY);
        } else if (sectPixelY > (sectPixelX + hexagon.getSideLengthY())) {
            // top left edge
            //System.out.println("top left " + ((sectPixelX + hexagon.getSideLengthY())));
            point.setX(sectX - 1);
            point.setY(sectY + 1);
        }
        return point;
    }

}
