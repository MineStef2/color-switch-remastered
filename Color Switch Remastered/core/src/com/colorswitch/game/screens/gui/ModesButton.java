package com.colorswitch.game.screens.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.DrawCall;
import com.colorswitch.game.screens.Screen;

public class ModesButton implements DrawCall{
	private Button modes;
	private GUIComponent[] arrows = new GUIComponent[2];
	private static final Vector2 MODES_SCALE_MAX = new Vector2(0.4f, 0.4f);
	private static final Vector2 MODES_SCALE_MIN = new Vector2(0.33f, 0.33f);
	private static final Vector2 ARROW_SCALE = new Vector2(0.4f, 0.4f);
	private static final float SCALE_AMOUNT = 0.0007f;
	private boolean isModesAtMaxScale;

	public ModesButton(Texture buttonTexture, Texture arrowTexture, Screen owner) {
		this.modes = (Button) ColorSwitch.addButton(new Texture("modes.png"), owner).overrideHoverColor(new Color(1, 1, 1, 0.8f));
		this.modes.setScale(MODES_SCALE_MAX.x, MODES_SCALE_MAX.y);
		this.isModesAtMaxScale = true;
		this.modes.setBounds((ColorSwitch.WINDOW_WIDTH - this.modes.getWidth()) / 2, (ColorSwitch.WINDOW_WIDTH - this.modes.getHeight()) / 2.5f, this.modes.getWidth(), this.modes.getHeight());
		this.arrows[1] = new GUIComponent(arrowTexture, owner).flipXAxis();
		this.arrows[0] = new GUIComponent(arrowTexture, owner);
		this.arrows[1].setScale(ARROW_SCALE.x, ARROW_SCALE.y);
		this.arrows[0].setScale(ARROW_SCALE.x, ARROW_SCALE.y);
		this.arrows[1].setBounds(0, this.modes.getY() + 20, this.arrows[1].getWidth(), this.arrows[1].getHeight());
		this.arrows[0].setBounds(this.modes.getWidth() * MODES_SCALE_MAX.x + this.arrows[0].getWidth() * ARROW_SCALE.x, this.arrows[1].getY(), this.arrows[0].getWidth(), this.arrows[0].getHeight());

	}

	@Override
	public void draw(SpriteBatch batch) {
		if(this.modes.getScaleX() == MODES_SCALE_MAX.x && this.modes.getScaleY() == MODES_SCALE_MAX.y) {
			this.isModesAtMaxScale = true;
		}
		if(this.isModesAtMaxScale && this.modes.getScaleX() > MODES_SCALE_MIN.x && this.modes.getScaleY() > MODES_SCALE_MIN.y) {
			this.modes.setScale(this.modes.getScaleX() - SCALE_AMOUNT, this.modes.getScaleY() - SCALE_AMOUNT);
			this.arrows[0].setScale(this.arrows[0].getScaleX() + SCALE_AMOUNT * 5, this.arrows[0].getScaleY() + SCALE_AMOUNT * 5);
			this.arrows[1].setScale(this.arrows[0].getScaleX(), this.arrows[0].getScaleY());
		}else {
			this.isModesAtMaxScale = false;
		}

		if(this.modes.getScaleX() == MODES_SCALE_MIN.x && this.modes.getScaleY() == MODES_SCALE_MIN.y) {
			this.isModesAtMaxScale = false;
		}
		if(!this.isModesAtMaxScale && this.modes.getScaleX() < MODES_SCALE_MAX.x && this.modes.getScaleY() < MODES_SCALE_MAX.y) {
			this.modes.setScale(this.modes.getScaleX() + SCALE_AMOUNT, this.modes.getScaleY() + SCALE_AMOUNT);
			this.arrows[0].setScale(this.arrows[0].getScaleX() - SCALE_AMOUNT * 5, this.arrows[0].getScaleY() - SCALE_AMOUNT * 5);
			this.arrows[1].setScale(this.arrows[0].getScaleX(), this.arrows[0].getScaleY());
		}
	}

	public Button getButton() {
		return this.modes;
	}
}
