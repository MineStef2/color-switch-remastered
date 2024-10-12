package com.colorswitch.game.gui.button;

@FunctionalInterface
public interface ButtonHoverBehavior {
	abstract void onHover(Button button, float xpos, float ypos);
}
