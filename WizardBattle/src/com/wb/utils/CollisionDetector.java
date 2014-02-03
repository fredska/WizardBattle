package com.wb.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface CollisionDetector {
	public boolean detectCollision(Rectangle one, Rectangle two,Vector2 velocityOne, float delta);
}
