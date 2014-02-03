package com.wb.entity;

import java.util.LinkedList;
import java.util.List;

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

	public void update(float delta){
		this.stateTime += delta;
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
		batch.enableBlending();
		batch.begin();
		batch.draw(frame, super.getPosition().x, super.getPosition().y, this.WIDTH,
				this.HEIGHT);
		batch.end();
	}
	
	//Draw Debug squares around the player;
	//All squares default to green;
	//Red squares indicate a collision with a tile
	private int startX, startY, endX, endY;
	private Array<Rectangle> tiles = new Array<Rectangle>();
	public void debug(Camera camera, BaseLevel level){

		startX = (int)((this.getPosition().x* 1.5) / this.WIDTH) - 1;
		endX = (int)((this.getPosition().x* 1.5) / this.WIDTH) + 1;
		startY = (int)((this.getPosition().y) / this.HEIGHT) - 1;
		endY = (int)((this.getPosition().y) / this.HEIGHT) + 1;
		
		level.getTiles(startX, startY, endX, endY, tiles);
		System.out.println("Number of Tile Collisions: " + tiles.size);
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.ORANGE);
		shapeRenderer.rect(startX*32, startY * 32, 32* (endX - startX),32* (endY - startY));
		shapeRenderer.end();
	}
	
	public static boolean collisionCheck(int startX, int startY, int endX, int endY, BaseLevel level, Array<Rectangle> tiles){
		level.getTiles(startX, startY, endX, endY, tiles);
		if(tiles.size > 0){
			//Check if the tile is above or below the character & block accordingly
			if(tiles.get(0).y >= endY || tiles.get(0).x >= endX){
				//Disable the upwards motion
				return false;
			}
		}
		return true;
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
