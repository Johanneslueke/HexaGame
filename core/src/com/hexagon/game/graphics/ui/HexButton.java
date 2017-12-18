package com.hexagon.game.graphics.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by Sven on 18.12.2017.
 */

public class HexButton {

    private TextButton textButton;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;
    private Skin skin;

    public HexButton(String text, float x, float y) {
        font = new BitmapFont();
        skin = new Skin();

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        textButton = new TextButton(text, textButtonStyle);
        textButton.setX(x);
        textButton.setY(y);
    }

    public TextButton getTextButton() {
        return textButton;
    }

    public void addToStage(Stage stage) {
        stage.addActor(this.textButton);
    }

    public void addListener(ChangeListener listener) {
        this.textButton.addListener(listener);
    }
}
