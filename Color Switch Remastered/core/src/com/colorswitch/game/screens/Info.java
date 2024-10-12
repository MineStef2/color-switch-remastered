package com.colorswitch.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.ScreenManager;
import com.colorswitch.game.TextureManager;
import com.colorswitch.game.WindowUtils;
import com.colorswitch.game.gui.GUIComponent;
import com.colorswitch.game.gui.StringComponent;
import com.colorswitch.game.gui.button.Button;

public class Info extends Screen{
	private static final Vector2 DESKTOP_CREDITS_SCALE = new Vector2(0.5f, 0.5f);
	private static final Vector2 PLATFORM_SCALE = androidInstance ? ColorSwitch.ANDROID_SCALE : DESKTOP_CREDITS_SCALE;
	private final StringComponent remasterer;
	private final StringComponent developer;

	public Info(SpriteBatch batch, TextureManager textureManager, ScreenManager screenManager) {
		super(batch, textureManager, screenManager);

		Button back = this.addDefaultBackButton();
		GUIComponent credits = new GUIComponent(this.getTexture("credits"), this)
				.applyScale(PLATFORM_SCALE);
		credits.shiftPosition(WindowUtils.getCenterX(credits.getWidth()), back.getY() - credits.getHeight() - EDGE_OFFSET);

		int size = androidInstance ? 60 : 30;
		this.remasterer = new StringComponent("Remastered by Miskolczi Stefan", this).setSize(size);
		this.developer = new StringComponent("(MineStef2)", this).setSize(size);
		this.remasterer.setPosition(WindowUtils.getCenterX(this.remasterer.getWidth()), credits.getY() - this.remasterer.getHeight() * 3);
		this.developer.setPosition(WindowUtils.getCenterX(this.developer.getWidth()),
				this.remasterer.getY() - this.developer.getHeight() * 1.5f);
	}

	@Override
	public void dispose() {
		super.dispose();
		this.remasterer.dispose();
		this.developer.dispose();
	}
}
