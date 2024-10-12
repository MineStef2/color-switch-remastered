package com.colorswitch.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Null;
import com.colorswitch.game.GameConfig.FileConfig.SettingsConfig;
import com.colorswitch.game.screens.Challenges;
import com.colorswitch.game.screens.Info;
import com.colorswitch.game.screens.MainMenu;
import com.colorswitch.game.screens.Prizes;
import com.colorswitch.game.screens.Screen;
import com.colorswitch.game.screens.ScreenAnimation;
import com.colorswitch.game.screens.Settings;

public final class ScreenManager {
	public static final float SCREEN_FADE_SPEED = 4.7f;
	private static final Logger LOGGER = new Logger(ScreenManager.class);
	public Screen previousScreen;
	public Screen currentScreen;
	public Screen nextScreen;
	public final Sprite screenAnimationHelper;
	private final ColorSwitch game;
	private final TextureManager textureManager;
	private final SpriteBatch batch;
	private MainMenu mainMenu;
	private ScreenAnimation screenAnimation;
	private Map<String, Screen> screens;

	public ScreenManager(TextureManager textureManager, SpriteBatch batch) {
		this.game = ColorSwitch.getInstance();
		this.textureManager = textureManager;
		this.batch = batch;
		this.screenAnimationHelper = new Sprite(this.getTexture("background_screen"));
		this.screenAnimationHelper.setColor(ColorSwitch.BACKGROUND_COLOR.sub(0, 0, 0, 1));
		this.screenAnimationHelper.setSize(this.game.getConfig().window.width, this.game.getConfig().window.height);

		this.screens = new HashMap<String, Screen>();
		this.screens.put("mainMenu", this.mainMenu = new MainMenu(this.batch, this.textureManager, this));
		SettingsConfig settingsConfig = this.game.getConfig().file.settings;
		this.screens.put("settings", new Settings(this.batch, this.textureManager, this,
				Gdx.files.getFileHandle(settingsConfig.settingsFile.getName(), settingsConfig.settingsFileType)));
		this.screens.put("info", new Info(this.batch, this.textureManager, this));
		this.screens.put("challenges", new Challenges(this.batch, this.textureManager, this));
		this.screens.put("prizes", new Prizes(this.batch, this.textureManager, this));
	}

	public void updateAnimations() {
		this.batch.begin();
		if (this.screenAnimation != null) {
			this.screenAnimation.execute(SCREEN_FADE_SPEED, Gdx.graphics.getDeltaTime());
		}
		this.screenAnimationHelper.draw(this.batch);
		this.batch.end();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && this.screenAnimation == null) {
			if (this.game.getScreen() == this.mainMenu) {
				Gdx.app.exit();
			} else {
				this.switchScreen(this.previousScreen);
			}
		}
	}

	public ScreenAnimation getDefaultAnimation() {
		return (fadeSpeed, deltaTime) -> {
			if (this.currentScreen != this.nextScreen) {
				this.screenAnimationHelper.setColor(this.screenAnimationHelper.getColor().add(0, 0, 0,
						SCREEN_FADE_SPEED * Gdx.graphics.getDeltaTime()));
				if (this.screenAnimationHelper.getColor().a == 1.0f) {
					this.game.setScreen(this.nextScreen);
					this.currentScreen = this.nextScreen;
				}
			} else {
				this.screenAnimationHelper.setColor(this.screenAnimationHelper.getColor().sub(0, 0, 0,
						SCREEN_FADE_SPEED * Gdx.graphics.getDeltaTime()));
				if (this.screenAnimationHelper.getColor().a == 0.0f) {
					this.screenAnimation = null;
				}
			}
		};
	}

	public void switchScreen(Screen newScreen) {
		try {
			this.previousScreen = (Screen) this.currentScreen;
			this.nextScreen = newScreen;
			this.screenAnimation = newScreen.getScreenAnimation();
		} catch (NullPointerException nullScreen) {
			LOGGER.error("Cannot switch to null screen");
			nullScreen.printStackTrace();
		}
	}

	public Texture getTexture(String name) {
		return this.textureManager.getTexture(name);
	}

	public void dispose() {
		this.screens.forEach((name, screen) -> screen.dispose());
	}

	public void invalidateScreenAnimation() {
		this.screenAnimation = null;
	}

	public boolean isScreenAnimationInvalidated() {
		return this.screenAnimation == null;
	}

	public Screen getScreen(String name) {
		return screens.get(name);
	}

	@Null
	public String getScreenName(Screen screen) {
		for(String name : screens.keySet()) {
			if(screens.get(name) == screen) {
				return name;
			}
		}
		return null;
	}

	public Map<String, Screen> getScreens() {
		return this.screens;
	}

	public Screen getPreviousScreen() {
		return previousScreen;
	}
}
