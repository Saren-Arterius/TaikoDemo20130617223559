package com.luzi82.elly.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Logger;
import com.luzi82.elly.Const;

public class EbCameraControl {

    protected Logger          iLogger              = new Logger(this.getClass().getSimpleName(), Logger.DEBUG);

    public EbCameraCalc       mCameraCalc;

    public static final float SMOOTH_REDUCE        = 1f / 256;
    public static final float DIV_LN_SMOOTH_REDUCE = (float) (1 / Math.log(EbCameraControl.SMOOTH_REDUCE));

    // moving
    public boolean            mMoving;

    // touch
    public static final int   TOUCH_MAX            = 16;
    private boolean           mTouchCountChange;
    private boolean           mTouchChange;
    private final boolean[]   mTouching;
    private final int[]       mTouchSX;
    private final int[]       mTouchSY;
    private final int[]       mTouchStartSX;
    private final int[]       mTouchStartSY;
    private float             mTouchStartWXAvg;
    private float             mTouchStartWYAvg;
    private float             mTouchStartSDiff;
    private float             mTouchStartCameraZoom;

    // mouse
    private int               mMouseOverSX;
    private int               mMouseOverSY;
    private int               mMouseScrolled;

    public EbCameraControl(EbCameraCalc aCameraCalc) {
        mCameraCalc = aCameraCalc;

        mMoving = false;

        mTouching = new boolean[EbCameraControl.TOUCH_MAX];
        mTouchSX = new int[EbCameraControl.TOUCH_MAX];
        mTouchSY = new int[EbCameraControl.TOUCH_MAX];

        mTouchStartSX = new int[EbCameraControl.TOUCH_MAX];
        mTouchStartSY = new int[EbCameraControl.TOUCH_MAX];
    }

