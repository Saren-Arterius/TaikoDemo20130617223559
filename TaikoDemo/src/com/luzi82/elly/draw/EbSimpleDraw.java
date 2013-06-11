package com.luzi82.elly.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.luzi82.elly.logic.EbObject;

public class EbSimpleDraw extends EbDraw {

	private Texture mTexture;
	private Mesh mUnitMesh;

	Class<?> mClass;
	String mFilename;
	float mSize;

	public EbSimpleDraw(Class<?> aClass, String aFilename, float aSize) {
		super(Type.OBJECT);
		mClass = aClass;
		mFilename = aFilename;
		mSize = aSize;
	}

	public void preDrawObject(GL10 aGl) {
		aGl.glDisable(GL10.GL_BLEND);
		aGl.glBlendFunc(GL10.GL_ONE, GL10.GL_ZERO);
		aGl.glEnable(GL10.GL_TEXTURE_2D);
		aGl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public void drawObject(GL10 aGl, EbObject<?> aObject) {
		if (!mClass.isInstance(aObject))
			return;
		aGl.glPushMatrix();
		if (aObject.mPos2 != null) {
			aGl.glTranslatef(aObject.mPos2.mX, aObject.mPos2.mY, 0);
		}
		aGl.glScalef(mSize, mSize, mSize);
		mTexture.bind();
		mUnitMesh.render(GL10.GL_TRIANGLE_STRIP);
		aGl.glPopMatrix();
	}

	public void alloc() {
		if (mTexture == null) {
			Pixmap tmpPixmap;
			tmpPixmap = new Pixmap(Gdx.files.internal("data/chitanda0.png"));
			mTexture = new Texture(tmpPixmap, true);
			tmpPixmap.dispose();
		}
		if (mUnitMesh == null) {
			VertexAttributes va;
			va = new VertexAttributes(new VertexAttribute(
					VertexAttributes.Usage.Position, 3, "position"),
					new VertexAttribute(
							VertexAttributes.Usage.TextureCoordinates, 2,
							"texturecoordinates"));
			mUnitMesh = new Mesh(true, 4, 4, va);
			mUnitMesh.setVertices(new float[] { //
					-0.5f, 0.5f, 0f, 0f, 0f,//
							0.5f, 0.5f, 0f, 1f, 0f,//
							-0.5f, -0.5f, 0f, 0f, 1f,//
							0.5f, -0.5f, 0f, 1f, 1f,//
					});
		}
	}

	public void free() {
		if (mTexture != null) {
			mTexture.dispose();
		}
		mTexture = null;
		if (mUnitMesh != null) {
			mUnitMesh.dispose();
		}
		mUnitMesh = null;
	}

}
