package com.colorswitch.game.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.TextureManager;
import com.colorswitch.game.gui.Button;
import com.colorswitch.game.gui.FadingButton;
import com.colorswitch.game.gui.ModesButton;
import com.colorswitch.game.gui.PlayButton;
import com.colorswitch.game.gui.Title;

public class MainMenu extends Screen{
	private static final float BUTTON_SEPARATION = androidInstance ? 20 : 10;
	private static final float ENDLESS_ROTATION_SPEED = 28f;
	private static final float MEDIA_ROTATION_SPEED = -173f;
	private Button endless, media;
	public FadingButton tasks, prize;

	public MainMenu(SpriteBatch batch, TextureManager textureManager, ScreenManager screenManager) {
		super(batch, textureManager, screenManager);

		float buttonHeight = ColorSwitch.addButton(this.textureManager.getTexture("settings"), this)
				.applyScreenBinding(this.screenManager::getSettings)
				.applyScale(Button.PLATFORM_SCALE)
				.flipYCoordinate()
				.shiftPosition(EDGE_OFFSET, EDGE_OFFSET + (androidInstance ? Screen.STATUS_BAR_OFFSET : 0)).getHeight();
		ColorSwitch.addButton(this.textureManager.getTexture("info"), this)
				.applyScale(Button.PLATFORM_SCALE)
				.flipXCoordinate()
				.flipYCoordinate()
				.shiftPosition(EDGE_OFFSET, EDGE_OFFSET + (androidInstance ? Screen.STATUS_BAR_OFFSET : 0));
		this.tasks = (FadingButton) ColorSwitch.addButton((Button) new FadingButton(this.textureManager.getTexture("tasks"), this, 0.15f, 1f, 1.4f, 2.1f)
				.applyScale(Button.PLATFORM_SCALE)
				.flipYCoordinate()
				.shiftPosition(EDGE_OFFSET, EDGE_OFFSET + BUTTON_SEPARATION + buttonHeight + (androidInstance ? Screen.STATUS_BAR_OFFSET : 0))
				.withDrawCall((deltaTime) -> {
					this.tasks.updateFade();
				}));
		this.prize = (FadingButton) ColorSwitch.addButton((Button) new FadingButton(this.textureManager.getTexture("prize"), this, 0.15f, 1f, 1.4f, 2.1f)
				.applyScale(Button.PLATFORM_SCALE)
				.flipXCoordinate()
				.flipYCoordinate()
				.shiftPosition(EDGE_OFFSET, EDGE_OFFSET + BUTTON_SEPARATION + buttonHeight + (androidInstance ? Screen.STATUS_BAR_OFFSET : 0))
				.withDrawCall((deltaTime) -> {
					this.prize.updateFade();
				}));
		this.endless = (Button) ColorSwitch.addButton(this.textureManager.getTexture("endless"), this)
				.applyScale(Button.PLATFORM_SCALE)
				.flipYCoordinate()
				.shiftPosition(EDGE_OFFSET, EDGE_OFFSET + BUTTON_SEPARATION * 2 + buttonHeight * 2 + (androidInstance ? Screen.STATUS_BAR_OFFSET : 0))
				.withDrawCall((deltaTime) -> this.endless.rotate(ENDLESS_ROTATION_SPEED * deltaTime))
				.setOriginInCenter();

		new Title(this.textureManager.getTexture("title"), this.textureManager.getTexture("circle"), this);
		new PlayButton(this.textureManager.getTexture("play1"), this.textureManager.getTexture("play2"), this.textureManager.getTexture("play3"), this.textureManager.getTexture("play4"), this);
		ModesButton modes = new ModesButton(this.textureManager.getTexture("modes"), this.textureManager.getTexture("arrow"), this);
		this.addDrawCall(modes);

		Texture mediaTexture = this.textureManager.getTexture("media");
		float buttonY = modes.getButton().getY() - mediaTexture.getHeight() * Button.PLATFORM_SCALE.y + (androidInstance ? -Screen.EDGE_OFFSET : +Screen.EDGE_OFFSET);
		float centerX = ColorSwitch.getInstance().findWindowCenterX(mediaTexture.getWidth() * Button.PLATFORM_SCALE.x);
		float buttonX = centerX;
		this.media = (Button) ColorSwitch.addButton(mediaTexture, this)
				.applyScale(Button.PLATFORM_SCALE)
				.shiftPosition(buttonX, buttonY)
				.setOriginInCenter()
				.withDrawCall((deltaTime) -> this.media.rotate(MEDIA_ROTATION_SPEED * deltaTime));
		float buttonWidth = this.media.getWidth();
		float offset = buttonWidth + BUTTON_SEPARATION * 2;
		ColorSwitch.addButton(this.textureManager.getTexture("star"), this).applyScale(Button.PLATFORM_SCALE).shiftPosition(buttonX -= offset, buttonY);
		ColorSwitch.addButton(this.textureManager.getTexture("shop"), this).applyScale(Button.PLATFORM_SCALE).shiftPosition(buttonX -= offset, buttonY);
		ColorSwitch.addButton(this.textureManager.getTexture("stats"), this).applyScale(Button.PLATFORM_SCALE).shiftPosition(centerX += offset, buttonY);
		ColorSwitch.addButton(this.textureManager.getTexture("wardrobe"), this).applyScale(Button.PLATFORM_SCALE).shiftPosition(centerX += offset, buttonY);
	}
}
