package com.wb.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Based on 
 * http://www.gamedev.net/page/resources/_/technical/game-programming/swept-aabb-collision-detection-and-response-r3084
 * 
 * Note that the position is based on the top-left corner, where the 
 * drawing starts at the bottom left corner
 * @author fskallos
 *
 */
public class SweptAABB implements CollisionDetector {

	@Override
	public boolean detectCollision(Rectangle b1, Rectangle b2, Vector2 b1Velocity, float delta) {
		return nextIteration(b1, b2, b1Velocity, delta) != 1.0f;
	}
	
	/**
	 * Use the Swept AABB collision calculation to detect any collisions.
	 * 
	 * If a collision is detected, the velocity will be updated and the 
	 * remaining time will be returned for any future iterations.
	 * @param b1
	 * @param b2
	 * @param b1Velocity
	 * @param delta
	 * @return
	 */
	public float nextIteration(Rectangle b1, Rectangle b2, Vector2 b1Velocity, float delta) {
		float xInvEntry, yInvEntry;
		float xInvExit, yInvExit;
		
		//Find the distance between the objects on the near and far sides for both x & y
		if(b1Velocity.x > 0.0f){
	        xInvEntry = b2.x - (b1.x + b1.width);
	        xInvExit = (b2.x + b2.width) - b1.x;
		} else {
			xInvEntry = (b2.x + b2.width)- b1.x;
			xInvExit = b2.x - (b1.x + b1.width);
		}
		
		if(b1Velocity.y > 0.0f){
			yInvEntry = b2.y - (b1.y + b1.height);
			yInvExit = (b2.y + b2.height) - b1.y;
		} else {
			yInvEntry = (b2.y + b2.height) - b1.y;
			yInvExit = b2.y - (b1.y + b1.height);
		}
		
		// find time of collision and time of leaving for each axis (if statement is to prevent divide by zero)
		float xEntry, yEntry;
		float xExit,  yExit;
		
		if(b1Velocity.x == 0.0f){
			xEntry = -Float.MAX_VALUE;
			xExit = Float.MAX_VALUE;
		} else {
			xEntry = xInvEntry / b1Velocity.x;
			xExit = xInvExit / b1Velocity.x;
		}
		
		if(b1Velocity.y == 0.0f){
			yEntry = -Float.MAX_VALUE;
			yExit = Float.MAX_VALUE;
		} else {
			yEntry = yInvEntry / b1Velocity.y;
			yExit = yInvExit / b1Velocity.y;
		}
		
		//Now, find if there has been any time between frames that the rectangles
		// collide with each other;
		float entryTime = xEntry >= yEntry? xEntry: yEntry;
		float exitTime = xExit >= yExit? xExit: yExit;
		
		// if there was no collision
	    if (entryTime > exitTime || xEntry < 0.0f && yEntry < 0.0f || xEntry > 1.0f || yEntry > 1.0f){
	    	return 1.0f;
	    }
	    
	    // calculate normal of collided surface
	    // and do the slide :)
	    if(xEntry < yEntry){
			b1Velocity.set(xInvEntry < 0f?-b1Velocity.x: b1Velocity.x, 0);
		} else {
			b1Velocity.set(0,yInvEntry < 0f? -b1Velocity.y: b1Velocity.y);
		}
	    return entryTime;
	}
	
	public boolean broadPhaseCollisionCheck(Rectangle b1, Rectangle b2, Vector2 b1Velocity, float delta){
		Rectangle broadPhaseBox = new Rectangle();
		broadPhaseBox.x = b1Velocity.x > 0 ? b1.x: b1.x + b1Velocity.x * delta;
		broadPhaseBox.y = b1Velocity.y > 0 ? b1.y: b1.y + b1Velocity.y * delta;
		broadPhaseBox.width = b1Velocity.x > 0 ? b1Velocity.x * delta + b1.width: b1.width - (b1Velocity.x * delta);
		broadPhaseBox.height = b1Velocity.y > 0 ? b1Velocity.y * delta + b1.height: b1.height - (b1Velocity.y * delta);
		
		return broadPhaseBox.overlaps(b2);
	}
}
