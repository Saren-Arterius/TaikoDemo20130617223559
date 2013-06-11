package com.luzi82.elly.camera;

import com.badlogic.gdx.utils.Logger;
import com.luzi82.elly.Const;

public class EbCameraCalc {

	protected Logger iLogger = new Logger(this.getClass().getSimpleName(), Logger.DEBUG);
	
	// move limit
	public float mWorldW;
	public float mWorldH;

	// screen
	public int mScreenW;
	public int mScreenH;
	public float mViewPortW;
	public float mViewPortH;

	// zoom limit
	public float mZoomMin;
	public float mZoomMax;
	public float mLogZoomMin;
	public float mLogZoomMax;

	// lock
	public long mLockTime;
	public float mLockWXDiff;
	public float mLockWYDiff;
	public float mLockZoomDiff;

	// camera (lock protected)
	public float iCameraRealZoom;
	public float iCameraRealWX;
	public float iCameraRealWY;

	// camera (calc use)
	public float iCameraCalcZoom;
	public float iCameraCalcWX;
	public float iCameraCalcWY;

	// camera dynamic
	public float mCameraCalcZoomD;
	public float mCameraCalcWXD;
	public float mCameraCalcWYD;

	public EbCameraCalc(float aWorldW, float aWorldH) {
		mWorldW = aWorldW;
		mWorldH = aWorldH;

		mLockTime = -1;
		mLockWXDiff = 0;
		mLockWYDiff = 0;
		mLockZoomDiff = 0;

//		iCameraZoom = Math.min(mWorldW, mWorldH);
		iCameraCalcZoom = 8;
		iCameraCalcWX = mWorldW / 2;
		iCameraCalcWY = mWorldH / 2;
		mCameraCalcZoomD = 0;
		mCameraCalcWXD = 0;
		mCameraCalcWYD = 0;
	}

	public void updateLock(float aReduce) {
		if (mLockTime < System.currentTimeMillis()) {
			mLockTime = -1;
		}

		if (mLockTime < 0) {
			CalcLockRet clr;

			clr = calcLock(iCameraRealWX, iCameraCalcWX, mLockWXDiff, aReduce);
			iCameraRealWX = clr.mValue;
			mLockWXDiff = clr.mDiff;

			clr = calcLock(iCameraRealWY, iCameraCalcWY, mLockWYDiff, aReduce);
			iCameraRealWY = clr.mValue;
			mLockWYDiff = clr.mDiff;

			clr = calcLock((float) Math.log(iCameraRealZoom),
					(float) Math.log(iCameraCalcZoom), mLockZoomDiff, aReduce);
			iCameraRealZoom = (float) Math.pow(Math.E, clr.mValue);
			mLockZoomDiff = clr.mDiff;
		} else {
			mLockWXDiff = iCameraRealWX - iCameraCalcWX;
			mLockWYDiff = iCameraRealWY - iCameraCalcWY;
			mLockZoomDiff = Math.abs((float) Math.log(iCameraRealZoom
					/ iCameraCalcZoom));
		}
	}

	public void zoomMove(float aNewZoom, float aDelta) {
		mCameraCalcZoomD = ((float) Math.log(aNewZoom / iCameraCalcZoom)) / aDelta;
		iCameraCalcZoom = aNewZoom;
	}

	public void xyMove(float aNewX, float aNewY, float aDelta) {
		mCameraCalcWXD = (aNewX - iCameraCalcWX) / aDelta;
		mCameraCalcWYD = (aNewY - iCameraCalcWY) / aDelta;
		iCameraCalcWX = aNewX;
		iCameraCalcWY = aNewY;
	}

	public void xySet(float aNewX, float aNewY) {
		iCameraCalcWX = aNewX;
		iCameraCalcWY = aNewY;
	}

	public float viewWorldYMin() {
		return iCameraCalcWY - iCameraCalcZoom * mViewPortH / 2;
	}

	public float viewWorldYMax() {
		return iCameraCalcWY + iCameraCalcZoom * mViewPortH / 2;
	}

	public float viewWorldXMin() {
		return iCameraCalcWX - iCameraCalcZoom * mViewPortW / 2;
	}

	public float viewWorldXMax() {
		return iCameraCalcWX + iCameraCalcZoom * mViewPortW / 2;
	}

	public float screenToWorldX(float aScreenX) {
		return iCameraCalcWX + (iCameraCalcZoom * mViewPortW * (aScreenX / mScreenW - 0.5f));
	}

