package com.colorswitch.game.screens;

import static com.colorswitch.game.gui.Button.BUTTON_SCALE;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.TextureManager;
import com.colorswitch.game.gui.GUIComponent;

public class Settings extends Screen {

	public Settings(SpriteBatch batch, TextureManager textureManager) {
		super(batch, textureManager);

		ColorSwitch.addButton(this.textureManager.getTexture("back"), this).setAsBackButton().applyScale(BUTTON_SCALE).flipYCoordinate();
		new GUIComponent(ColorSwitch.getInstance().getFrameBufferAsSprite().getTexture(), this).setColor(ColorSwitch.BACKGROUND_COLOR.sub(0, 0, 0, 1f));
	}
}
