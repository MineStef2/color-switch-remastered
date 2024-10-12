package com.colorswitch.game;

public class WindowUtils {
	public static float getCenterX(float componentWidth) {
		return (ColorSwitch.getInstance().getConfig().window.width - componentWidth) / 2;
	}

	public static float getCenterY(float componentHeight) {
		return (ColorSwitch.getInstance().getConfig().window.height - componentHeight) / 2;
	}
}
