package com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.ui.windows.AnimationWindow;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;

/**
 * Created by Johannes on 25.02.2018.
 */

public class Sidebar {

    public AnimationWindow statusWindow;



    public Sidebar(GroupWindow window, Stage stage) {

        statusWindow = new AnimationWindow(-255, 5, 5, 5, 250,Gdx.graphics.getHeight()-60, stage);

        window.getWindowList().add(statusWindow);
    }

    public void select(HexMap map, Point p, Stage stage) {
        statusWindow.show(stage);
    }

    public void deselect(Stage stage) {
        statusWindow.hide(stage);
    }


}
