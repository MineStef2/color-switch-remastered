package com.colorswitch.game.screens;

import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.ScreenManager;

@FunctionalInterface
public interface ScreenAnimationImplementation{

	/**
	 * Must contain screenManager.invalidateScreenAnimation() after completing the
	 * animation, and a game.setScreen() call to actually change the game's active
	 * screen.
	 *
	 * @param targetScreen the next screen that is determined by the animation
	 */
	abstract void animate(float fadeSpeed, float deltaTime, ScreenManager screenManager, ScreenFadingAssistant fadingAssistant, ColorSwitch game, Screen targetScreen);
}
