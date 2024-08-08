package com.colorswitch.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.gui.Button;
import com.colorswitch.game.gui.Button.ButtonEventType;
import com.colorswitch.game.screens.MainMenu;
import com.colorswitch.game.screens.Screen;
import com.colorswitch.game.screens.Settings;

public class ColorSwitch extends Game {
	public static final Color BACKGROUND_COLOR = new Color(0.16078f, 0.16078f, 0.16078f, 1);
	public static final int WINDOW_WIDTH = 480, WINDOW_HEIGHT = 840;
	public static final float EDGE_OFFSET = 20;
	private static final float SCREEN_FADE_SPEED = 4.7f;
	private static final List<Button> REGISTERED_BUTTONS = new ArrayList<Button>();
	private static ColorSwitch instance;
	private SpriteBatch batch;
	private TextureManager textureManager;
	private MainMenu mainMenu;
	private Settings settings;
	private Screen previousScreen, currentScreen, nextScreen;
	private Sprite screenAnimationHelper;
	private int currentMouseX = Integer.MIN_VALUE, currentMouseY = Integer.MIN_VALUE;
	private boolean screenAnimation;

	public ColorSwitch() {
		instance = this;
		this.previousScreen = this.currentScreen = this.nextScreen = null;
	}

	@Override
	public void create() {
		this.batch = new SpriteBatch();
		this.textureManager = new TextureManager(Gdx.files.getFileHandle("data/textures.json", FileType.Local), ".png");
		this.screenAnimationHelper = new Sprite(this.textureManager.getTexture("background"));
		this.screenAnimationHelper.setColor(BACKGROUND_COLOR.sub(0, 0, 0, 1));
		this.currentScreen = this.previousScreen = this.mainMenu = new MainMenu(this.batch, this.textureManager);
		this.settings = new Settings(this.batch, this.textureManager);


		REGISTERED_BUTTONS.forEach((button) -> {
			ScreenBinding binding = button.getScreenBinding();
			if(binding != null) {
				button.bindScreen(binding.bind());
			}
		});
		this.setScreen(this.mainMenu);
	}

	/** Developer note: Every animation speed value (rotation, fading, scaling, etc.) is affected by the deltaTime, hence the values will be higher than what they might seem.**/
	@Override
	public void render() {
		super.render();
		this.batch.begin();
		this.screenAnimationHelper.draw(this.batch);
		this.batch.end();

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
				int convertedCoord_mouseY = WINDOW_HEIGHT - this.currentMouseY;
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
				int convertedCoord_clickY = WINDOW_HEIGHT - currentMouseY;
				if(button.checkEventAt(clickX, convertedCoord_clickY, ButtonEventType.CLICK) && button.getOwner().isActive()) {
					this.onButtonClicked(button, currentMouseX, convertedCoord_clickY);
				}
			});
		}

		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE) && !this.screenAnimation) {
			if(this.getScreen() == this.mainMenu) {
				Gdx.app.exit();
			}else {
				this.switchScreen(this.previousScreen);
			}
		}

		if(this.screenAnimation) {
			if(this.currentScreen != this.nextScreen) {
				this.screenAnimationHelper.setColor(this.screenAnimationHelper.getColor().add(0, 0, 0, SCREEN_FADE_SPEED * Gdx.graphics.getDeltaTime()));
				if(this.screenAnimationHelper.getColor().a == 1.0f) {
					this.setScreen(this.nextScreen);
					this.currentScreen = this.nextScreen;
				}
			}else {
				this.screenAnimationHelper.setColor(this.screenAnimationHelper.getColor().sub(0, 0, 0, SCREEN_FADE_SPEED * Gdx.graphics.getDeltaTime()));
				if(this.screenAnimationHelper.getColor().a == 0.0f) {
					this.screenAnimation = false;
				}
			}
		}
	}

	public void switchScreen(Screen newScreen) {
		this.previousScreen = (Screen) this.getScreen();
		this.nextScreen = newScreen;
		this.screenAnimation = true;
	}

	public void onButtonClicked(Button button, float xpos, float ypos) {
		this.switchScreen(button.getBoundScreen());
	}

	public void onHover(Button button, float xpos, float ypos) {
		Color hoverColor = button.isHoverColorOverridden() ? button.getOverridenHoverColor() : Button.HOVER_COLOR;
		if(!button.equals(this.mainMenu.prize) && !(button.equals(this.mainMenu.tasks))) { // will be replaced with if prize and tasks aren't available
			button.setColor(hoverColor);
		}
	}

	public void onHoverOut(Button button, float xpos, float ypos) {
		if(!button.equals(this.mainMenu.prize) && !button.equals(this.mainMenu.tasks)) { // will be replaced with if prize and tasks aren't available
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

	public static float findWindowCenterX(float componentWidth) {
		return (WINDOW_WIDTH - componentWidth) / 2;
	}

	public static float findWindowCenterY(float componentHeight) {
		return (WINDOW_HEIGHT - componentHeight) / 2;
	}

	@Override
	public void dispose() {
		super.dispose();
		this.mainMenu.dispose();
		this.settings.dispose();
	}

	public static List<Button> getRegisteredButtons() {
		return REGISTERED_BUTTONS;
	}

	public MainMenu getMainMenuScreen() {
		return mainMenu;
	}

	public Settings getSettingsScreen() {
		return settings;
	}

	public Screen getPreviousScreen() {
		return previousScreen;
	}

	public Sprite getFrameBufferAsSprite() {
		return screenAnimationHelper;
	}

	public static ColorSwitch getInstance() {
		return instance;
	}
}
