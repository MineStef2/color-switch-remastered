package com.colorswitch.game.gui.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.screens.Screen;

public class FadingButton extends Button {
	private boolean isAtMaxOpacity;
	private float minOpacity;
	private float maxOpacity;
	private float fadeSpeed;
	private float secondaryFadeSpeed;

	public FadingButton(Texture texture, Vector2 position, Vector2 scale, Screen owner, float minOpacity,
						float maxOpacity, float fadeSpeed, float secondaryFadeSpeed) {
		super(texture, position, scale, owner);
		this.isAtMaxOpacity = true;
		this.minOpacity = minOpacity;
		this.maxOpacity = maxOpacity;
		this.fadeSpeed = fadeSpeed;
		this.secondaryFadeSpeed = secondaryFadeSpeed;
	}

	public FadingButton(Texture texture, Vector2 position, Screen owner, float minOpacity, float maxOpacity,
						float fadeSpeed, float secondaryFadeSpeed) {
		this(texture, position, new Vector2(1f, 1f), owner, minOpacity, maxOpacity, fadeSpeed, secondaryFadeSpeed);
	}

	public FadingButton(Texture texture, Screen owner, float minOpacity, float maxOpacity, float fadeSpeed,
						float secondaryFadeSpeed) {
		this(texture, new Vector2(0, 0), new Vector2(1f, 1f), owner, minOpacity, maxOpacity, fadeSpeed,
				secondaryFadeSpeed);
	}

	public FadingButton(Texture texture, Screen owner, float minOpacity, float maxOpacity, float fadeSpeed) {
		this(texture, owner, minOpacity, maxOpacity, fadeSpeed, 0);
	}

	public FadingButton(Texture texture, Vector2 position, Screen owner, float minOpacity, float maxOpacity,
						float fadeSpeed) {
		this(texture, position, owner, minOpacity, maxOpacity, fadeSpeed, 0);
	}

	public FadingButton(Texture texture, Vector2 position, Vector2 scale, Screen owner, float minOpacity,
						float maxOpacity, float fadeSpeed) {
		this(texture, position, scale, owner, minOpacity, maxOpacity, fadeSpeed, 0);
	}

	private void doAnimation(float fadeSpeed) {
		this.setColor(this.getColor().add(0, 0, 0, fadeSpeed));
	}

	public void updateFade() {
		float actualFadeSpeed = this.fadeSpeed * Gdx.graphics.getDeltaTime();
		float actualSecondarySpeed = this.secondaryFadeSpeed * Gdx.graphics.getDeltaTime();

		if (this.getColor().a == this.maxOpacity) {
			this.isAtMaxOpacity = true;
		}
		if (this.isAtMaxOpacity && this.getColor().a > this.minOpacity) {
			this.doAnimation(-actualFadeSpeed);
		} else {
			this.isAtMaxOpacity = false;
		}

		if (!this.isAtMaxOpacity && this.getColor().a < this.maxOpacity) {
			this.doAnimation(this.secondaryFadeSpeed != 0 ? +actualSecondarySpeed : +actualFadeSpeed);
		}
	}

	public boolean isAtMaxOpacity() {
		return isAtMaxOpacity;
	}
}
