package com.owataiko.taikodemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.luzi82.elly.EbScreen;
import com.luzi82.elly.render.MeshUtils;

public class TaikoGameScreen extends EbScreen<TaikoGame> implements Screen {

	public OrthographicCamera mCamera;
	public TaikoAudio iTaikoAudio;

	public Mesh mNoteAreaBackgroundMesh;

	public Mesh mLeftDonMesh;
	public Mesh mLeftKatMesh;
	public Mesh mRightDonMesh;
	public Mesh mRightKatMesh;

	protected TaikoGameScreen(TaikoGame aParent, TaikoAudio aTaikoAudio) {
		super(aParent);
		iTaikoAudio = aTaikoAudio;
	}

	public void onScreenRender(float aDelta) {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(PHI_2, PHI_2, PHI_2, 1.0f);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glDisable(GL10.GL_TEXTURE_2D);

		mCamera.apply(gl);

		// gl.glLineWidth(3.0f);
		gl.glColor4f(PHI_3, PHI_3, PHI_3, 1.0f);
		mNoteAreaBackgroundMesh.render(GL10.GL_TRIANGLE_STRIP);

		gl.glColor4f(0f, 0f, PHI_2, 1.0f);
		mLeftKatMesh.render(GL10.GL_TRIANGLE_STRIP);
		mRightKatMesh.render(GL10.GL_TRIANGLE_STRIP);

		gl.glColor4f(PHI_2, 0f, 0f, 1.0f);
		mLeftDonMesh.render(GL10.GL_TRIANGLE_STRIP);
		mRightDonMesh.render(GL10.GL_TRIANGLE_STRIP);
	}

	protected void onScreenLoadMember() {
		mCamera = new OrthographicCamera();
		mCamera.zoom = 1.0f;
		mCamera.viewportWidth = mScreenWidth;
		mCamera.viewportHeight = mScreenHeight;
		mCamera.position.x = mScreenWidth * 0.5f;
		mCamera.position.y = mScreenHeight * 0.5f;
		mCamera.update();

		float noteAreaHeight = mScreenWidth / 8;
		float padSize = mScreenHeight - noteAreaHeight;

		mNoteAreaBackgroundMesh = MeshUtils.newBoxMesh(0, mScreenHeight,
				mScreenWidth, mScreenHeight - noteAreaHeight);

		mLeftKatMesh = MeshUtils.newTriangleMesh(0, padSize, padSize, padSize,
				0, 0);
		mRightKatMesh = MeshUtils.newTriangleMesh(mScreenWidth, padSize,
				mScreenWidth - padSize, padSize, mScreenWidth, 0);

		mLeftDonMesh = MeshUtils.newTriangleMesh(padSize, padSize, padSize, 0,
				0, 0);
		mRightDonMesh = MeshUtils.newTriangleMesh(mScreenWidth - padSize,
				padSize, mScreenWidth - padSize, 0, mScreenWidth, 0);
	}

	final public float PHI = (float) ((1 + Math.sqrt(5)) * 0.5);

	final public float PHI_1 = 1 / PHI;

	final public float PHI_2 = PHI_1 * PHI_1;

	final public float PHI_3 = PHI_2 * PHI_1;

}
