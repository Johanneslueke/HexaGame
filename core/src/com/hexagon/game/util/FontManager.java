package com.hexagon.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by Sven on 16.02.2018.
 */

public class FontManager {

    public static FileHandle handlePiximisa;

    public static void init() {
         handlePiximisa = Gdx.files.internal("fonts/Piximisa.ttf");
    }

}
