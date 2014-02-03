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
		Gdx.gl10.glClearColor(1,1,1,1);
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		//Update the camera position
		updatePlayer(delta);
		updateCamera(delta);
		
		//TODO:  Show Error screen here
		if(level == null) return;
		
		level.render(camera);
		player.draw(level.getRenderer().getSpriteBatch());
		System.out.println(player.getPosition());
//		player.draw(camera);
//		player.debug(camera, level);
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
		player.setPosition(new Vector3(64,64,0));
	}
	private Array<Rectangle> tiles = new Array<Rectangle>();
	
	private boolean collisionCheck(int startX, int startY, int endX, int endY, Array<Rectangle> tiles){
		level.getTiles(startX, startY, endX, endY, tiles);
		if(tiles.size > 0 && player.isCollidable()){
			//Check if the tile is above or below the character & block accordingly
			if(tiles.get(0).y >= endY){
				//Disable the upwards motion
				return false;
			}
		}
		return true;
	}
	
	boolean canMoveUp, canMoveDown, canMoveLeft, canMoveRight;
	private void updatePlayer(float delta){
		//Set the movement defaults to true;
		canMoveUp = canMoveDown = canMoveLeft = canMoveRight = true;
		
		player.update(delta);
		float modifier = 200f;
		
		int startX, startY, endX, endY;
		int padding = 3;
		startX = (int) ((player.getPosition().x ) / 32);
		endX = (int) ((player.getPosition().x ) / 32)+1;
		startY = (int) ((player.getPosition().y ) / 32);
		endY = (int) ((player.getPosition().y + player.HEIGHT + padding) / 32);
		canMoveUp = collisionCheck(startX, startY, endX, endY, tiles);
		
		startX = (int) ((player.getPosition().x ) / 32) ;
		endX = (int) ((player.getPosition().x ) / 32)+1;
		startY = (int) ((player.getPosition().y - player.HEIGHT ) / 32);
		endY = (int) ((player.getPosition().y  - padding) / 32);
		canMoveDown = collisionCheck(startX, startY, endX, endY, tiles);
		
		Vector2 changePosition = new Vector2();
		if (canMoveUp && (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W))) {
			changePosition.y += delta * modifier;
		}
		if (canMoveDown && (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S))) {
			changePosition.y -= delta * modifier;
		}
		
		startY = endY = (int) ((player.getPosition().y ) / 32);
		startX = (int) ((player.getPosition().x - padding) / 32);
		endX = (int) ((player.getPosition().x + player.WIDTH  + padding) / 32);
		level.getTiles(startX, startY, endX, endY, tiles);
		
		if(tiles.size > 0){
			if(tiles.get(0).x >= endX){
				//Disable the upwards motion
				canMoveRight = false;
			} else{
				//Disable the downward motion
				canMoveLeft = false;
			}
			
			if(tiles.size >= 2)
				canMoveLeft = canMoveRight = false;
		}
		if (canMoveLeft && (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A))) {
			changePosition.x -= delta * modifier;
		}
		if (canMoveRight && (Gdx.input.isKeyPressed(Keys.RIGHT)
				|| Gdx.input.isKeyPressed(Keys.D))) {
			changePosition.x += delta * modifier;
		}
		if(changePosition == Vector2.Zero)
			player.setWalking(false);
		else 
			player.setWalking(true);
		this.player.translate(changePosition);
		
//		System.out.println(canMoveUp + " :: " + canMoveDown + " :: " + canMoveLeft + " :: " + canMoveRight);
	}
	
	private void updateCamera(float delta){
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
