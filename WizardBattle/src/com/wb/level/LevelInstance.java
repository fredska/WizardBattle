package com.wb.level;

public enum LevelInstance {
	TEST_MOVEMENT{
		@Override
		protected BaseLevel getLevelInstance() {
			return new TestMovementLevel();
		}
	};
	
	protected abstract BaseLevel getLevelInstance();
}
