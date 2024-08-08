package com.colorswitch.game;

import com.colorswitch.game.screens.Screen;

@FunctionalInterface
public interface ScreenBinding {
	abstract Screen bind();
}
