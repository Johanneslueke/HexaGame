package com.hexagon.game.graphics.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.util.FontManager;

/**
 * Created by Johannes on 16.02.2018.
 */

public class UILabel extends UiElement {
    public com.badlogic.gdx.scenes.scene2d.ui.Label getLabel() {
        return Label;
    }

    private Label       Label;
    private LabelStyle  style;

    public UILabel(float x, float y, float width, float height, String text){
        super(x,y,width,height);

        long start = System.currentTimeMillis();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(FontManager.handlePiximisa);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        BitmapFont font32 = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        style = new LabelStyle();
        style.font = font32;

        Label = new Label(text, style);
        Label.setX(x);
        Label.setY(y);
    }

    public UILabel( float x, float y, float width, float height, Stage stage,String text, ChangeListener changeListener) {
        this( x, y, width, height,text);

        addToStage(stage);
        addListener(changeListener);
    }


    @Override
    public void addToStage(Stage stage) {
        stage.addActor(this.Label);
    }

    @Override
    public void removeFromStage(Stage stage) {
        stage.getActors().removeValue(this.Label, false);
    }

    @Override
    public void show(Stage stage) {
        this.Label.setVisible(true);
    }

    @Override
    public void hide(Stage stage) {
        this.Label.setVisible(false);
    }

    public void addListener(ChangeListener listener) {
        this.Label.addListener(listener);
    }

    @Override
    public void setDisplayX(float x) {
        super.setDisplayX(x);
        Label.setX(x);
    }

    @Override
    public void setDisplayY(float y) {
        super.setDisplayX(y);
        Label.setY(y);
    }
}
