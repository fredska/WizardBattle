package com.wb.entity;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * An entity is presumed to be an axis-aligned square until further
 * notice :)
 * @author fskallos
 *
 */
public abstract class Entity {

	private boolean collidable;
	private boolean canMove;
	private Vector3 position;
	protected float HEIGHT;
	protected float WIDTH;
	
	protected Vector2 velocity;
	public Entity(boolean collidable){
		this.collidable = collidable;
		//Default the square to 32 x 32;
		setWidth(32f);
		setHeight(32f);
		velocity = Vector2.Zero;
	}
	
	public abstract void draw(Batch batch);
	
	public abstract void update(float delta);
	
	public void setPosition(Vector3 position){
		this.position = position;
	}
	
	public Vector3 getPosition(){
		return this.position;
	}
	
	public void translate(Vector3 translate){
		this.position = this.position.add(translate);
	}
	
	public void translate(Vector2 translate){
		this.position = this.position.add(translate.x, translate.y, 0);
	}
	
	public boolean isCollidable(){
		return this.collidable;
	}

	public float getHeight() {
		return HEIGHT;
	}

	public void setHeight(float height) {
		HEIGHT = height;
	}

	public float getWidth() {
		return WIDTH;
	}

	public void setWidth(float width) {
		WIDTH = width;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public Vector2 getCenter(){
		return new Vector2(getPosition().x + (getWidth() / 2f),
				getPosition().y + (getHeight() / 2f));
	}
	
	public Vector2[] getVerticies(){
		Vector2[] result = new Vector2[5];
		result[0] = getCenter();
		result[1] = new Vector2(getPosition().x + getWidth()
				, getPosition().y + getHeight());
		result[2] = new Vector2(getPosition().x + getWidth(),
				getPosition().y);
		result[3] = new Vector2(getPosition().x, getPosition().y);
		result[4] = new Vector2(getPosition().x, getPosition().y + getHeight());
		return result;
	}
	
}
