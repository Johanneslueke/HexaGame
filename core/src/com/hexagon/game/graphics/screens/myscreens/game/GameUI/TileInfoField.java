package com.hexagon.game.graphics.screens.myscreens.game.GameUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.ui.UILabel;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.graphics.ui.windows.Window;

/**
 * Created by Johannes on 25.02.2018.
 */

public class TileInfoField {

    public Window StatusWindow;
    public UILabel Title;

    public TileInfoField(GroupWindow window,Stage stage){

        StatusWindow = new Window(Gdx.graphics.getWidth()-255, 5,250, 200);
        Title = new UILabel(0,StatusWindow.getHeight(),StatusWindow.getWidth(),20,20,"TileInfo: ");

        StatusWindow.add(Title,stage);
        window.getWindowList().add(StatusWindow);
    }
}
