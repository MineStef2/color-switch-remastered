package com.colorswitch.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.TextureManager;
import com.colorswitch.game.gui.Button;
import com.colorswitch.game.gui.GUIComponent;

public class Settings extends Screen {

	public Settings(SpriteBatch batch, TextureManager textureManager, ScreenManager screenManager) {
		super(batch, textureManager, screenManager);

		ColorSwitch.addButton(this.textureManager.getTexture("back"), this)
				.applyScreenBinding(this.screenManager::getMainMenu)
				.applyScale(Button.PLATFORM_SCALE)
				.flipYCoordinate()
				.shiftPosition(EDGE_OFFSET, EDGE_OFFSET + (androidInstance ? Screen.STATUS_BAR_OFFSET : 0));
		new GUIComponent(this.textureManager.getTexture("settingsTitle"), this)
				.applyScale(Button.PLATFORM_SCALE)
				.flipYCoordinate()
				.flipXCoordinate()
				.shiftPosition(EDGE_OFFSET * 2.5f, EDGE_OFFSET + 5 + (androidInstance ? Screen.STATUS_BAR_OFFSET : 0));
	}
}
