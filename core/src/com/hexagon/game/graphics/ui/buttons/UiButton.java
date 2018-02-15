package com.hexagon.game.graphics.ui.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.ui.UiElement;

/**
 * Created by Sven on 18.12.2017.
 */

public class UiButton extends UiElement {

    private TextButton textButton;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;
    private Skin skin;

    public UiButton(String text, float x, float y, float width, float height) {
        super(x, y, width, height);
        //font = new BitmapFont();
        //font.getData().setScale(1.1f);
        //skin = new Skin();


        long start = System.currentTimeMillis();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Piximisa.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        BitmapFont font12 = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font12;


        textButton = new TextButton(text, textButtonStyle);
        textButton.setX(x);
        textButton.setY(y);
        System.out.println("Time: " + (System.currentTimeMillis() - start));
    }

    public UiButton(String text, float x, float y, float width, float height, Stage stage, ChangeListener changeListener) {
        this(text, x, y, width, height);

        addToStage(stage);
        addListener(changeListener);
    }

    public TextButton getTextButton() {
        return textButton;
    }

    @Override
    public void addToStage(Stage stage) {
        stage.addActor(this.textButton);
    }

    @Override
    public void show(Stage stage) {
        this.textButton.setVisible(true);
    }

    @Override
    public void hide(Stage stage) {
        this.textButton.setVisible(false);
    }

    public void addListener(ChangeListener listener) {
        this.textButton.addListener(listener);
    }

    @Override
    public void setDisplayX(float x) {
        super.setDisplayX(x);
        textButton.setX(x);
    }

    @Override
    public void setDisplayY(float y) {
        super.setDisplayX(y);
        textButton.setY(y);
    }
}
