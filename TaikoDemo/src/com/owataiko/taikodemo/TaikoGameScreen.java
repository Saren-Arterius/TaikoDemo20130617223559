package com.owataiko.taikodemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.luzi82.elly.EbScreen;
import com.luzi82.elly.render.EbTextRender;
import com.luzi82.elly.render.MeshUtils;

public class TaikoGameScreen extends EbScreen<TaikoGame> implements Screen {

	public OrthographicCamera mCamera;

	public Mesh mNoteAreaBackgroundMesh;

	public Mesh mLeftDonMesh;
	public Mesh mLeftKatMesh;
	public Mesh mRightDonMesh;
	public Mesh mRightKatMesh;

	public Texture mTargetTexture;
	public Texture mDonBigTexture;
	public Texture mDonSmallTexture;
	public Texture mKatBigTexture;
	public Texture mKatSmallTexture;
	public Texture mLongSmallTexture;
	public Mesh mUnitRectMesh;

	public EbTextRender mTestText;

	public int iNoteAreaHeight;
	public int iPadSize;
	public boolean iPlaying;

	public long iStartTime;

	public boolean iSupportLowLatency;

	protected TaikoGameScreen(TaikoGame aParent) {
		super(aParent);
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

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		mTargetTexture.bind();
		gl.glPushMatrix(); // H4zbUmA3

		gl.glTranslatef(0, mScreenHeight - iNoteAreaHeight, 0);

		gl.glPushMatrix(); // Va08ccxD
		gl.glScalef(iNoteAreaHeight, iNoteAreaHeight, 0);
		mUnitRectMesh.render(GL10.GL_TRIANGLE_STRIP);
		gl.glPopMatrix(); // Va08ccxD

		final long SCREEN_PERIOD = 2000;

		// long bgmPos = iTaikoAudio.bgmPos();
		long bgmPos = System.currentTimeMillis() - iStartTime;
		if (iPlaying) {
			int scanIdxStart = (int) (Math.floor((bgmPos
					- Caramelldansen.start_time * 1000 - SCREEN_PERIOD / 2)
					/ (Caramelldansen.unit_time * 1000)));
			int scanIdxEnd = (int) (Math.floor((bgmPos
					- Caramelldansen.start_time * 1000 + 3 * SCREEN_PERIOD / 2)
					/ (Caramelldansen.unit_time * 1000)));
			scanIdxStart = Math.max(0, scanIdxStart);
			scanIdxEnd = Math.min(scanIdxEnd, Caramelldansen.note.length);

			for (int idx = scanIdxStart; idx < scanIdxEnd; ++idx) {
				Texture tx = null;
				if (Caramelldansen.note[idx] == 1) {
					tx = mDonSmallTexture;
				} else if (Caramelldansen.note[idx] == 2) {
					tx = mKatSmallTexture;
				} else if (Caramelldansen.note[idx] == 3) {
					tx = mDonBigTexture;
				} else if (Caramelldansen.note[idx] == 4) {
					tx = mKatBigTexture;
				} else if (Caramelldansen.note[idx] == 5) {
					tx = mLongSmallTexture;
				} else if (Caramelldansen.note[idx] == 55) {
					tx = mLongSmallTexture;
				} else {
					continue;
				}
				long time = (long) ((idx * Caramelldansen.unit_time + Caramelldansen.start_time) * 1000);
				time -= bgmPos;
				float x = time * mScreenWidth;
				x /= SCREEN_PERIOD;

				tx.bind();

				gl.glPushMatrix(); // Pq0dnidl
				gl.glTranslatef(x, 0, 0);
				gl.glScalef(iNoteAreaHeight, iNoteAreaHeight, 0);
				mUnitRectMesh.render(GL10.GL_TRIANGLE_STRIP);
				gl.glPopMatrix(); // Pq0dnidl
			}

		}

		gl.glPopMatrix(); // H4zbUmA3

		gl.glPushMatrix(); // IRBZwEFn

		gl.glTranslatef(mScreenWidth, 0, 0f);
		gl.glScalef(32f, 32f, 0f);
		gl.glTranslatef(-1f, 0, 0f);
		Texture tx2 = iSupportLowLatency ? mKatBigTexture : mDonBigTexture;
		tx2.bind();
		mUnitRectMesh.render(GL10.GL_TRIANGLE_STRIP);

		gl.glPopMatrix(); // IRBZwEFn

		gl.glPushMatrix(); // I3bmfI9C

		gl.glTranslatef(mScreenWidth / 2, mScreenHeight / 2, 0f);
		gl.glScalef(32f, 32f, 0f);
		mTestText.render(gl);

		gl.glPopMatrix(); // I3bmfI9C
	}

