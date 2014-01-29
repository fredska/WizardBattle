package com.wb.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public abstract class Entity {

	private boolean collidable;
	private Vector3 position;
	
	public Entity(boolean collidable){
		this.collidable = collidable;
	}
	
	public abstract void draw(Batch batch);
	
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
}
