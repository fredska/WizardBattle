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
//	static float MAX_VELOCITY = 10f;
//	static float JUMP_VELOCITY = 40f;
//	static float DAMPING = 0.75f;

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
	private Animation playerWalkUp, playerWalkLeft, playerWalkDown,
			playerWalkRight;

	private Texture playerStandTexture;
	private Sprite sprite;
	

	public Player() {
		super(true);

		// Only need one frame to show the player has stopped
		TextureRegion stand = new TextureRegion(new Texture(
				Gdx.files.internal("art/sprites/wizard/smr1/smr1_fr1.png")));
		playerStand = new Animation(0, stand);

		// Initialize the animation for walking upward ;
		playerWalkUp = compileAnimation("art/sprites/wizard/smr1/smr1_bk1.gif",
				"art/sprites/wizard/smr1/smr1_bk2.gif");
		playerWalkLeft = compileAnimation(
				"art/sprites/wizard/smr1/smr1_lf1.gif",
				"art/sprites/wizard/smr1/smr1_lf2.gif");
		playerWalkDown = compileAnimation(
				"art/sprites/wizard/smr1/smr1_fr1.png",
				"art/sprites/wizard/smr1/smr1_fr2.png");
		playerWalkRight = compileAnimation(
				"art/sprites/wizard/smr1/smr1_rt1.gif",
				"art/sprites/wizard/smr1/smr1_rt2.gif");
		super.setHeight(32f);
		super.setWidth(32f);
		this.WIDTH = 32f;
		this.HEIGHT = 32f;
		shapeRenderer = new ShapeRenderer();
	}

	private Animation compileAnimation(String... spriteFilePath) {
		List<TextureRegion> spriteFrames = new LinkedList<TextureRegion>();
		for (String filePath : spriteFilePath) {
			spriteFrames.add(new TextureRegion(new Texture(Gdx.files
					.internal(filePath))));
		}
		TextureRegion[] regions = new TextureRegion[spriteFrames.size()];
		regions = spriteFrames.toArray(regions);
		Animation result = new Animation(0.15f, regions);
		result.setPlayMode(Animation.LOOP_PINGPONG);
		return result;
	}

	private SweptAABB collisionDetector = new SweptAABB();
	/**
	 * Update any information of the player on a frame by frame basis
	 * This includes movement, health, mana, animation changes 
	 */
	@Override
	public void update(float delta){
		this.stateTime += delta;
//		System.out.println("Velocity: " + getVelocity());
		
		//Get all nearby tiles
		//First load the level data;
		BaseLevel level = null;
		if(LevelManager.getInstance().getCurrentLevel() == null) {
			LevelManager.getInstance().load(LevelInstance.TEST_MOVEMENT);
		}
		level = LevelManager.getInstance().getCurrentLevel();
		tiles = new Array<Rectangle>();
		tiles.clear();
		startX = (int)((this.getPosition().x* 1.5) / this.WIDTH) - 2;
		endX = (int)((this.getPosition().x* 1.5) / this.WIDTH) + 2;
		startY = (int)((this.getPosition().y) / this.HEIGHT) - 2;
		endY = (int)((this.getPosition().y) / this.HEIGHT) + 2;
		level.getTiles(startX, startY, endX, endY, tiles);
		
		Rectangle boundingBox = new Rectangle(getPosition().x, getPosition().y, WIDTH, HEIGHT);
		boolean collisionDetected = false;
		for(Rectangle tile : tiles){
			//Quickly check to see if there is a collision
			tile.height *= 32;
			tile.width *= 32;
			tile.x *= 32;
			tile.y *= 32;
			if(collisionDetector.broadPhaseCollisionCheck(boundingBox, tile, getVelocity(), delta)){
				collisionDetected = true;
				//If so, then it can be determined that there will be a time of collision
				float timeToCollision = 0f;
				//Make a copy of the current Velocity
				Vector2 tmpVelocity = getVelocity().cpy();
				//Get the time till collision
				timeToCollision = collisionDetector.nextIteration(boundingBox, tile, getVelocity(), delta);
				//Move the player from the start time till delta with the given velocity
				getPosition().add((delta - timeToCollision) * tmpVelocity.x, (delta - timeToCollision) * tmpVelocity.y, 0);
				
				//Now, with the new velocity from the detector, move the player the rest of the way
				getPosition().add(timeToCollision * getVelocity().x, timeToCollision * getVelocity().y, 0);
				break;
			}
		}
		
		//Update the player's position if no collisions were detected;
		if(!collisionDetected)
			getPosition().add(delta * getVelocity().x, delta * getVelocity().y, 0);
		
	}
	
	@Override
	public void draw(Batch batch) {
		TextureRegion frame = null;
		state = walking?state.Walking : state.Standing;
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
		
//		batch.enableBlending();
//		batch.begin();
//		batch.draw(frame, super.getPosition().x, super.getPosition().y, this.WIDTH,
//				this.HEIGHT);
//		batch.end();
	}
	
	//Draw Debug squares around the player;
	//All squares default to green;
	//Red squares indicate a collision with a tile
	private int startX, startY, endX, endY;
	private Array<Rectangle> tiles = new Array<Rectangle>();
	public void debug(Camera camera, BaseLevel level){

		startX = (int)((this.getPosition().x* 1.5) / this.WIDTH) - 2;
		endX = (int)((this.getPosition().x* 1.5) / this.WIDTH) + 2;
		startY = (int)((this.getPosition().y) / this.HEIGHT) - 2;
		endY = (int)((this.getPosition().y) / this.HEIGHT) + 2;
		
		level.getTiles(startX, startY, endX, endY, tiles);
		System.out.println("Number of Tile Collisions: " + tiles.size);
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.ORANGE);
		shapeRenderer.rect(startX*32, startY * 32, 32* (endX - startX),32* (endY - startY));
		shapeRenderer.end();
	}
	
	public void draw(Camera camera){
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.rect(super.getPosition().x, super.getPosition().y, this.WIDTH, this.HEIGHT);
		shapeRenderer.end();
	}
	
	public void setWalking(boolean isWalking){
		this.walking = isWalking;
	}

	public boolean isWalking() {
		return this.walking;
	}
}
