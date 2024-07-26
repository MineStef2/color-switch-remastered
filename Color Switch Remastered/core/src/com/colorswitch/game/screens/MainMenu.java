package com.colorswitch.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.screens.gui.Button;
import com.colorswitch.game.screens.gui.ButtonEventListener;
import com.colorswitch.game.screens.gui.FadingButton;
import com.colorswitch.game.screens.gui.ModesButton;
import com.colorswitch.game.screens.gui.PlayButton;
import com.colorswitch.game.screens.gui.Title;

public class MainMenu extends Screen implements ButtonEventListener{
	private static final Vector2 BUTTON_SCALE = new Vector2(0.5f, 0.5f);
	private static final float BUTTON_SEPARATION = 10;
	@SuppressWarnings("unused") //will be removed after adding the button control
	private Button settings, info, endless, shop, star, media, stats, wardrobe;
	private FadingButton tasks, prize;
	private ModesButton modes;
	private Title title;
	private PlayButton play;

	public MainMenu(SpriteBatch batch) {
		super(batch);
		ColorSwitch.addButtonEventListener(this);

		this.settings = (Button) ColorSwitch.addButton(new Texture("settings.png"), this).applyScale(BUTTON_SCALE).flipYCoordinate().shiftPosition(ColorSwitch.EDGE_OFFSET, ColorSwitch.EDGE_OFFSET);
		this.info = (Button) ColorSwitch.addButton(new Texture("info.png"), this).applyScale(BUTTON_SCALE).flipXCoordinate().flipYCoordinate().shiftPosition(ColorSwitch.EDGE_OFFSET, ColorSwitch.EDGE_OFFSET);
		this.tasks = (FadingButton) ColorSwitch.addButton((Button) new FadingButton(new Texture("tasks.png"), this).applyScale(BUTTON_SCALE).flipYCoordinate().shiftPosition(ColorSwitch.EDGE_OFFSET, ColorSwitch.EDGE_OFFSET + BUTTON_SEPARATION + this.info.getHeight()));
		this.prize = (FadingButton) ColorSwitch.addButton((Button) new FadingButton(new Texture("prize.png"), this).applyScale(BUTTON_SCALE).flipXCoordinate().flipYCoordinate().shiftPosition(ColorSwitch.EDGE_OFFSET, ColorSwitch.EDGE_OFFSET + BUTTON_SEPARATION + this.tasks.getHeight()));
		this.endless = (Button) ColorSwitch.addButton(new Texture("endless.png"), this).applyScale(BUTTON_SCALE).flipYCoordinate().shiftPosition(ColorSwitch.EDGE_OFFSET, ColorSwitch.EDGE_OFFSET + BUTTON_SEPARATION * 2 + this.prize.getHeight() * 2).setOriginInCenter();

		this.title = new Title(new Texture("title.png"), new Texture("circle.png"), this);
		this.addDrawCall(this.title);
		this.play = new PlayButton(new Texture("play_layer1.png"), new Texture("play_layer2.png"), new Texture("play_layer3.png"), new Texture("play_layer4.png"), this);
		this.addDrawCall(this.play);
		this.modes = new ModesButton(new Texture("modes.png"), new Texture("arrow.png"), this);
		this.addDrawCall(this.modes);

		Texture mediaTexture = new Texture("media.png");
		this.media = (Button) ColorSwitch.addButton(mediaTexture, this).applyScale(BUTTON_SCALE).shiftPosition(ColorSwitch.findXAxisCenter(mediaTexture.getWidth() * BUTTON_SCALE.x), this.modes.getButton().getY() - mediaTexture.getHeight() * BUTTON_SCALE.y / 2).setOriginInCenter();
		this.star = (Button) ColorSwitch.addButton(new Texture("star.png"), this).applyScale(BUTTON_SCALE).shiftPosition(this.media.getX() - this.media.getWidth() - BUTTON_SEPARATION * 2, this.media.getY());
		this.shop = (Button) ColorSwitch.addButton(new Texture("shop.png"), this).applyScale(BUTTON_SCALE).shiftPosition(this.star.getX() - this.star.getWidth() - BUTTON_SEPARATION * 2, this.star.getY());
		this.stats = (Button) ColorSwitch.addButton(new Texture("stats.png"), this).applyScale(BUTTON_SCALE).shiftPosition(this.media.getX() + this.media.getWidth() + BUTTON_SEPARATION * 2, this.media.getY());
		this.wardrobe = (Button) ColorSwitch.addButton(new Texture("wardrobe.png"), this).applyScale(BUTTON_SCALE).shiftPosition(this.stats.getX() + this.stats.getWidth() + BUTTON_SEPARATION * 2, this.stats.getY());
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(ColorSwitch.BACKGROUND_COLOR, true);
		super.render(delta);
		this.endless.rotate(0.2f);
		this.media.rotate(-1.2f);
		this.tasks.updateFade(); //if tasks are available
		this.prize.updateFade(); // if prize is available
	}

	@Override
	public void onButtonClicked(Button button, float xpos, float ypos) {

	}

	@Override
	public void onHover(Button button, float xpos, float ypos) {
		Color hoverColor = button.isHoverColorOverridden() ? button.getOverridenHoverColor() : Button.HOVER_COLOR;
		if(!button.equals(this.prize) && !(button.equals(this.tasks))) { // if prize and tasks aren't available
			button.setColor(hoverColor);
		}
	}

	@Override
	public void onHoverOut(Button button, float xpos, float ypos) {
		if(!button.equals(this.prize) && !(button.equals(this.tasks))) { // if prize and tasks aren't available
			button.setColor(Color.WHITE);
		}
	}
}
