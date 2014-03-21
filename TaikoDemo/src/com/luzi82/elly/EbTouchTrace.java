package com.luzi82.elly;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.Disposable;

public class EbTouchTrace implements Disposable {

    private final Texture            mTexture = new Texture(Gdx.files.internal("data/circle_mark.png"));
    private final Mesh               mMesh;
    private final OrthographicCamera mCamera;

    private final LinkedList<Record> mRecordL = new LinkedList<EbTouchTrace.Record>();

    private final float              TIMEOUT  = 0.25f;

    public EbTouchTrace() {
        VertexAttributes va;
        va = new VertexAttributes( //
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "position"),//
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "texturecoordinates")//
        );

        mMesh = new Mesh(true, 4, 4, va);
        mMesh.setVertices(new float[] { //
        -0.5f, 0.5f, 0f, 0f, 1f,//
                0.5f, 0.5f, 0f, 1f, 1f,//
                -0.5f, -0.5f, 0f, 0f, 0f,//
                0.5f, -0.5f, 0f, 1f, 0f,//
        });
        mMesh.setIndices(new short[] {0, 1, 2, 3});

        mCamera = new OrthographicCamera();
        mCamera.zoom = 1f;
    }

    public void touchDown(int x, int y, int pointer, int button) {
        mRecordL.addLast(new Record(System.currentTimeMillis(), x, y, pointer, Type.DOWN));
    }

    public void touchUp(int x, int y, int pointer, int button) {
        mRecordL.addLast(new Record(System.currentTimeMillis(), x, y, pointer, Type.UP));
    }

    public void touchDragged(int x, int y, int pointer) {
        mRecordL.addLast(new Record(System.currentTimeMillis(), x, y, pointer, Type.DRAG));
    }

    public void touchMoved(int x, int y) {
        mRecordL.addLast(new Record(System.currentTimeMillis(), x, y, -1, Type.MOVE));
    }

    public void render() {
        final GL10 gl = Gdx.graphics.getGL10();
        final float cm = Gdx.graphics.getPpcX();
        final long now = System.currentTimeMillis();

        final float screenW = Gdx.graphics.getWidth();
        final float screenH = Gdx.graphics.getHeight();

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL10.GL_TEXTURE_2D);

        mCamera.viewportWidth = screenW;
        mCamera.viewportHeight = screenH;
        mCamera.position.x = screenW / 2;
        mCamera.position.y = screenH / 2;
        mCamera.update();
        mCamera.apply(gl);

        mTexture.bind();

        final LinkedList<Record> remove = new LinkedList<EbTouchTrace.Record>();

        for (final Record r: mRecordL) {
            final float timeDiff = (now - r.mTime) / 1000f;
            if (timeDiff > TIMEOUT) {
                remove.add(r);
                continue;
            }
            gl.glPushMatrix();
            gl.glTranslatef(r.mX, screenH - r.mY, 0);
            final float radius = (1f + timeDiff * (Const.PHI - 1) / TIMEOUT) * cm * 2;
            gl.glScalef(radius, radius, 1);
            gl.glColor4f(r.mType.mR, r.mType.mG, r.mType.mB, 1 - (timeDiff / TIMEOUT));
            mMesh.render(GL10.GL_TRIANGLE_STRIP);
            gl.glPopMatrix();
        }
        mRecordL.removeAll(remove);
    }

    @Override
    public void dispose() {
        EbDeepDispose.disposeMember(this, super.getClass());
    }

    enum Type {
        DOWN(0f, 1f, 0f), //
        UP(1f, 0f, 0f), //
        DRAG(0f, 0f, 1f), //
        MOVE(0.5f, 0.5f, 0.5f);
        float mR;
        float mG;
        float mB;

        Type(float aR, float aG, float aB) {
            mR = aR;
            mG = aG;
            mB = aB;
        }
    }

    class Record {
        final long mTime;
        final int  mX;
        final int  mY;
        final int  mPointer;
        final Type mType;

        Record(long aTime, int aX, int aY, int aPointer, Type aType) {
            mTime = aTime;
            mX = aX;
            mY = aY;
            mPointer = aPointer;
            mType = aType;
        }
    }

}
