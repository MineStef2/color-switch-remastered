package com.colorswitch.game.gui.button;

@FunctionalInterface
public interface ButtonHoverOutBehavior {
	abstract void onHoverOut(Button button, float xpos, float ypos);
}
