package com.wb.level;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.IntMap;

public class LevelManager {
	private static LevelManager instance;
	private Game game;
	private IntMap<BaseLevel> levels;
	private BaseLevel currentLevel;

	private LevelManager() {
		levels = new IntMap<BaseLevel>();
		currentLevel = null;
	}

	public static LevelManager getInstance() {
		if (instance == null) {
			instance = new LevelManager();
		}
		return instance;
	}

	public void initialize(Game game) {
		this.game = game;
	}

	public void load(LevelInstance levelInstance) {
		if (this.game == null)
			return;
		if (!levels.containsKey(levelInstance.ordinal())) {
			levels.put(levelInstance.ordinal(), levelInstance.getLevelInstance());
		}
		currentLevel = levels.get(levelInstance.ordinal());
	}

	public void dispose() {
		for (BaseLevel level : levels.values()) {
			// level.dispose();
		}
		levels.clear();
		instance = null;
		currentLevel = null;
	}

	public BaseLevel getCurrentLevel() {
		return currentLevel;
	}
}
