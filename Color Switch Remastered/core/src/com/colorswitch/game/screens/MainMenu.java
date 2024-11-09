package com.colorswitch.game.screens;

import com.badlogic.gdx.graphics.Texture;
import com.colorswitch.game.SharedConstants;
import com.colorswitch.game.WindowUtils;
import com.colorswitch.game.gui.ModesButton;
import com.colorswitch.game.gui.PlayButton;
import com.colorswitch.game.gui.StringComponent;
import com.colorswitch.game.gui.Title;
import com.colorswitch.game.gui.button.Button;
import com.colorswitch.game.gui.button.FadingButton;

public class MainMenu extends Screen {
	private static final float BUTTON_SEPARATION = androidInstance ? 20 : 10;
	private static final float ENDLESS_ROTATION_SPEED = 28f;
	private static final float MEDIA_ROTATION_SPEED = -173f;
	private FadingButton challenges;
	private FadingButton prize;
	private Button endless;
	private Button media;

	@Override
	public void initializeUI() {
		float buttonHeight = this.newButton(this.getTexture("settings"))
				.bindScreen(Screens.SETTINGS)
				.applyScale(SharedConstants.commonScale)
				.flipYCoordinate()
				.shiftPosition(EDGE_OFFSET, EDGE_OFFSET + (androidInstance ? Screen.STATUS_BAR_OFFSET : 0)).getHeight();
		this.newButton(this.getTexture("info"))
				.bindScreen(Screens.INFO).applyScale(SharedConstants.commonScale)
				.flipXCoordinate().flipYCoordinate()
				.shiftPosition(EDGE_OFFSET, EDGE_OFFSET + (androidInstance ? Screen.STATUS_BAR_OFFSET : 0));
		this.challenges = (FadingButton) this.newButton((Button) new FadingButton(this.getTexture("challenges"), this, 0.15f, 1f, 1.4f, 2.1f)
				.bindScreen(Screens.CHALLENGES)
				.changeHoverBehaviors(null, null)                              /* hover and hoverOut behavior will be changed when the challenges feature is added */
				.applyScale(SharedConstants.commonScale).flipYCoordinate()
				.shiftPosition(EDGE_OFFSET,
						EDGE_OFFSET + BUTTON_SEPARATION + buttonHeight
							   + (androidInstance ? Screen.STATUS_BAR_OFFSET : 0))
				.withDrawCall((deltaTime) -> {
					this.challenges.updateFade();
				}));
		this.prize = (FadingButton) this.newButton((Button) new FadingButton(this.getTexture("prize"), this, 0.15f, 1f, 1.4f, 2.1f)
				.bindScreen(Screens.PRIZES)
				.changeHoverBehaviors(null, null) 											/* hover and hoverOut behavior will be changed when the prize feature is added */
				.applyScale(SharedConstants.commonScale)
				.flipXCoordinate()
				.flipYCoordinate()
				.shiftPosition(EDGE_OFFSET,
						EDGE_OFFSET + BUTTON_SEPARATION + buttonHeight
								+ (androidInstance ? Screen.STATUS_BAR_OFFSET : 0))
				.withDrawCall((deltaTime) -> {
					this.prize.updateFade();
				}));
		this.endless = (Button) this.newButton(this.getTexture("endless"))
				.applyScale(SharedConstants.commonScale).flipYCoordinate()
				.shiftPosition(EDGE_OFFSET,
						EDGE_OFFSET + BUTTON_SEPARATION * 2 + buttonHeight * 2
								+ (androidInstance ? Screen.STATUS_BAR_OFFSET : 0))
				.withDrawCall((deltaTime) -> this.endless.rotate(ENDLESS_ROTATION_SPEED * deltaTime))
				.setOriginInCenter();

		Title title = new Title(this.getTexture("title"), this.getTexture("circle"), this);
		new PlayButton(this.getTexture("play_layer1"), this.getTexture("play_layer2"),
				this.getTexture("play_layer3"), this.getTexture("play_layer4"), this);
		ModesButton modes = new ModesButton(this.getTexture("modes"),
				this.getTexture("arrow"), this);
		this.addDrawCall(modes);

		Texture mediaTexture = this.getTexture("media");
		float buttonY = modes.getButton().getY() - mediaTexture.getHeight() * SharedConstants.commonScale.y
				+ (androidInstance ? -Screen.EDGE_OFFSET : +Screen.EDGE_OFFSET);
		float centerX = WindowUtils.getCenterX(mediaTexture.getWidth() * SharedConstants.commonScale.x);
		float buttonX = centerX;
		this.media = (Button) this.newButton(mediaTexture).applyScale(SharedConstants.commonScale)
				.shiftPosition(buttonX, buttonY).setOriginInCenter()
				.withDrawCall((deltaTime) -> this.media.rotate(MEDIA_ROTATION_SPEED * deltaTime));
		float buttonWidth = this.media.getWidth();
		float offset = buttonWidth + BUTTON_SEPARATION * 2;
		this.newButton(this.getTexture("star_button"))
				.applyScale(SharedConstants.commonScale)
				.shiftPosition(buttonX -= offset, buttonY);
		this.newButton(this.getTexture("shop"))
				.applyScale(SharedConstants.commonScale)
				.shiftPosition(buttonX -= offset, buttonY);
		this.newButton(this.getTexture("stats"))
				.applyScale(SharedConstants.commonScale)
				.shiftPosition(centerX += offset, buttonY);
		this.newButton(this.getTexture("wardrobe"))
				.applyScale(SharedConstants.commonScale)
				.shiftPosition(centerX += offset, buttonY);

		StringComponent remastered = new StringComponent("Remastered", this).setSize(androidInstance ? 80 : 30);
		remastered.setPosition(WindowUtils.getCenterX(remastered.getLayout().width), title.getTitleText().getY());
	}
}
