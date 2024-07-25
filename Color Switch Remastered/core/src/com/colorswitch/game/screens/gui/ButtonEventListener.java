package com.colorswitch.game.screens.gui;

public interface ButtonEventListener {
	abstract void onButtonClicked(Button button, float xpos, float ypos);
	abstract void onHover(Button button, float xpos, float ypos);
	abstract void onHoverOut(Button button, float xpos, float ypos);
}