    public void update(float aDelta) {
        int i;

        final float reduce = (float) Math.pow(EbCameraControl.SMOOTH_REDUCE, aDelta);
        final float intReduce = (reduce - 1) * EbCameraControl.DIV_LN_SMOOTH_REDUCE;

        float touchSXAvg = 0;
        float touchSYAvg = 0;
        float touchSDiff = 0;
        int touchCount = 0;

        for (i = 0; i < EbCameraControl.TOUCH_MAX; ++i) {
            if (!mTouching[i]) {
                continue;
            }
            touchSXAvg += mTouchSX[i];
            touchSYAvg += mTouchSY[i];
            ++touchCount;
        }

        if (touchCount > 0) {
            touchSXAvg /= touchCount;
            touchSYAvg /= touchCount;
            if (touchCount > 1) {
                for (i = 0; i < EbCameraControl.TOUCH_MAX; ++i) {
                    if (!mTouching[i]) {
                        continue;
                    }
                    float d = 0, dd = 0;
                    dd = mTouchSX[i] - touchSXAvg;
                    dd *= dd;
                    d += dd;
                    dd = mTouchSY[i] - touchSYAvg;
                    dd *= dd;
                    d += dd;
                    touchSDiff += (float) Math.sqrt(d);
                }
            }
            touchSDiff /= touchCount;
            if (mTouchCountChange) {
                mTouchStartWXAvg = mCameraCalc.screenToWorldX(touchSXAvg);
                mTouchStartWYAvg = mCameraCalc.screenToWorldY(touchSYAvg);
                if (touchCount > 1) {
                    mTouchStartSDiff = touchSDiff;
                    mTouchStartCameraZoom = mCameraCalc.iCameraCalcZoom;
                } else {
                    mCameraCalc.smoothZoom(aDelta, reduce, intReduce);
                    final float newCameraWX = mCameraCalc.screenWorldToCameraWX(touchSXAvg, mTouchStartWXAvg);
                    final float newCameraWY = mCameraCalc.screenWorldToCameraWY(touchSYAvg, mTouchStartWYAvg);
                    mCameraCalc.xyMove(newCameraWX, newCameraWY, aDelta);
                }
                mTouchCountChange = false;
            } else if (mTouchChange) {
                if (touchCount > 1) {
                    final float newZoom = mTouchStartCameraZoom * mTouchStartSDiff / touchSDiff;
                    mCameraCalc.zoomMove(newZoom, aDelta);
                } else {
                    mCameraCalc.smoothZoom(aDelta, reduce, intReduce);
                }
                final float newCameraWX = mCameraCalc.screenWorldToCameraWX(touchSXAvg, mTouchStartWXAvg);
                final float newCameraWY = mCameraCalc.screenWorldToCameraWY(touchSYAvg, mTouchStartWYAvg);
                mCameraCalc.xyMove(newCameraWX, newCameraWY, aDelta);
            } else if (touchCount == 1) {
                mCameraCalc.smoothZoom(aDelta, reduce, intReduce);
                final float newCameraWX = mCameraCalc.screenWorldToCameraWX(touchSXAvg, mTouchStartWXAvg);
                final float newCameraWY = mCameraCalc.screenWorldToCameraWY(touchSYAvg, mTouchStartWYAvg);
                mCameraCalc.xySet(newCameraWX, newCameraWY);
            }
        } else if (mMouseScrolled != 0) {
            final float mouseBX = mCameraCalc.screenToWorldX(mMouseOverSX);
            final float mouseBY = mCameraCalc.screenToWorldY(mMouseOverSY);

            mCameraCalc.mCameraCalcZoomD -= mMouseScrolled * Const.PHI;
            mCameraCalc.smoothZoom(aDelta, reduce, intReduce);

            final float newCameraWX = mCameraCalc.screenWorldToCameraWX(mMouseOverSX, mouseBX);
            final float newCameraWY = mCameraCalc.screenWorldToCameraWY(mMouseOverSY, mouseBY);
            mCameraCalc.xyMove(newCameraWX, newCameraWY, aDelta);
            mMouseScrolled = 0;
        } else {
            mCameraCalc.smoothZoom(aDelta, reduce, intReduce);
            mCameraCalc.smoothXY(aDelta, reduce, intReduce);
            mMoving = false;
        }
        mTouchChange = false;

        if (mTouchCountChange && (touchCount == 0) && (mCameraCalc.mLockTime > 0)) {
            mCameraCalc.iCameraCalcWX = mCameraCalc.iCameraRealWX;
            mCameraCalc.iCameraCalcWY = mCameraCalc.iCameraRealWY;
            mCameraCalc.iCameraCalcZoom = mCameraCalc.iCameraRealZoom;
            mCameraCalc.mCameraCalcWXD = 0;
            mCameraCalc.mCameraCalcWYD = 0;
            mCameraCalc.mCameraCalcZoomD = 0;
        }

        mCameraCalc.updateLock(reduce);

        iLogger.debug("x " + mCameraCalc.iCameraRealWX);
        iLogger.debug("y " + mCameraCalc.iCameraRealWY);
        iLogger.debug("z " + mCameraCalc.iCameraRealZoom);
    }

    public void touchDown(int aSX, int aSY, int aPointer, int aButton, long aTime) {
        // iLogger.debug("touchDown");
        mTouchCountChange = true;
        mTouchChange = true;
        mTouching[aPointer] = true;
        mTouchSX[aPointer] = aSX;
        mTouchSY[aPointer] = aSY;
        mTouchStartSX[aPointer] = aSX;
        mTouchStartSY[aPointer] = aSY;
    }

    public void touchUp(int aSX, int aSY, int aPointer, int aButton, long aTime) {
        // iLogger.debug("touchUp");
        mTouchCountChange = true;
        mTouchChange = true;
        mTouching[aPointer] = false;
    }

    public void touchDragged(int aSX, int aSY, int aPointer, long aTime) {
        // iLogger.debug("touchDragged");
        mTouching[aPointer] = true;
        mTouchChange = ((mTouchSX[aPointer] != aSX) || (mTouchSY[aPointer] != aSY));
        mTouchSX[aPointer] = aSX;
        mTouchSY[aPointer] = aSY;

        final float diff = EbCameraCalc.diff(aSX, aSY, mTouchStartSX[aPointer], mTouchStartSY[aPointer]);
        if (diff > Gdx.graphics.getPpcX() * 3) {
            mMoving = true;
            mCameraCalc.mLockTime = -1;
        }
    }

    public void touchMoved(int aX, int aY, long aTime) {
        // iLogger.debug("touchMoved");
        mMouseOverSX = aX;
        mMouseOverSY = aY;
    }

    public void scrolled(int aAmount, long aTime) {
        mMouseScrolled += aAmount;
    }

}
