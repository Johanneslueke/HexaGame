package com.hexagon.game.graphics.screens.myscreens.game.GameUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.ui.windows.AnimationWindow;
import com.hexagon.game.graphics.ui.windows.GroupWindow;

/**
 * Created by Johannes on 25.02.2018.
 */

public class PlayerStatus {

    public AnimationWindow StatusWindow;

    public PlayerStatus(GroupWindow window, Stage stage) {

        StatusWindow = new AnimationWindow(-255, 5, 5, 5, 250,Gdx.graphics.getHeight()-60, stage);

        window.getWindowList().add(StatusWindow);
    }

}
