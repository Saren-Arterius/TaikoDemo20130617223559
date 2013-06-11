package com.owataiko.taikodemo;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.luzi82.elly.EbGame;

public class TaikoGame extends EbGame {

	public TaikoAudio iTaikoAudio;

	public TaikoGame(TaikoAudio aTaikoAudio) {
		iTaikoAudio = aTaikoAudio;
	}

	@Override
	public void create() {
		super.create();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		setScreen(new TaikoGameScreen(this,iTaikoAudio));
	}

	// private OrthographicCamera camera;
	// private SpriteBatch batch;
	// private Texture texture;
	// private Sprite sprite;
	//
	// @Override
	// public void create() {
	// float w = Gdx.graphics.getWidth();
	// float h = Gdx.graphics.getHeight();
	//
	// camera = new OrthographicCamera(1, h/w);
	// batch = new SpriteBatch();
	//
	// texture = new Texture(Gdx.files.internal("data/libgdx.png"));
	// texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	//
	// TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
	//
	// sprite = new Sprite(region);
	// sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
	// sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
	// sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
	// }
	//
	// @Override
	// public void dispose() {
	// batch.dispose();
	// texture.dispose();
	// }
	//
	// @Override
	// public void render() {
	// Gdx.gl.glClearColor(1, 1, 1, 1);
	// Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	//
	// batch.setProjectionMatrix(camera.combined);
	// batch.begin();
	// sprite.draw(batch);
	// batch.end();
	// }
	//
	// @Override
	// public void resize(int width, int height) {
	// }
	//
	// @Override
	// public void pause() {
	// }
	//
	// @Override
	// public void resume() {
	// }
}
