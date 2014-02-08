package com.wb.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class WorldManager {
	private World world;
	private static WorldManager instance;

	public WorldManager() {
		this.world = null;
	}

	public static WorldManager getInstance() {
		if (instance == null) {
			instance = new WorldManager();
		}
		instance.world = new World(new Vector2(0, 0), false);
		return instance;
	}
	
	public void nextStep(){
		if(world == null) return;
		
		world.step(1/60f, 6, 2);
	}

	public void dispose() {
		world.dispose();
	}

	public World getWorld() {
		return this.world;
	}
}
