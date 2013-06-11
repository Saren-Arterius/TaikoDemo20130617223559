package com.luzi82.elly.draw;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.Logger;
import com.luzi82.elly.Const;
import com.luzi82.elly.camera.EbCameraCalc;
import com.luzi82.elly.camera.EbLogicScreen;

public class EbGridDraw extends EbDraw {

	protected Logger iLogger = new Logger(this.getClass().getSimpleName(),
			Logger.DEBUG);

	public static final float PHI = Const.PHI;
	private static final float LINE_ALPHA = 1f / 16;
	private static final float LINE_WIDTH = 1f / 16;

	private Mesh mLineMeshH;
	private float[] mLineMeshHF;
	private final int[] LINE_MESH_HA = {//
	1 * 7 - 1,//
			2 * 7 - 1,//
			7 * 7 - 1,//
			8 * 7 - 1,//
	};

	public EbGridDraw() {
		super(Type.STATIC);
	}

	public void drawStatic(GL10 aGl, EbLogicScreen<?, ?> aScreen) {
		// iLogger.debug("drawStatic");
		int i;

		aGl.glEnable(GL10.GL_BLEND);
		aGl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		aGl.glDisable(GL10.GL_TEXTURE_2D);

		EbCameraCalc mCameraCalc = aScreen.mCameraCalc;
		float worldW = aScreen.mLogic.mWorldW;
		float worldH = aScreen.mLogic.mWorldH;

		int factor = 1;
		final int FACTOR_UP = 8;
		// final int ALPHA_LIMIT = 4;

		while (factor < mCameraCalc.mZoomMax) {

			// if (mCameraCalc.iCameraRealZoom < factor * FACTOR_UP) {
			float a = mCameraCalc.iCameraRealZoom / factor;
			a = (float) Math.log(a);
			a /= (float) Math.log(FACTOR_UP);
			if (a > 1)
				a = 2 - a;
			// a = 1 - a;
			if (a > 0) {
				// if (a > 1)
				// a = 1f;
				a *= LINE_ALPHA;
				for (int ai : LINE_MESH_HA) {
					mLineMeshHF[ai] = a;
				}
				mLineMeshH.setVertices(mLineMeshHF);

				int min, max;

				min = (int) Math
						.floor(Math.max(0, mCameraCalc.viewWorldYMin()));
				min = (min / factor) * factor;
				max = (int) Math.ceil(Math.min(worldH,
						mCameraCalc.viewWorldYMax()));
				for (i = min; i < max; i += factor) {
					aGl.glPushMatrix();
					aGl.glTranslatef(0, i, 0);
					aGl.glScalef(worldW, factor, 1);
					mLineMeshH.render(GL10.GL_TRIANGLES);
					aGl.glPopMatrix();
				}

				min = (int) Math
						.floor(Math.max(0, mCameraCalc.viewWorldXMin()));
				min = (min / factor) * factor;
				max = (int) Math.ceil(Math.min(worldW,
						mCameraCalc.viewWorldXMax()));
				aGl.glPushMatrix();
				aGl.glRotatef(90, 0, 0, 1);
				aGl.glScalef(1, -1, 1);
				for (i = min; i < max; i += factor) {
					aGl.glPushMatrix();
					aGl.glTranslatef(0, i, 0);
					aGl.glScalef(worldH, factor, 1);
					mLineMeshH.render(GL10.GL_TRIANGLES);
					aGl.glPopMatrix();
				}
				aGl.glPopMatrix();
			}
			// }

			factor *= FACTOR_UP;

		}
	}

	public void alloc() {
		iLogger.debug("alloc");
		VertexAttributes va;
		va = new VertexAttributes( //
				new VertexAttribute(VertexAttributes.Usage.Position, 3,
						"position"),//
				new VertexAttribute(VertexAttributes.Usage.Color, 4, "color")//
		);
		mLineMeshHF = new float[] { //
		1f, 0f, 0f, 0f, 0f, 0f, LINE_ALPHA,//
				0f, 0f, 0f, 0f, 0f, 0f, LINE_ALPHA,//
				1f, LINE_WIDTH, 0f, 0f, 0f, 0f, 0f,//
				0f, LINE_WIDTH, 0f, 0f, 0f, 0f, 0f,//

				1f, -LINE_WIDTH, 0f, 0f, 0f, 0f, 0f,//
				0f, -LINE_WIDTH, 0f, 0f, 0f, 0f, 0f,//
				1f, 0f, 0f, 0f, 0f, 0f, LINE_ALPHA,//
				0f, 0f, 0f, 0f, 0f, 0f, LINE_ALPHA,//
		};
		mLineMeshH = new Mesh(false, 8, 12, va);
		mLineMeshH.setIndices(new short[] { //
				0, 1, 2, //
						1, 2, 3, //
						4, 5, 6, //
						5, 6, 7 //
				});
	}

	public void free() {
		iLogger.debug("free");
		mLineMeshHF = null;
		if (mLineMeshH != null) {
			mLineMeshH.dispose();
		}
		mLineMeshH = null;
	}

}
