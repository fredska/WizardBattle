package com.wb.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public abstract class Projectile extends Entity {

	private Vector2 velocity;

	public Projectile(boolean collidable) {
		super(collidable);
	}

	public abstract void draw(Batch batch);

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public Vector2 getVelocity() {
		if (this.velocity == null)
			this.velocity = new Vector2();
		return this.velocity;
	}
}
