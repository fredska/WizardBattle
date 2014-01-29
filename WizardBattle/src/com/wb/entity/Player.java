package com.wb.entity;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {
	public final float WIDTH;
	public final float HEIGHT;
	static float MAX_VELOCITY = 10f;
	static float JUMP_VELOCITY = 40f;
	static float DAMPING = 0.75f;

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
				Gdx.files.internal("art/sprites/wizard/smr1/smr1_fr1.gif")));
		playerStand = new Animation(0, stand);

		// Initialize the animation for walking upward ;
		playerWalkUp = compileAnimation("art/sprites/wizard/smr1/smr1_bk1.gif",
				"art/sprites/wizard/smr1/smr1_bk2.gif");
		playerWalkLeft = compileAnimation(
				"art/sprites/wizard/smr1/smr1_lf1.gif",
				"art/sprites/wizard/smr1/smr1_lf2.gif");
		playerWalkDown = compileAnimation(
				"art/sprites/wizard/smr1/smr1_fr1.gif",
				"art/sprites/wizard/smr1/smr1_fr2.gif");
		playerWalkRight = compileAnimation(
				"art/sprites/wizard/smr1/smr1_rt1.gif",
				"art/sprites/wizard/smr1/smr1_rt2.gif");
		this.WIDTH = 32f;
		this.HEIGHT = 32f;
		
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
	
	public void setWalking(boolean isWalking){
		this.walking = isWalking;
	}

	public boolean isWalking() {
		return this.walking;
	}
}
