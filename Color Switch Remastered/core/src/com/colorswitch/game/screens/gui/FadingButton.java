package com.colorswitch.game.screens.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.screens.Screen;

public class FadingButton extends Button{
	private boolean isAtMaxOpacity;

	public FadingButton(Texture texture, Vector2 position, Vector2 scale, Screen owner) {
		super(texture, position, scale, owner);
		this.isAtMaxOpacity = true;
	}

	public FadingButton(Texture texture, Vector2 position, Screen owner) {
		this(texture, position, new Vector2(1f, 1f), owner);
	}

	public FadingButton(Texture texture, Screen owner) {
		this(texture, new Vector2(0, 0), new Vector2(1f, 1f), owner);
	}

	public void doAnimation(float fadeAmount) {
		this.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a + fadeAmount);
	}

	public void updateFade() {
		if(this.getColor().a == 1f) {
			isAtMaxOpacity = true;
		}
		if(isAtMaxOpacity && this.getColor().a > 0.15f) {
			this.doAnimation(-0.01f);
		}else {
			isAtMaxOpacity = false;
		}

		if(!isAtMaxOpacity && this.getColor().a < 1f) {
			this.doAnimation(+0.015f);
		}
	}

	public boolean isAtMaxOpacity() {
		return isAtMaxOpacity;
	}
}
