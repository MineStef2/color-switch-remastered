package com.colorswitch.game.gui;

import com.colorswitch.game.DrawCall;
import com.colorswitch.game.screens.Screen;

public abstract class CompositeGUIComponent implements DrawCall{
	private final Screen owner;

	public CompositeGUIComponent(Screen owner) {
		this.owner = owner;
		this.owner.addDrawCall(this);
	}

	public Screen getOwner() {
		return owner;
	}
}
