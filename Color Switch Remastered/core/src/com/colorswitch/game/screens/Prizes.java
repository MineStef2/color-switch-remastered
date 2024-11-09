package com.colorswitch.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.gui.GUIComponent;
import com.colorswitch.game.gui.StringComponent;

public class Prizes extends Screen{
	private static final Color TOP_COLOR = Color.valueOf("#FF0070");

	@Override
	public void initializeUI() {
		GUIComponent top = this.addDefaultTopPadding();
		top.setColor(TOP_COLOR);
		StringComponent title = this.addDefaultTitle("PRIZES", this.addDefaultHomeButton());
		title.setFont(StringComponent.MEDIUM_FONT_FILE);

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
