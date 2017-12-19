package com.hexagon.game.graphics.ui.windows;

import com.hexagon.game.graphics.ui.UiElement;

/**
 * Created by Sven on 19.12.2017.
 *
 * Orders the added ui Elements neatly
 *
 */

public class DropdownWindow extends Window {


    public DropdownWindow(float windowX, float windowY, int rows, int columns, float marginLeftRight, float marginTopBottom) {
        super(windowX, windowY, 0, 0); // width and height will be set later

        for (int i=0; i<elementList.size(); i++) {
            UiElement element = elementList.get(i);
            int row = i / rows;
            int col = i % columns;

            element.setX(col * element.getWidth() + 10);
            element.setY(row * element.getHeight() + 10);
        }

    }
}
