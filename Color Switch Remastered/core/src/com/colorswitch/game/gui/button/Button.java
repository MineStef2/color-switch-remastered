package com.colorswitch.game.gui.button;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Null;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.ScreenManager;
import com.colorswitch.game.gui.GUIComponent;
import com.colorswitch.game.screens.Screen;
import com.colorswitch.game.screens.ScreenBinding;
import com.colorswitch.game.sound.AudioOutput;

public class Button extends GUIComponent {
	public static final Vector2 DESKTOP_SCALE = new Vector2(0.5f, 0.5f);
	public static final Vector2 PLATFORM_SCALE = (Gdx.app.getType() == ApplicationType.Android)
			? ColorSwitch.ANDROID_SCALE
			: DESKTOP_SCALE;
	public static final Color HOVER_COLOR = new Color(0.78431f, 0.78431f, 0.78431f, 1);
	private boolean hovering;
	private boolean overrideHoverColor;
	private Color overridenHoverColor;
	private Screen boundScreen;
	private ScreenBinding screenBinding;
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
		return this;
	}

	public Button applyScreen(String screenName) {
		ScreenManager screenManager = this.owner.getScreenManager();
		this.screenBinding = () -> screenManager.getScreen(screenName);
		this.behavior.setClickBehavior((button, xpos, ypos) -> screenManager.switchScreen(this.boundScreen));
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

	public void reloadClickSound() {
		this.behavior.setClickSound(ColorSwitch.getInstance().getSoundManager().getAudioOutput("click"));
	}

	public boolean isHovering() {
		return hovering;
	}

	public boolean isHoverColorOverridden() {
		return overrideHoverColor;
	}

	public boolean checkEventAt(float x, float y, ButtonEventType type) {
		this.hovering = this.getBoundingRectangle().contains(x, y);
		if (type == ButtonEventType.CLICK || type == ButtonEventType.HOVER) {
			return hovering;
		} else if (type == ButtonEventType.HOVER_OUT) {
			return !hovering;
		} else {
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
