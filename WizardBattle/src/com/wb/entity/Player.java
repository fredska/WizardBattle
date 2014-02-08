package com.wb.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.wb.level.BaseLevel;
import com.wb.level.LevelInstance;
import com.wb.level.LevelManager;
import com.wb.utils.SweptAABB;

public class Player extends Entity {

	private ShapeRenderer shapeRenderer;
	public final float WIDTH;
	public final float HEIGHT;

	// static float MAX_VELOCITY = 10f;
	// static float JUMP_VELOCITY = 40f;
	// static float DAMPING = 0.75f;

	enum State {
		Standing, Walking
	}

	final Vector2 position = new Vector2();
	final Vector2 velocity = new Vector2();
	State state = State.Standing;
	float stateTime = 0;
	boolean faceRight = true;
	boolean walking = false;
	boolean grounded = false;

	private Animation playerStand;
	private Animation playerWalkUp, playerWalkLeft, playerWalkDown, playerWalkRight;

	private Texture playerStandTexture;
	private Sprite sprite;

	public Player() {
		super(true);

		// Only need one frame to show the player has stopped
		TextureRegion stand = new TextureRegion(new Texture(Gdx.files.internal("art/sprites/wizard/smr1/smr1_fr1.png")));
		playerStand = new Animation(0, stand);

		// Initialize the animation for walking upward ;
		playerWalkUp = compileAnimation("art/sprites/wizard/smr1/smr1_bk1.gif", "art/sprites/wizard/smr1/smr1_bk2.gif");
		playerWalkLeft = compileAnimation("art/sprites/wizard/smr1/smr1_lf1.gif", "art/sprites/wizard/smr1/smr1_lf2.gif");
		playerWalkDown = compileAnimation("art/sprites/wizard/smr1/smr1_fr1.png", "art/sprites/wizard/smr1/smr1_fr2.png");
		playerWalkRight = compileAnimation("art/sprites/wizard/smr1/smr1_rt1.gif", "art/sprites/wizard/smr1/smr1_rt2.gif");
		super.setHeight(32f);
		super.setWidth(32f);
		this.WIDTH = 32f;
		this.HEIGHT = 32f;
		shapeRenderer = new ShapeRenderer();
		tiles = new Array<Rectangle>();
	}

	private Animation compileAnimation(String... spriteFilePath) {
		List<TextureRegion> spriteFrames = new LinkedList<TextureRegion>();
		for (String filePath : spriteFilePath) {
			spriteFrames.add(new TextureRegion(new Texture(Gdx.files.internal(filePath))));
		}
		TextureRegion[] regions = new TextureRegion[spriteFrames.size()];
		regions = spriteFrames.toArray(regions);
		Animation result = new Animation(0.15f, regions);
		result.setPlayMode(Animation.LOOP_PINGPONG);
		return result;
	}

	private Array<Rectangle> collidedTiles = new Array<Rectangle>();

	/**
	 * Update any information of the player on a frame by frame basis This
	 * includes movement, health, mana, animation changes
	 */
	@Override
	public void update(float delta) {
		this.stateTime += delta;
		collidedTiles.clear();
		// System.out.println("Velocity: " + getVelocity());

		// Get all nearby tiles
		// First load the level data;
		BaseLevel level = null;
		if (LevelManager.getInstance().getCurrentLevel() == null) {
			LevelManager.getInstance().load(LevelInstance.TEST_MOVEMENT);
		}
		level = LevelManager.getInstance().getCurrentLevel();

		Object[] returnValues = getShortestTimeToCollision(level, delta);
		float shortestTimeToCollision = (Float)(returnValues[0]);
		Rectangle boundingBox = (Rectangle)returnValues[1];
		Rectangle closestTile = (Rectangle)returnValues[2];
		// Round Two: Check for tiles on the horizontal sides, THEN check on the
		// vertical sides

		// If shortestTimeToCollision == 1.0f, then there are no collisions to
		// begin with!
		if (shortestTimeToCollision == 1.0f) {
			getPosition().add(delta * getVelocity().x, delta * getVelocity().y, 0);
			return;
		}

		getPosition().add(shortestTimeToCollision * getVelocity().x, shortestTimeToCollision * getVelocity().y, 0);
		// Get the new normal vectors from the future collision:
		// nextIteration returns the timeToCollision, as well as the new normal
		// velocity vector
		// TODO: Getting the altered velocity should be different than recalling
		// this method.. FIX!
		SweptAABB.nextIteration(boundingBox, closestTile, getVelocity(), delta);

		// Now, do the last iteration to check for a second collision (Corners
		// are an example of this)
//		returnValues = getShortestTimeToCollision(level, delta - shortestTimeToCollision);
//		shortestTimeToCollision = (Float)(returnValues[0]);
//		getPosition().add(shortestTimeToCollision * getVelocity().x, shortestTimeToCollision * getVelocity().y, 0);
	}

