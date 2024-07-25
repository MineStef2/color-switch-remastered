package com.colorswitch.game.screens.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.screens.Screen;

public class Button extends GUIObject{
	private Vector2 originalScale;
	private boolean hovering;
	public static final Color HOVER_COLOR = new Color(0.78431f, 0.78431f, 0.78431f, 1);
	private boolean overrideHoverColor;
	private Color overridenHoverColor;

	public Button(Texture texture, Screen owner, Vector2 scale) {
		super(texture, owner, scale);
	}

	public Button(Texture texture, Screen owner) {
		this(texture, owner, new Vector2(1f, 1f));
	}

	public Button overrideHoverColor(Color newColor) {
		this.overridenHoverColor = newColor;
		this.overrideHoverColor = true;
		return this;
	}

	public void doFadeAnimation(float fadeAmount) {
		this.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a + fadeAmount);
	}

	public boolean isHovering() {
		return hovering;
	}

	public boolean isHoverColorOverridden() {
		return overrideHoverColor;
	}

	public boolean checkEventAt(float x, float y, ButtonEventType type) {
		this.hovering = this.getBoundingRectangle().contains(x, y);
		if(type == ButtonEventType.CLICK || type == ButtonEventType.HOVER) {
			return hovering;
		}else if(type == ButtonEventType.HOVER_OUT) {
			return !hovering;
		}else {
			System.err.println("Button.checkEventAt() returned false!");
			return false;
		}
	}

	public Vector2 getOriginalScale() {
		return originalScale;
	}

	public Color getOverridenHoverColor() {
		return overridenHoverColor;
	}

	public static enum ButtonEventType{
		CLICK, HOVER, HOVER_OUT
	}
}
