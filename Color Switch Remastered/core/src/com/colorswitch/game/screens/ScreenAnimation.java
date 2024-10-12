package com.colorswitch.game.screens;

@FunctionalInterface
public interface ScreenAnimation {
	abstract void execute(float fadeSpeed, float deltaTime);
}
