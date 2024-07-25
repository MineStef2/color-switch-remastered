package com.colorswitch.game.screens.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.screens.Screen;

public class FadingButton extends Button{
	protected boolean isAtMaxOpacity;

	public FadingButton(Texture texture, Screen owner, Vector2 scale) {
		super(texture, owner, scale);
		this.isAtMaxOpacity = true;
	}

	public void updateFade() {
		if(this.getColor().a == 1f) {
			this.isAtMaxOpacity = true;
		}
		if(this.isAtMaxOpacity && this.getColor().a > 0.15f) {
			this.doFadeAnimation(-0.01f);
		}else {
			this.isAtMaxOpacity = false;
		}

		if(!this.isAtMaxOpacity && this.getColor().a < 1f) {
			this.doFadeAnimation(+0.015f);
		}
	}

	public boolean isAtMaxOpacity() {
		return isAtMaxOpacity;
	}
}
