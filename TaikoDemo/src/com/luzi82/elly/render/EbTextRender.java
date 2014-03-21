package com.luzi82.elly.render;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;

public class EbTextRender {

    public Texture mTexture;
    public Mesh    mMesh;
    public float   mWidth;
    public float   mHeight;

    // public EbTextRender(String aText,float aSize){
    // }

    public void render(GL10 aGl) {
        aGl.glEnable(GL10.GL_BLEND);
        aGl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        aGl.glEnable(GL10.GL_TEXTURE_2D);
        aGl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        mTexture.bind();
        mMesh.render(GL10.GL_TRIANGLE_STRIP);
    }

    public interface Factory {
        public EbTextRender create(String aText, float aSize);
    }

}
