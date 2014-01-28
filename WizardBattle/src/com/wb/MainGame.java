package com.wb;

import com.badlogic.gdx.Game;
import com.wb.screens.ScreenManager;
import com.wb.screens.WB_Screen;

public class MainGame extends Game {
	
	@Override
	public void create() {		
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().show(WB_Screen.INTRO);
	}

	@Override
	public void dispose() {
		super.dispose();
		ScreenManager.getInstance().dispose();
	}
}
