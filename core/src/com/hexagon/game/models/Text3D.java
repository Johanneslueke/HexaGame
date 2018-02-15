package com.hexagon.game.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Created by Sven on 13.02.2018.
 */

public class Text3D {

    private Model       boxModel; // For testing only
    private Pixmap      pixmap;
    private BitmapFont  font;

    public Text3D() {
        font = new BitmapFont(true);
        font.getData().scale(20);

        


    }

    public Decal create() {
        ModelBuilder builder = new ModelBuilder();

        Texture texture = createTexture(new Color(1.0f, 0, 0, 1.0f), new Color(0.3f, 0, 0, 0.0f));

        /*TextureAttribute textureAttribute = TextureAttribute.createDiffuse(texture);
        Material material = new Material(textureAttribute,
                ColorAttribute.createSpecular(1, 1, 1, 1),
                FloatAttribute.createShininess(8f));

        long attributes = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates;

        boxModel = builder.createBox(0.0001f, 1f, 1f, material, attributes);
*/

        /*ModelInstance instance = new ModelInstance(boxModel);
        instance.transform.rotate(0, 1, 0, 180);
        instance.transform.rotate(0, 0, 1, 90);*/


        Decal decal = Decal.newDecal(new TextureRegion(texture), true);
        decal.setScale(0.002f);

        return decal;
    }

    public void dispose() {
        boxModel.dispose();
    }

    private Texture createTexture(Color fg, Color bg) {
        Pixmap pm = render(fg, bg);
        return new Texture(pm);
    }

    private Pixmap render(Color fg, Color bg) {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        SpriteBatch spriteBatch = new SpriteBatch();

        FrameBuffer fbo = FrameBuffer.createFrameBuffer(Pixmap.Format.RGB565, width, height, true);
        fbo.begin();

        Gdx.gl.glEnable(GL20.GL_BLEND); // allows transparent drawing
        Gdx.gl.glClearColor(0, 1.0f, 0, 1.0f);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Matrix4 normalProj = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.setProjectionMatrix(normalProj);

        spriteBatch.enableBlending();
        spriteBatch.setColor(new Color(0, 0, 0, 1.0f));
        spriteBatch.begin();

        //font.getData().setScale(25, 20);
        font.draw(spriteBatch, "Abcdefg", 10, height-500);
        spriteBatch.end();

        pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, width, height);
        fbo.end();
        fbo.dispose();
        spriteBatch.dispose();

        return pixmap;
    }
}
