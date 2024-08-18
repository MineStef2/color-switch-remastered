package com.colorswitch.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.colorswitch.game.gui.Button;
import com.colorswitch.game.gui.Button.ButtonEventType;
import com.colorswitch.game.screens.Screen;
import com.colorswitch.game.screens.ScreenBinding;
import com.colorswitch.game.screens.ScreenManager;

public class ColorSwitch extends Game {
	public static final Color BACKGROUND_COLOR = new Color(0.16078f, 0.16078f, 0.16078f, 1);
	public static final Vector2 ANDROID_SCALE = new Vector2(1, 1);
	private static final List<Button> REGISTERED_BUTTONS = new ArrayList<Button>();
	private static ColorSwitch instance;
	private SpriteBatch batch;
	private TextureManager textureManager;
	private int currentMouseX = Integer.MIN_VALUE, currentMouseY = Integer.MIN_VALUE;
	private ScreenManager screenManager;
	private final GameConfig config;

	public ColorSwitch(GameConfig config) {
		instance = this;
		this.config = config;
	}

	@Override
	public void create() {
		if(this.config.androidInstance && this.config.width == 0 && this.config.height == 0) {
			TextureRegion screen = ScreenUtils.getFrameBufferTexture();
			this.config.width = screen.getRegionWidth();
			this.config.height = screen.getRegionHeight();
			System.out.println(this.config.width +", "+ this.config.height);
		}
		this.batch = new SpriteBatch();
		this.textureManager = new TextureManager(Gdx.files.getFileHandle("textures.json", FileType.Internal), ".png");
		this.screenManager = new ScreenManager(this.textureManager, this.batch);
		this.screenManager.createScreens();

		REGISTERED_BUTTONS.forEach((button) -> {
			ScreenBinding binding = button.getScreenBinding();
			if(binding != null) {
				button.bindScreen(binding.bind());
			}
		});
		this.setScreen(this.screenManager.getMainMenu());
	}

	/** Developer note: Every animation speed value (rotation, fading, scaling, etc.) is affected by the deltaTime, hence the values will be higher than what they might seem.**/
	@Override
	public void render() {
		super.render();

		int currentMouseX = Gdx.input.getX();
		int currentMouseY = Gdx.input.getY();
		if(this.currentMouseX == Integer.MIN_VALUE && this.currentMouseY == Integer.MIN_VALUE) {
			this.currentMouseX = currentMouseX;
			this.currentMouseY = currentMouseY;
		}
		if(this.currentMouseX != currentMouseX || this.currentMouseY != currentMouseY) {
			this.currentMouseX = currentMouseX;
			this.currentMouseY = currentMouseY;
			REGISTERED_BUTTONS.forEach((button) -> {
				int convertedCoord_mouseY = this.config.height - this.currentMouseY;
				if(button.checkEventAt(currentMouseX, convertedCoord_mouseY, ButtonEventType.HOVER)) {
					this.onHover(button, currentMouseX, convertedCoord_mouseY);
				}
				if(button.checkEventAt(currentMouseX, convertedCoord_mouseY, ButtonEventType.HOVER_OUT)) {
					this.onHoverOut(button, currentMouseX, convertedCoord_mouseY);
				}
			});
		}
		if(Gdx.input.justTouched()) {
			REGISTERED_BUTTONS.forEach((button) -> {
				int clickX = currentMouseX;
				int convertedCoord_clickY = this.config.height - currentMouseY;
				if(button.checkEventAt(clickX, convertedCoord_clickY, ButtonEventType.CLICK) && button.getOwner().isActive()) {
					this.onButtonClicked(button, currentMouseX, convertedCoord_clickY);
				}
			});
		}
		this.screenManager.updateAnimations();
	}

	public void onButtonClicked(Button button, float xpos, float ypos) {
		this.screenManager.switchScreen(button.getBoundScreen());
	}

	public void onHover(Button button, float xpos, float ypos) {
		Color hoverColor = button.isHoverColorOverridden() ? button.getOverridenHoverColor() : Button.HOVER_COLOR;
		if(!button.equals(this.screenManager.getMainMenu().prize) && !(button.equals(this.screenManager.getMainMenu().tasks)) && this.screenManager.isScreenAnimationInvalidated()) { // will be replaced with if prize and tasks aren't available
			button.setColor(hoverColor);
		}
	}

	public void onHoverOut(Button button, float xpos, float ypos) {
		if(!button.equals(this.screenManager.getMainMenu().prize) && !button.equals(this.screenManager.getMainMenu().tasks) && this.screenManager.isScreenAnimationInvalidated()) { // will be replaced with if prize and tasks aren't available
			button.setColor(Color.WHITE);
		}
	}

	public static Button addButton(Texture texture, Vector2 position, Vector2 scale, Screen owner) {
		Button button = new Button(texture, position, scale, owner);
		REGISTERED_BUTTONS.add(button);
		return button;
	}

	public static Button addButton(Texture texture, Vector2 position, Screen owner) {
		return addButton(texture, position, new Vector2(1f, 1f), owner);
	}

	public static Button addButton(Texture texture, Screen owner) {
		return addButton(texture, new Vector2(0, 0), new Vector2(1f, 1f), owner);
	}

	public static Button addButton(Button button) {
		REGISTERED_BUTTONS.add(button);
		return button;
	}

	public float findWindowCenterX(float componentWidth) {
		return (this.config.width - componentWidth) / 2;
	}

	public float findWindowCenterY(float componentHeight) {
		return (this.config.height - componentHeight) / 2;
	}

	@Override
	public void dispose() {
		super.dispose();
		this.screenManager.dispose();
	}

	public static List<Button> getRegisteredButtons() {
		return REGISTERED_BUTTONS;
	}

	public static ColorSwitch getInstance() {
		return instance;
	}

	public GameConfig getConfig() {
		return config;
	}
}
