package com.colorswitch.game.screens;

import java.util.ArrayList;
import java.util.List;

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
	private Button settings, info, endless, shop, star, media, stats, wardrobe;
	private ModesButton modes;
	private PlayButton play;
	private Title title;
	private FadingButton prize, tasks;
	private static final List<Button> TOP_BUTTONS = new ArrayList<Button>();

	public MainMenu(SpriteBatch batch) {
		this.batch = batch;
		ColorSwitch.addButtonEventListener(this);

		float Yshift;
		this.settings = (Button) ColorSwitch.addButton(new Texture("settings.png"), this, BUTTON_SCALE).flipYCoord();
		TOP_BUTTONS.add(this.settings);

		this.info = (Button) ColorSwitch.addButton(new Texture("info.png"), this, BUTTON_SCALE).flipBothCoords();
		TOP_BUTTONS.add(this.info);

		this.tasks = (FadingButton) ColorSwitch.addButton((Button) new FadingButton(new Texture("tasks.png"), this, BUTTON_SCALE).flipYCoord());
		Yshift = this.tasks.getWidth() + ColorSwitch.EDGE_OFFSET / 2;
		this.tasks.shiftPosition(0, Yshift);
		TOP_BUTTONS.add(this.tasks);

		this.prize = (FadingButton) ColorSwitch.addButton((Button) new FadingButton(new Texture("prize.png"), this, BUTTON_SCALE).flipBothCoords());
		this.prize.shiftPosition(0, Yshift);
		TOP_BUTTONS.add(this.prize);

		this.endless = (Button) ColorSwitch.addButton(new Texture("endless.png"), this, BUTTON_SCALE).flipYCoord();
		this.endless.shiftPosition(0, Yshift * 2);
		this.endless.setOriginCenter();
		TOP_BUTTONS.add(this.endless);

		TOP_BUTTONS.forEach((button) -> {
			button.shiftPosition(ColorSwitch.EDGE_OFFSET, ColorSwitch.EDGE_OFFSET);
		});

		this.title = new Title(new Texture("title.png"), new Texture("circle.png"), this);
		this.addDrawCall(this.title);
		this.play = new PlayButton(new Texture("play_layer1.png"), new Texture("play_layer2.png"), new Texture("play_layer3.png"), new Texture("play_layer4.png"), this);
		this.addDrawCall(this.play);
		this.modes = new ModesButton(new Texture("modes.png"), new Texture("arrow.png"), this);
		this.addDrawCall(this.modes);

		this.media = (Button) ColorSwitch.addButton(new Texture("media.png"), this).applyScale(BUTTON_SCALE);
		Yshift =  this.modes.getButton().getY() - this.modes.getButton().getHeight() * ModesButton.MODES_SCALE_MAX.x / 1.3f;
		this.media.setPosition(ColorSwitch.findXAxisCenter(this.media.getWidth()), Yshift);
		this.media.setOriginCenter();

		this.star = (Button) ColorSwitch.addButton(new Texture("star.png"), this).applyScale(BUTTON_SCALE);
		float Xshift = this.media.getX() - ColorSwitch.EDGE_OFFSET - this.media.getWidth();
		this.star.setPosition(Xshift, Yshift);

		this.shop = (Button) ColorSwitch.addButton(new Texture("shop.png"), this).applyScale(BUTTON_SCALE);
		Xshift = this.star.getX() - ColorSwitch.EDGE_OFFSET - this.star.getWidth();
		this.shop.setPosition(Xshift, Yshift);

		this.stats = (Button) ColorSwitch.addButton(new Texture("stats.png"), this).applyScale(BUTTON_SCALE);
		Xshift = this.media.getX() + ColorSwitch.EDGE_OFFSET + this.media.getWidth();
		this.stats.setPosition(Xshift, Yshift);

		this.wardrobe = (Button) ColorSwitch.addButton(new Texture("wardrobe.png"), this).applyScale(BUTTON_SCALE);
		Xshift = this.stats.getX() + ColorSwitch.EDGE_OFFSET + this.star.getWidth();
		this.wardrobe.setPosition(Xshift, Yshift);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(ColorSwitch.BACKGROUND_COLOR, true);
		super.render(delta);
		this.endless.rotate(0.2f);
		this.media.rotate(-1.2f);
		this.prize.updateFade(); // if prize is available
	}

	@Override
	public void onButtonClicked(Button button, float xpos, float ypos) {

	}

	@Override
	public void onHover(Button button, float xpos, float ypos) {
		Color hoverColor = button.isHoverColorOverridden() ? button.getOverridenHoverColor() : Button.HOVER_COLOR;
		if(!button.equals(this.prize)) { // if prize isn't available
			button.setColor(hoverColor);
		}
	}

	@Override
	public void onHoverOut(Button button, float xpos, float ypos) {
		if(!button.equals(this.prize)) { // if prize isn't available
			button.setColor(Color.WHITE);
		}
	}
}
