package com.wb.screens;

public enum WB_Screen {

	INTRO {
		@Override
		protected com.badlogic.gdx.Screen getScreenInstance(){
			return new IntroScreen();
		}
	},
	MAIN_MENU{
		@Override
		protected com.badlogic.gdx.Screen getScreenInstance(){
			return new MainMenuScreen();
		}
	},
	CREDITS{
		@Override
		protected com.badlogic.gdx.Screen getScreenInstance(){
			return new CreditsScreen();
		}
	};
	
	protected abstract com.badlogic.gdx.Screen getScreenInstance();
}
