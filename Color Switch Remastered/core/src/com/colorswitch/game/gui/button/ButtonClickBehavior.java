package com.colorswitch.game.gui.button;

@FunctionalInterface
public interface ButtonClickBehavior {
	abstract void onClick(Button button, float xpos, float ypos);
}
