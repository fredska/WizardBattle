package com.wb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.wb.entity.Player;
import com.wb.level.BaseLevel;
import com.wb.level.TestMovementLevel;

public class GameScreen implements Screen {

	private final static int SCREEN_HEIGHT = Gdx.graphics.getHeight();
	private final static int SCREEN_WIDTH = Gdx.graphics.getWidth();

	private BaseLevel level;
	private OrthographicCamera camera;
	private Player player;

	@Override
	public void render(float delta) {
		Gdx.gl10.glClearColor(1, 1, 1, 1);
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		// Update the camera position
		updatePlayer(delta);
		updateCamera(delta);

		// TODO: Show Error screen here
		if (level == null)
			return;

		level.render(camera);
		player.draw(level.getRenderer().getSpriteBatch());
//		System.out.println(player.getPosition());
		// player.draw(camera);
		// player.debug(camera, level);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		level = new TestMovementLevel();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		player = new Player();
		player.setPosition(new Vector3(64, 64, 0));
	}

	private Array<Rectangle> tiles = new Array<Rectangle>();

	private void updatePlayer(float delta) {
		float modifier = 100f;
		player.getVelocity().set(0, 0);
		if ((Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W))) {
			player.getVelocity().add(0, modifier);
		}
		if ((Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input
				.isKeyPressed(Keys.S))) {
			player.getVelocity().add(0, -modifier);
		}
		if ((Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input
				.isKeyPressed(Keys.A))) {
			player.getVelocity().add(-modifier, 0);
		}
		if ((Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input
				.isKeyPressed(Keys.D))) {
			player.getVelocity().add(modifier, 0);
		}

		player.update(delta);
	}

	private void updateCamera(float delta) {
		camera.position.set(player.getPosition());
		camera.update();
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {

	}

}
