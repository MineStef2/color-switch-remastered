package com.colorswitch.game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ScreenBinding;
import com.colorswitch.game.screens.Screen;

public class Button extends GUIComponent{
	public static final Vector2 BUTTON_SCALE = new Vector2(0.5f, 0.5f);
	public static final Color HOVER_COLOR = new Color(0.78431f, 0.78431f, 0.78431f, 1);
	private boolean hovering;
	private boolean overrideHoverColor;
	private Color overridenHoverColor;
	private Screen boundScreen;
	private ScreenBinding screenBinding;

	public Button(Texture texture, Vector2 position, Vector2 scale, Screen owner) {
		super(texture, owner, scale);
	}

	public Button overrideHoverColor(Color newColor) {
		this.overrideHoverColor = true;
		this.overridenHoverColor = newColor;
		return this;
	}

	public Button bindScreen(Screen screen) {
		this.boundScreen = screen;
		return this;
	}

	public Button applyScreenBinding(ScreenBinding binding) {
		this.screenBinding = binding;
		return this;
	}

	public Button setAsBackButton() {
		this.owner.setBackButton(this);
		return this;
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

	public Color getOverridenHoverColor() {
		return overridenHoverColor;
	}

	public Screen getBoundScreen() {
		return boundScreen;
	}

	public ScreenBinding getScreenBinding() {
		return screenBinding;
	}

	public static enum ButtonEventType{
		CLICK, HOVER, HOVER_OUT
	}
}
