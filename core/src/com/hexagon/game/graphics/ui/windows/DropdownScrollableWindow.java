package com.hexagon.game.graphics.ui.windows;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hexagon.game.graphics.ui.UiElement;
import com.hexagon.game.input.InputUtil;

/**
 * Created by Sven on 19.12.2017.
 *
 * Orders the added ui Elements neatly
 *
 */

public class DropdownScrollableWindow extends DropdownWindow {

    private int currentLine = 0;
    private int displayAmount = 1; // the amount of lines to display
    private int maxLines = 0; // the max amount of lines this window has
    private int columns;

    public DropdownScrollableWindow(float windowX, float windowY, float width, float height, float marginLeftRight, float marginTopBottom, int displayAmount) {
        super(windowX, windowY, width, height, marginLeftRight, marginTopBottom); // width and height will be set later
        if (width == 0 && height == 0) {
            autoSize = true;
        }
        this.displayAmount = displayAmount;
    }

    public void orderAllNeatly(int columns, int startLine, int stopLine) {
        if (elementList.isEmpty()) {
            return;
        }
        float maxX = 0;
        UiElement lastElement = null;
        int counter = 0;

        this.columns = columns;


        startLine *= columns;
        stopLine *= columns;
        stopLine += columns;

        if (startLine >= elementList.size()) {
            startLine = elementList.size() - 1;
        }
        if (stopLine > elementList.size()) {
            stopLine = elementList.size();
        }

        for (UiElement element : elementList) {
            element.setDisplayX(-100);
            element.setDisplayY(-100);
        }
        for (int i=startLine; i<stopLine; i++) {
            if (i < 0 || i >= elementList.size()) {
                continue;
            }
            UiElement element = elementList.get(i);
            int row = counter / columns;
            int col = counter % columns;

            counter++;

            element.setX(col * element.getWidth() + 10);
            element.setY(row * element.getHeight() + 10);

            maxX = Math.max(maxX, element.getX());

            element.setDisplayX(getX() + element.getX());
            element.setDisplayY(getY() + element.getY());

            lastElement = element;
        }
        maxLines = elementList.size()/columns;
        if (lastElement == null) {
            return;
        }
        if (autoSize) {
            UiElement firstElement = elementList.get(0);
            setWidth(maxX + lastElement.getWidth() + 20 - firstElement.getX());
            setHeight(lastElement.getY() + lastElement.getHeight() + 20 - firstElement.getY());
        }


    }

    public boolean scroll(int amount) {
        float mouseX = InputUtil.mouseX();
        float mouseY = InputUtil.mouseY();

        if (mouseX > getX() + getWidth()
                || mouseX < getX()
                || mouseY < getY()
                || mouseY > getY() + getHeight()) {
            return false;
        }
        amount *= -1;
        currentLine += amount;
        if (currentLine < 0) {
            currentLine = 0;
        } else if (currentLine > maxLines - displayAmount) {
            currentLine = maxLines - displayAmount;
        }
        orderAllNeatly(this.columns, currentLine, currentLine + this.displayAmount);
        return true;
    }

    @Override
    public void render(ShapeRenderer renderer) {
        if (!isVisible()) {
            return;
        }
        super.render(renderer);
        float scrollbarHeight = (1 / (float) (maxLines-displayAmount+1) * getHeight()) + 1;
        float scrollbarPos = (getY() + ((int) ((currentLine / (float) (maxLines-displayAmount+1)) * getHeight())));
        renderer.setColor(1f, 0f, 0f, 1f);
        renderer.rect(getX() + getWidth() - 8, scrollbarPos, 8, scrollbarHeight);
    }
}
