package com.colorswitch.game.screens;

import static com.colorswitch.game.gui.Button.BUTTON_SCALE;

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
	private static final float BUTTON_SEPARATION = 10;
	private static final float ENDLESS_ROTATION_SPEED = 28f;
	private static final float MEDIA_ROTATION_SPEED = -173f;
	private Button endless, media;
	public FadingButton tasks, prize;

	public MainMenu(SpriteBatch batch, TextureManager textureManager) {
		super(batch, textureManager);

		float buttonHeight = ColorSwitch.addButton(this.textureManager.getTexture("settings"), this).applyScreenBinding(ColorSwitch.getInstance()::getSettingsScreen).applyScale(BUTTON_SCALE).flipYCoordinate().shiftPosition(ColorSwitch.EDGE_OFFSET, ColorSwitch.EDGE_OFFSET).getHeight();
		ColorSwitch.addButton(this.textureManager.getTexture("info"), this).applyScale(BUTTON_SCALE).flipXCoordinate().flipYCoordinate().shiftPosition(ColorSwitch.EDGE_OFFSET, ColorSwitch.EDGE_OFFSET);
		this.tasks = (FadingButton) ColorSwitch.addButton((Button) new FadingButton(this.textureManager.getTexture("tasks"), this, 0.15f, 1f, 1.4f, 2.1f).applyScale(BUTTON_SCALE).flipYCoordinate().shiftPosition(ColorSwitch.EDGE_OFFSET, ColorSwitch.EDGE_OFFSET + BUTTON_SEPARATION + buttonHeight))
				.withDrawCall((deltaTime) -> {
					this.tasks.updateFade();
				});
		this.prize = (FadingButton) ColorSwitch.addButton((Button) new FadingButton(this.textureManager.getTexture("prize"), this, 0.15f, 1f, 1.4f, 2.1f).applyScale(BUTTON_SCALE).flipXCoordinate().flipYCoordinate().shiftPosition(ColorSwitch.EDGE_OFFSET, ColorSwitch.EDGE_OFFSET + BUTTON_SEPARATION + buttonHeight))
				.withDrawCall((deltaTime) -> {
					this.prize.updateFade();
				});
		this.endless = (Button) ColorSwitch.addButton(this.textureManager.getTexture("endless"), this).applyScale(BUTTON_SCALE).flipYCoordinate().shiftPosition(ColorSwitch.EDGE_OFFSET, ColorSwitch.EDGE_OFFSET + BUTTON_SEPARATION * 2 + buttonHeight * 2).withDrawCall((deltaTime) -> this.endless.rotate(ENDLESS_ROTATION_SPEED * deltaTime)).setOriginInCenter();

		new Title(this.textureManager.getTexture("title"), this.textureManager.getTexture("circle"), this);
		new PlayButton(this.textureManager.getTexture("play1"), this.textureManager.getTexture("play2"), this.textureManager.getTexture("play3"), this.textureManager.getTexture("play4"), this);
		ModesButton modes = new ModesButton(this.textureManager.getTexture("modes"), this.textureManager.getTexture("arrow"), this);
		this.addDrawCall(modes);

		Texture mediaTexture = this.textureManager.getTexture("media");
		float buttonY = modes.getButton().getY() - mediaTexture.getHeight() * BUTTON_SCALE.y / 2;
		float centerX = ColorSwitch.findWindowCenterX(mediaTexture.getWidth() * BUTTON_SCALE.x);
		float buttonX = centerX;
		this.media = (Button) ColorSwitch.addButton(mediaTexture, this).applyScale(BUTTON_SCALE).shiftPosition(buttonX, buttonY).setOriginInCenter().withDrawCall((deltaTime) -> this.media.rotate(MEDIA_ROTATION_SPEED * deltaTime));
		float buttonWidth = this.media.getWidth();
		float offset = buttonWidth + BUTTON_SEPARATION * 2;
		ColorSwitch.addButton(this.textureManager.getTexture("star"), this).applyScale(BUTTON_SCALE).shiftPosition(buttonX -= offset, buttonY);
		ColorSwitch.addButton(this.textureManager.getTexture("shop"), this).applyScale(BUTTON_SCALE).shiftPosition(buttonX -= offset, buttonY);
		ColorSwitch.addButton(this.textureManager.getTexture("stats"), this).applyScale(BUTTON_SCALE).shiftPosition(centerX += offset, buttonY);
		ColorSwitch.addButton(this.textureManager.getTexture("wardrobe"), this).applyScale(BUTTON_SCALE).shiftPosition(centerX += offset, buttonY);

		//TODO: change animation values (rotation, fading, scaling) so that it works with deltaTime
	}
}
