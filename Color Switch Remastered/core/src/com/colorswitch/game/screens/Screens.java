package com.colorswitch.game.screens;

import com.badlogic.gdx.utils.Null;

public enum Screens {
	MAIN_MENU(MainMenu.class),
	SETTINGS(Settings.class),
	CHALLENGES(Challenges.class),
	INFO(Info.class),
	PRIZES(Prizes.class),
	GAME_MODES(GameModes.class),
	PREVIOUS(Previous.class);

	private final Class<? extends Screen> classSource;

	Screens(Class<? extends Screen> classSource) {
		this.classSource = classSource;
	}

	public Class<? extends Screen> getClassSource() {
		return classSource;
	}

	@Null
	public static Screens fromInstance(Screen instance) {
		for(Screens screen : Screens.values()) {
			if(screen.classSource == instance.getClass()) {
				return screen;
			}
		}
		return null;
	}

}
