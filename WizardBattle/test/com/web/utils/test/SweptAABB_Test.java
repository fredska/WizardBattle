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
	private Rectangle b1;
	private Vector2 b1Velocity;
	@Before
	public void setup(){
		collisionDetector = new SweptAABB();
		b1 = new Rectangle(0,0,10,10);
		b1Velocity = new Vector2(100, 40);
	}
	
	/**
	 * This test will be for a valid collision between two retangles
	 * with one rectangle having a velocity and the other with no velocity
	 */
	@Test
	public void SweptAABB_Valid_Test() {		
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
		
		Rectangle b2 = new Rectangle(500,500, 10, 10);
		
		float delta = 1;
		
		float timeToCollision = collisionDetector.nextIteration(b1, b2, b1Velocity, delta);
		
		assertFalse(collisionDetector.broadPhaseCollisionCheck(b1, b2, b1Velocity, delta));
		
		assertEquals(1.0f, timeToCollision, 0.0f);
	}
	
	/**
	 * This test is for two rectangles that have already collided with each other
	 */
	@Test
	public void immediateCollisionTest(){
		Rectangle b2 = new Rectangle(10,10,10,10);
		float delta = 1f;
		
		assertTrue(collisionDetector.broadPhaseCollisionCheck(b1, b2, b1Velocity, delta));
		
		//There should be no time lost 
		assertEquals(0.0f, collisionDetector.nextIteration(b1, b2, b1Velocity, delta), 0.0f);
	}
	
	@Test
	public void overlapCollisionTest(){
		
	}

}