package com.colorswitch.game.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.TextureManager;

public final class ScreenManager {
	public static final float SCREEN_FADE_SPEED = 4.7f;
	public Screen previousScreen, currentScreen, nextScreen;
	public final Sprite screenAnimationHelper;
	private final ColorSwitch game;
	private final TextureManager textureManager;
	private final SpriteBatch batch;
	private MainMenu mainMenu;
	private Settings settings;
	private ScreenAnimation screenAnimation;

	private List<Screen> screens;

	public ScreenManager(TextureManager textureManager, SpriteBatch batch) {
		this.game = ColorSwitch.getInstance();
		this.textureManager = textureManager;
		this.batch = batch;
		this.screenAnimationHelper = new Sprite(this.textureManager.getTexture("background"));
		this.screenAnimationHelper.setColor(ColorSwitch.BACKGROUND_COLOR.sub(0, 0, 0, 1));
		this.screenAnimationHelper.setSize(this.game.getConfig().width, this.game.getConfig().height);
	}

	public final void createScreens() {
		this.screens = new ArrayList<Screen>();
		this.screens.add(this.currentScreen = this.previousScreen = this.mainMenu = new MainMenu(this.batch, this.textureManager, this));
		this.screens.add(this.settings = new Settings(this.batch, this.textureManager, this));
	}

	public List<Screen> getScreens(){
		return this.screens;
	}

	public void updateAnimations() {
		this.batch.begin();
		if(this.screenAnimation != null) {
			this.screenAnimation.execute(SCREEN_FADE_SPEED, Gdx.graphics.getDeltaTime());
		}
		this.screenAnimationHelper.draw(this.batch);
		this.batch.end();

		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE) && this.screenAnimation == null) {
			if(this.game.getScreen() == this.mainMenu) {
				Gdx.app.exit();
			}else {
				this.switchScreen(this.previousScreen);
			}
		}
	}

	public ScreenAnimation getDefaultAnimation() {
		return (fadeSpeed, deltaTime) -> {
			if(this.currentScreen != this.nextScreen) {
				this.screenAnimationHelper.setColor(this.screenAnimationHelper.getColor().add(0, 0, 0, SCREEN_FADE_SPEED * Gdx.graphics.getDeltaTime()));
				if(this.screenAnimationHelper.getColor().a == 1.0f) {
					this.game.setScreen(this.nextScreen);
					this.currentScreen = this.nextScreen;
				}
			}else {
				this.screenAnimationHelper.setColor(this.screenAnimationHelper.getColor().sub(0, 0, 0, SCREEN_FADE_SPEED * Gdx.graphics.getDeltaTime()));
				if(this.screenAnimationHelper.getColor().a == 0.0f) {
					this.screenAnimation = null;
				}
			}
		};
	}

	public void switchScreen(Screen newScreen) {
		try {
			this.previousScreen = (Screen) this.game.getScreen();
			this.nextScreen = newScreen;
			this.nextScreen.getComponents().forEach((component) -> component.setColor(Color.WHITE));
			this.screenAnimation = newScreen.getScreenAnimation();
		} catch (NullPointerException nullScreen) {
			Gdx.app.error("ScreenManager$NullPointerException", "Cannot switch to null screen");
			nullScreen.printStackTrace();
		}
	}

	public void dispose() {
		this.mainMenu.dispose();
		this.settings.dispose();
	}

	public void invalidateScreenAnimation() {
		this.screenAnimation = null;
	}

	public boolean isScreenAnimationInvalidated() {
		return this.screenAnimation == null;
	}

	public MainMenu getMainMenu() {
		return mainMenu;
	}

	public Settings getSettings() {
		return settings;
	}

	public Screen getPreviousScreen() {
		return previousScreen;
	}
}
