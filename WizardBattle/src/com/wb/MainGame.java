package com.wb;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.wb.level.WorldManager;
import com.wb.screens.ScreenManager;
import com.wb.screens.WB_Screen;

public class MainGame extends Game {
	
	@Override
	public void create() {		
		
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().show(WB_Screen.GAME);
	}

	@Override
	public void dispose() {
		super.dispose();
		ScreenManager.getInstance().dispose();
	}
}
