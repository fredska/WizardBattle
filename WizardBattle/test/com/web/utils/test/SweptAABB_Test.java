package com.web.utils.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.wb.utils.CollisionDetector;
import com.wb.utils.SweptAABB;


public class SweptAABB_Test {

	private Rectangle b1;
	private Vector2 b1Velocity;
	@Before
	public void setup(){
		b1 = new Rectangle(0,0,10,10);
		b1Velocity = new Vector2(100, 40);
	}
	
	/**
	 * This test will be for a valid collision between two rectangles
	 * with one rectangle having a velocity and the other with no velocity
	 */
	@Test
	public void SweptAABB_Valid_Test() {		
		//b2 is stationary
		Rectangle b2 = new Rectangle(60, 20, 10, 1000);
	
		
		//deltaTime is in (arbitrary) seconds between frames
		float deltaTime = 1;
		
		float timeToCollision = SweptAABB.nextIteration(b1, b2, b1Velocity, deltaTime);
		
		assertNotEquals(1.0f, timeToCollision);
		assertEquals(0.5f, timeToCollision, 0.0f);
		
		//Check to see if the velocity is corrected;
		assertEquals(new Vector2(0,40), b1Velocity);
	}
	
	@Test
	public void collisionInPositiveXDirection(){
		b1Velocity = new Vector2(100, 0); //Set only in the Positive X direction
		//b2 is stationary
		Rectangle b2 = new Rectangle(90, 20, 10, 1000);
	
		
		//deltaTime is in (arbitrary) seconds between frames
		float deltaTime = 1;
		
		float timeToCollision = SweptAABB.nextIteration(b1, b2, b1Velocity, deltaTime);
		assertNotEquals(1.0f, timeToCollision);
		assertEquals(0.8f, timeToCollision, 0.0f);
		
		assertEquals(Vector2.Zero, b1Velocity);
	}
	
	@Test
	public void NoCollisionTest(){
		
		Rectangle b2 = new Rectangle(500,500, 10, 10);
		
		float delta = 1;
		
		float timeToCollision = SweptAABB.nextIteration(b1, b2, b1Velocity, delta);
		
		assertFalse(SweptAABB.broadPhaseCollisionCheck(b1, b2, b1Velocity, delta));
		
		assertEquals(1.0f, timeToCollision, 0.0f);
	}
	
	/**
	 * This test is for two rectangles that have already collided with each other
	 */
	@Test
	public void immediateCollisionTest(){
		final Vector2 velocityChecker = b1Velocity;
		//Check if rectangle is directly above the player
		Rectangle b2 = new Rectangle(0,10,10,10);
		float delta = 1f;
		
		assertTrue(SweptAABB.broadPhaseCollisionCheck(b1, b2, b1Velocity, delta));
		
		//There should be no time lost 
		assertEquals(0.0f, SweptAABB.nextIteration(b1, b2, b1Velocity, delta), 0.0f);
		//Validate that the velocity is only in the +X direction;
		assertEquals(new Vector2(velocityChecker.x, 0), b1Velocity);
		
		//Reset b1Velocity;
		b1Velocity = velocityChecker;
		//Check if tile is redirectly in the +X direction;
		b2 = new Rectangle(10,0,10,10);
		assertTrue(SweptAABB.broadPhaseCollisionCheck(b1, b2, b1Velocity, delta));
		
		//There should be no time lost 
		assertEquals(0.0f, SweptAABB.nextIteration(b1, b2, b1Velocity, delta), 0.0f);
		//Validate that the velocity is only in the +Y direction;
		assertEquals(new Vector2(0,velocityChecker.y), b1Velocity);
		
		//Reset b1Velocity;
		b1Velocity = velocityChecker;
		
		//Validate that if a tile is on the exact corner (top right to bottom left)
		// that b1 still has control of where to go;
		b2 = new Rectangle(10,10,10,10);
		assertTrue(!SweptAABB.broadPhaseCollisionCheck(b1, b2, b1Velocity, delta));
		
		//There should be no time lost 
		assertEquals(1.0f, SweptAABB.nextIteration(b1, b2, b1Velocity, delta), 0.0f);
	}
	
	//Since the object has overlapped the tile, 
	@Test
	public void overlapCollisionTest(){
		Rectangle b2 = new Rectangle(5,5,10,10);
		float delta = 1f;
		assertTrue(SweptAABB.broadPhaseCollisionCheck(b1, b2, b1Velocity, delta));
		
		assertEquals(1.0f, SweptAABB.nextIteration(b1, b2, b1Velocity, delta), 0.0f);
	}

}
