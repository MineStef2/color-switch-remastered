package com.colorswitch.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.ScreenManager;
import com.colorswitch.game.TextureManager;
import com.colorswitch.game.gui.GUIComponent;
import com.colorswitch.game.gui.StringComponent;
import com.colorswitch.game.gui.button.Button;

public class Prizes extends Screen{
	private static final Color TOP_COLOR = Color.valueOf("#FF0070");

	public Prizes(SpriteBatch batch, TextureManager textureManager, ScreenManager screenManager) {
		super(batch, textureManager, screenManager);

		GUIComponent top = new GUIComponent(this.getTexture("top"), this)
				.applyScale(ColorSwitch.getPlatformScale())
				.flipYCoordinate();
		top.setColor(TOP_COLOR);

		Button back = (Button) ColorSwitch.addButton(this.getTexture("home"), this)
				.applyScreen("mainMenu")
				.applyScale(ColorSwitch.getPlatformScale())
				.flipYCoordinate()
				.shiftPosition(EDGE_OFFSET, EDGE_OFFSET
						+ (androidInstance ? Screen.STATUS_BAR_OFFSET : 0));

		StringComponent title = new StringComponent("PRIZES", this)
				.setSize(50) 										/* invalid for android */
				.setFont(StringComponent.MEDIUM_FONT_FILE);
		title.setPosition(new Vector2(back.getX() + back.getWidth() + EDGE_OFFSET / 2,
				back.getY() + (back.getHeight() + title.getHeight()) / 2));

		GUIComponent star = new GUIComponent(this.getTexture("star"), this)
				.applyScale(ColorSwitch.getPlatformScale());
		star.shiftPosition(title.getX() + title.getWidth(),
				title.getY() - (star.getHeight() + title.getHeight()) / 2);

		StringComponent starAmount = new StringComponent(this.game.getUser().getStars() + "", this)
				.setSize(50);									   /* invalid for android */
		starAmount.setColor(Color.YELLOW);
		starAmount.setPosition(new Vector2(star.getX() + star.getWidth(),
				star.getY() + (star.getHeight() + starAmount.getHeight()) / 2));
	}
}
