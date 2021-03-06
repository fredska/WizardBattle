package com.wb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.wb.entity.Player;
import com.wb.level.BaseLevel;
import com.wb.level.LevelInstance;
import com.wb.level.LevelManager;
import com.wb.level.TestMovementLevel;
import com.wb.level.WorldManager;

public class GameScreen implements Screen {

	private final static int SCREEN_HEIGHT = Gdx.graphics.getHeight();
	private final static int SCREEN_WIDTH = Gdx.graphics.getWidth();

	private BaseLevel level;
	private OrthographicCamera camera;
	private Player player;
	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
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
		player.debug(camera, level);
//		debugRenderer.render(WorldManager.getInstance().getWorld(), camera.combined);
//		WorldManager.getInstance().nextStep();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		
		LevelManager.getInstance().initialize(ScreenManager.getInstance().getGameInstance());
		LevelManager.getInstance().load(LevelInstance.TEST_MOVEMENT);
		level = LevelManager.getInstance().getCurrentLevel();
		level.loadLevelIntoWorld(1);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		player = new Player();
		player.setPosition(new Vector3(64, 64, 0));
	}

	private Array<Rectangle> tiles = new Array<Rectangle>();

	private void updatePlayer(float delta) {
		float modifier = 250f;
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