	private Object[] getShortestTimeToCollision(BaseLevel level, float delta) {
		// Plan: Find the shortest timeToCollision for any direction. Then
		// calculate the distance
		// after the time to collision. Afterwards there can only be one
		// direction, so a follow up
		// detection would be needed just in case.
		
		Rectangle boundingBox = new Rectangle(getPosition().x, getPosition().y, WIDTH, HEIGHT);
		float timeToCollision = delta;
		
		tiles.clear();
		// Get the tiles to the left and right of the player
		startX = (int) ((this.getPosition().x) / this.WIDTH) - 2;
		endX = startX + 4;
		startY = (int) (this.getPosition().y / this.HEIGHT) - 1;
		endY = startY + 2;
		level.getTiles(startX, startY, endX, endY, tiles);
		Array<Rectangle> tmpTilesArray = new Array<Rectangle>(tiles);

		// Get the tiles above and below the player
		startY = (int) ((this.getPosition().y) / this.HEIGHT) - 1;
		endY = (int) ((this.getPosition().y) / this.HEIGHT) + 2;
		startX = (int) (this.getPosition().x / this.WIDTH) - 1;
		endX = startX + 2;
		level.getTiles(startX, startY, endX, endY, tiles);
		// Add the tiles into one Array
		tiles.addAll(tmpTilesArray);
		tmpTilesArray.clear();

		// Get the first iteration of the shortest time to Collision
		float shortestTimeToCollision = 1.0f;
		Rectangle closestTile = null;
		Vector2 tmpVelocity = null;
		for (Rectangle tile : tiles) {
			tmpVelocity = getVelocity().cpy().scl(2);
			if (SweptAABB.broadPhaseCollisionCheck(boundingBox, tile, tmpVelocity, delta)) {
				timeToCollision = SweptAABB.nextIteration(boundingBox, tile, tmpVelocity, delta);
				if (shortestTimeToCollision >= timeToCollision) {
					shortestTimeToCollision = timeToCollision;
					closestTile = tile;
				}
				collidedTiles.add(tile);
			}
		}
		return new Object[]{shortestTimeToCollision, boundingBox, closestTile};
	}

	@Override
	public void draw(Batch batch) {
		TextureRegion frame = null;
		state = walking ? state.Walking : state.Standing;
		switch (this.state) {
		case Standing:
			frame = playerStand.getKeyFrame(this.stateTime);
			break;
		case Walking:
			frame = playerWalkDown.getKeyFrame(this.stateTime);
			break;
		default:
			frame = playerWalkUp.getKeyFrame(this.stateTime);
			break;
		}

		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.GRAY);
		shapeRenderer.rect(getPosition().x, getPosition().y, WIDTH, HEIGHT);
		shapeRenderer.end();

		// batch.enableBlending();
		// batch.begin();
		// batch.draw(frame, super.getPosition().x, super.getPosition().y,
		// this.WIDTH,
		// this.HEIGHT);
		// batch.end();
	}

	// Draw Debug squares around the player;
	// All squares default to green;
	// Red squares indicate a collision with a tile
	private int startX, startY, endX, endY;
	private Array<Rectangle> tiles = new Array<Rectangle>();

	public void debug(Camera camera, BaseLevel level) {
		if (collidedTiles.size != 0) {
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeType.Filled);
			for (Rectangle tile : collidedTiles) {
				shapeRenderer.setColor(Color.LIGHT_GRAY);
				shapeRenderer.rect(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight());
			}
			shapeRenderer.end();
		}
	}

	public void draw(Camera camera) {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.rect(super.getPosition().x, super.getPosition().y, this.WIDTH, this.HEIGHT);
		shapeRenderer.end();
	}

	public void setWalking(boolean isWalking) {
		this.walking = isWalking;
	}

	public boolean isWalking() {
		return this.walking;
	}
}
