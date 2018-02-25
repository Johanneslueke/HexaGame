package com.hexagon.game.graphics.screens.myscreens.game.GameUI;

import com.badlogic.gdx.Gdx;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.graphics.ui.windows.Window;

/**
 * Created by Johannes on 25.02.2018.
 */

public class PlayerStatus {

    public Window       StatusWindow;

    public PlayerStatus(GroupWindow window){

        StatusWindow = new Window(5, 5,250,Gdx.graphics.getHeight()-60);

        window.getWindowList().add(StatusWindow);
    }

}