	protected void onScreenLoadMember() {
		iParent.iParam.mTaikoAudio.load();

		mCamera = new OrthographicCamera();
		mCamera.zoom = 1.0f;
		mCamera.viewportWidth = mScreenWidth;
		mCamera.viewportHeight = mScreenHeight;
		mCamera.position.x = mScreenWidth * 0.5f;
		mCamera.position.y = mScreenHeight * 0.5f;
		mCamera.update();

		iNoteAreaHeight = mScreenWidth / 8;
		iPadSize = mScreenHeight - iNoteAreaHeight;
		if (iPadSize > mScreenWidth / 2)
			iPadSize = mScreenWidth / 2;

		mNoteAreaBackgroundMesh = MeshUtils.newBoxMesh(0, mScreenHeight,
				mScreenWidth, mScreenHeight - iNoteAreaHeight);

		mLeftKatMesh = MeshUtils.newTriangleMesh(0, iPadSize, iPadSize,
				iPadSize, 0, 0);
		mRightKatMesh = MeshUtils.newTriangleMesh(mScreenWidth, iPadSize,
				mScreenWidth - iPadSize, iPadSize, mScreenWidth, 0);

		mLeftDonMesh = MeshUtils.newTriangleMesh(iPadSize, iPadSize, iPadSize,
				0, 0, 0);
		mRightDonMesh = MeshUtils.newTriangleMesh(mScreenWidth - iPadSize,
				iPadSize, mScreenWidth - iPadSize, 0, mScreenWidth, 0);

		mTargetTexture = new Texture(Gdx.files.internal("data/target.png"));
		mDonSmallTexture = new Texture(Gdx.files.internal("data/don_small.png"));
		mKatSmallTexture = new Texture(Gdx.files.internal("data/kat_small.png"));
		mDonBigTexture = new Texture(Gdx.files.internal("data/don_big.png"));
		mKatBigTexture = new Texture(Gdx.files.internal("data/kat_big.png"));
		mLongSmallTexture = new Texture(
				Gdx.files.internal("data/long0_small.png"));

		VertexAttributes va;
		va = new VertexAttributes( //
				new VertexAttribute(VertexAttributes.Usage.Position, 3,
						"position"),//
				new VertexAttribute(VertexAttributes.Usage.TextureCoordinates,
						2, "texturecoordinates")//
		);

		mUnitRectMesh = new Mesh(true, 4, 4, va);
		mUnitRectMesh.setVertices(new float[] { //
				0f, 1f, 0f, 0f, 1f,//
						1f, 1f, 0f, 1f, 1f,//
						0f, 0f, 0f, 0f, 0f,//
						1f, 0f, 0f, 1f, 0f,//
				});
		mUnitRectMesh.setIndices(new short[] { 0, 1, 2, 3 });

		mTestText = iParent.iParam.mTextRenderFactory.create("Hello", 64);

		iPlaying = false;

		iSupportLowLatency = iParent.iParam.mTaikoAudio.supportLowLatency();
	}

	@Override
	protected void onScreenDisposeMember() {
		iParent.iParam.mTaikoAudio.unload();
		iPlaying = false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button, long aTime) {
		if (y < iNoteAreaHeight) {
			if (iPlaying) {
				iParent.iParam.mTaikoAudio.stopBgm();
			} else {
				iParent.iParam.mTaikoAudio.playBgm();
				iStartTime = System.currentTimeMillis();
			}
			iPlaying = !iPlaying;
		} else if (y > mScreenHeight - iPadSize) {
			if (x < iPadSize) {
				if (x > mScreenHeight - y) {
					iParent.iParam.mTaikoAudio.don();
				} else {
					iParent.iParam.mTaikoAudio.kat();
				}
			} else if (mScreenWidth - x < iPadSize) {
				if (mScreenWidth - x > mScreenHeight - y) {
					iParent.iParam.mTaikoAudio.don();
				} else {
					iParent.iParam.mTaikoAudio.kat();
				}
			}
		}
		return true;
	}

	final public float PHI = (float) ((1 + Math.sqrt(5)) * 0.5);

	final public float PHI_1 = 1 / PHI;

	final public float PHI_2 = PHI_1 * PHI_1;

	final public float PHI_3 = PHI_2 * PHI_1;

}
