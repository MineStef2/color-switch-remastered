package com.colorswitch.game.gui.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Null;
import com.colorswitch.game.gui.GUIComponent;
import com.colorswitch.game.screens.Screen;
import com.colorswitch.game.screens.Screens;
import com.colorswitch.game.sound.AudioOutput;

public class Button extends GUIComponent {
	public static final Color HOVER_COLOR = new Color(0.78431f, 0.78431f, 0.78431f, 1);
	public boolean hovering;
	public boolean flag_nullClickBehavior = false;
	private boolean overrideHoverColor;
	private Color overridenHoverColor;
	private Screen boundScreen;
	private ButtonBehavior behavior;

	public Button(Texture texture, Vector2 position, Vector2 scale, Screen owner) {
		super(texture, owner, scale);
		this.behavior = new ButtonBehavior(this);
	}

	public Button overrideHoverColor(Color newColor) {
		this.overrideHoverColor = true;
		this.overridenHoverColor = newColor;
		return this;
	}

	public Button bindScreen(Screen screen) {
		this.boundScreen = screen;
		if (this.behavior.getClickBehavior() == null) {
			this.behavior.setClickBehavior(this.owner.getScreenManager()::defaultScreenSwitchBehavior);
		}
		return this;
	}

	public Button bindScreen(Screens screen) {
		this.bindScreen(this.owner.getScreenManager().getScreenInstance(screen));
		return this;
	}

	public Button applyClickBehavior(@Null ButtonClickBehavior clickBehaviour) {
		this.behavior.setClickBehavior(clickBehaviour);
		return this;
	}

	public Button changeHoverBehavior(@Null ButtonHoverBehavior hoverBehaviour) {
		this.behavior.setHoverBehavior(hoverBehaviour);
		return this;
	}

	public Button changeHoverOutBehavior(@Null ButtonHoverOutBehavior hoverOutBehavior) {
		this.behavior.setHoverOutBehavior(hoverOutBehavior);
		return this;
	}

	public Button changeHoverBehaviors(@Null ButtonHoverBehavior hoverBehavior, ButtonHoverOutBehavior hoverOutBehavior) {
		this.behavior.setHoverBehavior(hoverBehavior);
		this.behavior.setHoverOutBehavior(hoverOutBehavior);
		return this;
	}

	public Button changeClickSound(@Null AudioOutput clickSound) {
		this.behavior.setClickSound(clickSound);
		return this;
	}

	public boolean isHoverColorOverridden() {
		return overrideHoverColor;
	}

	public ButtonEventType checkEventAt(float x, float y) {
		this.hovering = this.getBoundingRectangle().contains(x, y);
		if (Gdx.input.justTouched() && this.hovering) {
			return ButtonEventType.CLICK;
		} else if (this.hovering && !Gdx.input.isTouched()) {
			return ButtonEventType.HOVER;
		} else if (!this.hovering && !Gdx.input.isTouched()) {
			return ButtonEventType.HOVER_OUT;
		}
		return null;
	}

	public Color getOverridenHoverColor() {
		return overridenHoverColor;
	}

	public Screen getBoundScreen() {
		return boundScreen;
	}

	public ButtonClickBehavior getClickBehavior() {
		return this.behavior.getClickBehavior();
	}

	public ButtonHoverBehavior getHoverBehavior() {
		return this.behavior.getHoverBehavior();
	}

	public ButtonHoverOutBehavior getHoverOutbehavior() {
		return this.behavior.getHoverOutBehavior();
	}

	public AudioOutput getClickSound() {
		return this.behavior.getClickSound();
	}

	public static enum ButtonEventType {
		CLICK, HOVER, HOVER_OUT
	}
}
