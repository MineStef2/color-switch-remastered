package com.colorswitch.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.gui.button.Button;
import com.colorswitch.game.screens.Screen;

public class ModesButton extends CompositeGUIComponent {
	public static final Vector2 DESKTOP_SCALE_MAX = new Vector2(0.4f, 0.4f);
	public static final Vector2 DESKTOP_SCALE_MIN = new Vector2(0.33f, 0.33f);
	public static final Vector2 DESKTOP_ARROW_SCALE = new Vector2(0.4f, 0.4f);
	private static final boolean androidInstance = Gdx.app.getType() == ApplicationType.Android;
	public static final Vector2 PLATFORM_SCALE_MAX = ColorSwitch.getPlatformScale(DESKTOP_SCALE_MAX);
	public static final Vector2 PLATFORM_SCALE_MIN = androidInstance ? new Vector2(0.7f, 0.7f) : DESKTOP_SCALE_MIN;
	public static final Vector2 PLATFORM_ARROW_SCALE = androidInstance ? new Vector2(1f, 1f) : DESKTOP_ARROW_SCALE;
	private static final float SCALE_SPEED = androidInstance ? 0.2f : 0.05f;
	private Button modes;
	private GUIComponent[] arrows = new GUIComponent[2];
	private boolean isModesAtMaxScale;

	public ModesButton(Texture buttonTexture, Texture arrowTexture, Screen owner) {
		super(owner);
		this.modes = (Button) ColorSwitch.addButton(buttonTexture, owner).overrideHoverColor(new Color(1, 1, 1, 0.8f));
		this.modes.setScale(PLATFORM_SCALE_MAX.x, PLATFORM_SCALE_MAX.y);
		this.isModesAtMaxScale = true;
		this.modes.setBounds((ColorSwitch.getInstance().getConfig().window.width - this.modes.getWidth()) / 2,
				ColorSwitch.getInstance().getConfig().window.height / 2
						- this.modes.getHeight() * PLATFORM_SCALE_MAX.x * (androidInstance ? 3.5f : 4),
				this.modes.getWidth(), this.modes.getHeight());
		this.arrows[1] = new GUIComponent(arrowTexture, owner).flipXAxis();
		this.arrows[0] = new GUIComponent(arrowTexture, owner);
		this.arrows[1].setScale(PLATFORM_ARROW_SCALE.x, PLATFORM_ARROW_SCALE.y);
		this.arrows[0].setScale(PLATFORM_ARROW_SCALE.x, PLATFORM_ARROW_SCALE.y);
		this.arrows[1].setBounds(androidInstance ? this.modes.getX() - this.arrows[1].getHeight() : 0,
				this.modes.getY() + 20, this.arrows[1].getWidth(), this.arrows[1].getHeight());
		this.arrows[0].setBounds(this.modes.getWidth() * PLATFORM_SCALE_MAX.x
				+ this.arrows[0].getWidth() * PLATFORM_ARROW_SCALE.x
				+ (androidInstance ? 23 : 0),
				this.arrows[1].getY(), this.arrows[0].getWidth(), this.arrows[0].getHeight());
	}

	@Override
	public void draw(float deltaTime) {
		float actualScaleSpeed = SCALE_SPEED * deltaTime;
		if ((this.modes.getScaleX() >= PLATFORM_SCALE_MAX.x) && (this.modes.getScaleY() >= PLATFORM_SCALE_MAX.y)) {
			this.isModesAtMaxScale = true;
		}
		if (this.isModesAtMaxScale && (this.modes.getScaleX() >= PLATFORM_SCALE_MIN.x)
				&& (this.modes.getScaleY() >= PLATFORM_SCALE_MIN.y)) {
			this.modes.setScale(this.modes.getScaleX() - actualScaleSpeed, this.modes.getScaleY() - actualScaleSpeed);
			this.arrows[0].setScale(this.arrows[0].getScaleX() + actualScaleSpeed * (androidInstance ? 2.5f : 4),
					this.arrows[0].getScaleY() + actualScaleSpeed * (androidInstance ? 2.5f : 4));
			this.arrows[1].setScale(this.arrows[0].getScaleX(), this.arrows[0].getScaleY());
		} else {
			this.isModesAtMaxScale = false;
		}

		if (!this.isModesAtMaxScale && (this.modes.getScaleX() <= PLATFORM_SCALE_MAX.x)
				&& (this.modes.getScaleY() <= PLATFORM_SCALE_MAX.y)) {
			this.modes.setScale(this.modes.getScaleX() + actualScaleSpeed, this.modes.getScaleY() + actualScaleSpeed);
			this.arrows[0].setScale(this.arrows[0].getScaleX() - actualScaleSpeed * (androidInstance ? 2.5f : 4),
					this.arrows[0].getScaleY() - actualScaleSpeed * (androidInstance ? 2.5f : 4));
			this.arrows[1].setScale(this.arrows[0].getScaleX(), this.arrows[0].getScaleY());
		}
	}

	public Button getButton() {
		return this.modes;
	}
}