	public float screenToWorldY(float aScreenY) {
		return iCameraCalcWY
				+ (iCameraCalcZoom * mViewPortH * (1 - (aScreenY / mScreenH) - 0.5f));
	}

	public float screenToWorldRealX(float aScreenX) {
		return iCameraRealWX
				+ (iCameraRealZoom * mViewPortW * (aScreenX / mScreenW - 0.5f));
	}

	public float screenToWorldRealY(float aScreenY) {
		return iCameraRealWY
				+ (iCameraRealZoom * mViewPortH * (1 - (aScreenY / mScreenH) - 0.5f));
	}

	public float screenWorldToCameraWX(float aScreenX, float aWorldX) {
		return (iCameraCalcZoom * mViewPortW) * (0.5f - aScreenX / mScreenW)
				+ aWorldX;
	}

	public float screenWorldToCameraWY(float aScreenY, float aWorldY) {
		return (iCameraCalcZoom * mViewPortH) * (0.5f + aScreenY / mScreenH - 1)
				+ aWorldY;
	}

	public void onScreenResize(int aScreenWidth, int aScreenHeight) {
		mScreenW = aScreenWidth;
		mScreenH = aScreenHeight;
		mViewPortW = (mScreenW > mScreenH) ? (((float) mScreenW) / mScreenH)
				: 1;
		mViewPortH = (mScreenW > mScreenH) ? 1
				: (((float) mScreenH) / mScreenW);

		mZoomMin = 4 * Const.PHI;
		mZoomMax = Math.max(mWorldH / mViewPortH, mWorldW / mViewPortW)
				* Const.PHI;
		mLogZoomMin = (float) Math.log(mZoomMin);
		mLogZoomMax = (float) Math.log(mZoomMax);
	}

	public void smoothZoom(float aDelta, float aReduce, float aIntReduce) {
		// iCameraZoom *= (float) Math.pow(Math.E, mCameraZoomD * aIntReduce);
		float logCameraZoom = (float) Math.log(iCameraCalcZoom);
		logCameraZoom = smooth(aDelta, aReduce, aIntReduce, logCameraZoom,
				mCameraCalcZoomD, mLogZoomMin, mLogZoomMax);
		iCameraCalcZoom = (float) Math.pow(Math.E, logCameraZoom);
		mCameraCalcZoomD *= aReduce;
	}

	public void smoothXY(float aDelta, float aReduce, float aIntReduce) {
		// iCameraX += mCameraXD * aIntReduce;
		// iCameraY += mCameraYD * aIntReduce;
		iCameraCalcWX = smooth(aDelta, aReduce, aIntReduce, iCameraCalcWX, mCameraCalcWXD,
				0, mWorldW);
		iCameraCalcWY = smooth(aDelta, aReduce, aIntReduce, iCameraCalcWY, mCameraCalcWYD,
				0, mWorldH);
		mCameraCalcWXD *= aReduce;
		mCameraCalcWYD *= aReduce;
	}

	private static float smooth(float aDelta, float aReduce, float aIntReduce,
			float aS0, float aV, float aMin, float aMax) {
		if ((aMin <= aS0) && (aS0 <= aMax)) {
			float s1 = aS0 + aV * aIntReduce;
			// TODO mid border
			return s1;
		} else {
			float border = (aS0 < aMin) ? aMin : aMax;
			float dm = aV * aIntReduce;
			float db = (aReduce - 1) * (2 * aS0 - 2 * border + dm) / 2;
			float s1 = aS0 + dm + db;
			// TODO mid border
			return s1;
		}

	}

	public static float diff(float aX0, float aY0, float aX1, float aY1) {
		float dx = aX1 - aX0;
		float dy = aY1 - aY0;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	private static class CalcLockRet {
		float mValue;
		float mDiff;

		CalcLockRet(float aTar, float aDiff) {
			mValue = aTar;
			mDiff = aDiff;
		}
	}

	private static CalcLockRet calcLock(float aOri, float aTar, float aDiff,
			float aReduce) {
		if (aDiff == 0)
			return new CalcLockRet(aTar, 0);
		if (aOri == aTar)
			return new CalcLockRet(aTar, 0);
		float diff = aOri - aTar;
		aDiff *= aReduce;
		if (diff * aDiff <= 0)
			return new CalcLockRet(aTar, 0);
		if (Math.abs(diff) <= Math.abs(aDiff))
			return new CalcLockRet(aOri, diff);
		return new CalcLockRet(aTar + aDiff, aDiff);
	}

}
