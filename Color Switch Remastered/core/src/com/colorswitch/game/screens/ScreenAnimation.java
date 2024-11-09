package com.colorswitch.game.screens;

import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.ScreenManager;

public class ScreenAnimation {
	private final ScreenAnimationImplementation animation;
	private final float speed;
	public static final ScreenAnimation DEFAULT_ANIMATION = new ScreenAnimation((fadeSpeed, deltaTime, screenManager, fadingAssistant, game, targetScreen) -> {
		if ((Screen) game.getScreen() != targetScreen && fadingAssistant.getAlpha() < 1) {
			fadingAssistant.fadeIn(fadeSpeed, deltaTime);
		} else if (fadingAssistant.getAlpha() == 1) {
			game.setScreen(targetScreen);
			fadingAssistant.fadeOut(fadeSpeed, deltaTime);
		} else {
			fadingAssistant.fadeOut(fadeSpeed, deltaTime);
		}
		if ((Screen) game.getScreen() == targetScreen && fadingAssistant.getAlpha() == 0) {
			screenManager.invalidateScreenAnimation();
		}
	}, ScreenManager.SCREEN_FADE_SPEED);

	public ScreenAnimation(ScreenAnimationImplementation animation, float speed) {
		this.animation = animation;
		this.speed = speed;
	}

	public void animate(float deltaTime, ScreenManager screenManager, ScreenFadingAssistant fadingAssistant, ColorSwitch game, Screen targetScreen) {
		this.animation.animate(this.speed, deltaTime, screenManager, fadingAssistant, game, targetScreen);
	}

	public float getSpeed() {
		return speed;
	}
}
