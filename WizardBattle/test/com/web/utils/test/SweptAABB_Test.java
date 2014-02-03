package com.web.utils.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.wb.utils.CollisionDetector;
import com.wb.utils.SweptAABB;


public class SweptAABB_Test {

	private SweptAABB collisionDetector;
	@Before
	public void setup(){
		collisionDetector = new SweptAABB();
	}
	
	/**
	 * This test will be for a valid collision between two retangles
	 * with one rectangle having a velocity and the other with no velocity
	 */
	@Test
	public void SweptAABB_Valid_Test() {
		//b1 is the box that will move
		Rectangle b1 = new Rectangle(0, 0, 10, 10);
		Vector2 b1Velocity = new Vector2(100, 40);
		
		//b2 is stationary
		Rectangle b2 = new Rectangle(60, 20, 10, 1000);
	
		
		//deltaTime is in (arbitrary) seconds between frames
		float deltaTime = 1;
		
		float timeToCollision = collisionDetector.nextIteration(b1, b2, b1Velocity, deltaTime);
		
		assertNotEquals(1.0f, timeToCollision);
		assertEquals(0.5f, timeToCollision, 0.0f);
		
		//Check to see if the velocity is corrected;
		assertEquals(new Vector2(0,40), b1Velocity);
	}
	
	@Test
	public void NoCollisionTest(){
		//This test should return a 1.0f for no collisions
		Rectangle b1 = new Rectangle(0,0,10,10);
		Vector2 b1Velocity = new Vector2(10,10);
		
		Rectangle b2 = new Rectangle(500,500, 10, 10);
		
		float delta = 1;
		
		float timeToCollision = new SweptAABB().nextIteration(b1, b2, b1Velocity, delta);
		
		assertFalse(collisionDetector.broadPhaseCollisionCheck(b1, b2, b1Velocity, delta));
		
		assertEquals(1.0f, timeToCollision, 0.0f);
	}

}
