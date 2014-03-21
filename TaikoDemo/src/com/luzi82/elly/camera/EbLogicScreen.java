package com.luzi82.elly.camera;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.luzi82.elly.EbGame;
import com.luzi82.elly.EbScreen;
import com.luzi82.elly.draw.EbDraw;
import com.luzi82.elly.draw.EbSimpleDraw;
import com.luzi82.elly.logic.EbLogic;
import com.luzi82.elly.logic.EbObject;

public class EbLogicScreen<G extends EbGame, L extends EbLogic<L>> extends EbScreen<G> {

    public L                  mLogic;

    public OrthographicCamera mCamera;
    public EbCameraCalc       mCameraCalc;
    public EbCameraControl    mCameraControl;

    public List<EbDraw>       mDrawList;

    protected EbLogicScreen(G aParent) {
        super(aParent);
        mDrawList = new LinkedList<EbDraw>();
    }

    @Override
    public void onScreenRender(float aDelta) {
        final GL10 gl = Gdx.graphics.getGL10();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glDisable(GL10.GL_DEPTH_TEST);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        updateCamera(aDelta, gl);
        final EbObject<L>[] objList = mLogic.objectList();
        for (final EbDraw draw: mDrawList) {
            if (draw.mType == EbDraw.Type.STATIC) {
                draw.drawStatic(gl, this);
            } else if (draw.mType == EbDraw.Type.OBJECT) {
                draw.preDrawObject(gl);
                for (final EbObject<L> obj: objList) {
                    draw.drawObject(gl, obj);
                }
                draw.postDrawObject(gl);
            }
        }
    }

    @Override
    protected void onScreenLoadMember() {
        mCamera = new OrthographicCamera();
        mCameraCalc = new EbCameraCalc(mLogic.mWorldW, mLogic.mWorldH);
        mCameraControl = new EbCameraControl(mCameraCalc);

        for (final EbDraw draw: mDrawList) {
            draw.alloc();
        }
    }

    @Override
    public void onScreenResize() {
        mCameraCalc.onScreenResize(mScreenWidth, mScreenHeight);
        mCamera.viewportWidth = mCameraCalc.mViewPortW;
        mCamera.viewportHeight = mCameraCalc.mViewPortH;
    }

    public void addDraw(Class<?> aClass, String aFilename, float aSize) {
        addDraw(new EbSimpleDraw(aClass, aFilename, aSize));
    }

    public void addDraw(EbDraw aDraw) {
        mDrawList.add(aDraw);
    }

    private void updateCamera(float aDelta, GL10 aGl) {
        mCameraControl.update(aDelta);

        mCamera.zoom = mCameraCalc.iCameraRealZoom;
        mCamera.position.x = mCameraCalc.iCameraRealWX;
        mCamera.position.y = mCameraCalc.iCameraRealWY;
        mCamera.update();
        mCamera.apply(aGl);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button, long aTime) {
        // iLogger.debug("touchDown");

        // int minSide = Math.min(mScreenWidth, mScreenHeight);
        // float blockPerPixel = mCameraCalc.iCameraRealZoom / minSide;
        // if (blockPerPixel <= mBlockPerPixelBorder) {
        // if (!mCameraControl.mMoving) {
        // mCameraCalc.mLockTime = System.currentTimeMillis() + 1000;
        // }
        // } else {
        // mCameraControl.mMoving = true;
        // }

        mCameraControl.touchDown(x, y, pointer, button, aTime);

        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button, long aTime) {
        // iLogger.debug("touchUp");
        mCameraControl.touchUp(x, y, pointer, button, aTime);

        // if ((mCameraCalc.mLockTime >= 0) && (!mCameraControl.mMoving)) {
        // int minSide = Math.min(mScreenWidth, mScreenHeight);
        // float blockPerPixel = mCameraCalc.iCameraRealZoom / minSide;
        // if (blockPerPixel <= mBlockPerPixelBorder) {
        // int bx = (int) mCameraCalc.screenToBoardRealX(x);
        // int by = (int) mCameraCalc.screenToBoardRealY(y);
        // boolean good = true;
        // good = good && (bx >= 0);
        // good = good && (bx < Board.WIDTH);
        // good = good && (by >= 0);
        // good = good && (by < Board.HEIGHT);
        // if (good) {
        // if (mBoard.get0(bx, by)) {
        // mBoard.set(bx, by, false);
        // mBlockDraw.dirty(bx, by);
        // }
        // }
        // }
        // }

        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer, long aTime) {
        // iLogger.debug("touchDragged");
        mCameraControl.touchDragged(x, y, pointer, aTime);
        return true;
    }

    @Override
    public boolean touchMoved(int x, int y, long aTime) {
        // iLogger.debug("touchMoved");
        mCameraControl.touchMoved(x, y, aTime);
        return true;
    }

    @Override
    public boolean scrolled(int amount, long aTime) {
        mCameraControl.scrolled(amount, aTime);
        return true;
    }

}
