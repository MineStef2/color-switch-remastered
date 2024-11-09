package com.colorswitch.game.screens;

import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.WindowUtils;
import com.colorswitch.game.gui.GUIComponent;
import com.colorswitch.game.gui.StringComponent;
import com.colorswitch.game.gui.button.Button;

public class Info extends Screen{
	private static final Vector2 DESKTOP_CREDITS_SCALE = new Vector2(0.5f, 0.5f);
	private static final Vector2 PLATFORM_SCALE = androidInstance ? ColorSwitch.ANDROID_SCALE : DESKTOP_CREDITS_SCALE;

	@Override
	public void initializeUI() {
		Button back = this.addDefaultBackButton();
		GUIComponent credits = new GUIComponent(this.getTexture("credits"), this)
				.applyScale(PLATFORM_SCALE);
		credits.shiftPosition(WindowUtils.getCenterX(credits.getWidth()), back.getY() - credits.getHeight() - EDGE_OFFSET);

		int size = androidInstance ? 60 : 30;
		StringComponent remasterer = new StringComponent("Remastered by Miskolczi Stefan", this).setSize(size);
		StringComponent developer = new StringComponent("(MineStef2)", this).setSize(size);
		remasterer.setPosition(WindowUtils.getCenterX(remasterer.getWidth()), credits.getY() - remasterer.getHeight() * 3);
		developer.setPosition(WindowUtils.getCenterX(developer.getWidth()),
				remasterer.getY() - developer.getHeight() * 1.5f);

	}
}