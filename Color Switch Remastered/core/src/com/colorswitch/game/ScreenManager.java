package com.colorswitch.game;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.colorswitch.game.gui.button.Button;
import com.colorswitch.game.gui.button.ButtonHoverBehavior;
import com.colorswitch.game.gui.button.ButtonHoverOutBehavior;
import com.colorswitch.game.level.modes.GameMode;
import com.colorswitch.game.screens.GameModeScreen;
import com.colorswitch.game.screens.Screen;
import com.colorswitch.game.screens.ScreenAnimation;
import com.colorswitch.game.screens.ScreenFadingAssistant;
import com.colorswitch.game.screens.Screens;

public final class ScreenManager implements ProcessTimer {
	public static final float SCREEN_FADE_SPEED = 4.7f;
	private static final Logger LOGGER = new Logger(ScreenManager.class);
	public final ScreenFadingAssistant fadingAssistant;
	Screen currentScreen;
	Screen previousScreen;
	List<Screen> screenNavigator;
	Map<String, GameModeScreen> gameModeScreens;
	private List<Screen> allScreens;
	private final ColorSwitch game;
	private ScreenAnimation screenAnimation;
	private Map<Screens, Screen> referencedScreens;
	private List<ScrollEventListener> scrollEventListeners;

	public ScreenManager() {
		this.game = ColorSwitch.getInstance();
		this.fadingAssistant = new ScreenFadingAssistant();
		this.referencedScreens = new HashMap<Screens, Screen>();
		this.screenNavigator = new ArrayList<Screen>();
		this.gameModeScreens = new HashMap<String, GameModeScreen>();
		this.scrollEventListeners = new ArrayList<ScrollEventListener>();
		this.allScreens = new ArrayList<Screen>();
	}

	void bootstrap() {
		LOGGER.info("Bootstrapping screens...");
		Instant startTime = this.startTimer();
		this.game.getLevelManager().getRegisteredGameModes().forEach((gameMode) -> {
			GameModeScreen gameModeScreen = new GameModeScreen(gameMode);
			this.gameModeScreens.put(gameMode.getName(), gameModeScreen);
			this.allScreens.add(gameModeScreen);
		});
		for (Screens screen : Screens.values()) {
			Class<?> rootClass = screen.getClassSource();
			Constructor<?> constructor = rootClass.getDeclaredConstructors()[0];			/* There must be only one constructor
			 																				 * per screen subclass */
			try {
				Screen screenInstance = (Screen) constructor.newInstance((Object[]) null);
				this.referencedScreens.put(screen, screenInstance);
				this.allScreens.add(screenInstance);
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		this.allScreens.forEach((screen) -> screen.initializeUI());

		Screen mainMenu = this.getScreenInstance(Screens.MAIN_MENU);
		this.screenNavigator.add(0, mainMenu);
		if (!this.game.isReloaded()) {
			this.currentScreen = this.previousScreen = mainMenu;
			this.game.setScreen(mainMenu);
		}
		LOGGER.info("Bootstrapped screens in " + this.getStringMillisElapsed(startTime));
	}

	public void update(int currentMouseX, int currentMouseY, float deltaTime) {
		if (this.screenAnimation != null) {
			this.screenAnimation.animate(deltaTime, this, this.fadingAssistant, this.game, this.currentScreen);
			this.fadingAssistant.draw(this.game.getBatch());
		}

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && this.screenAnimation == null) {
			if (this.game.getActiveScreen() == Screens.MAIN_MENU) {
				Gdx.app.exit();
			} else {
				this.switchScreen(this.previousScreen);
			}
		}

		int flipped_mouseY = this.game.getConfig().window.height - currentMouseY;
		for (Button button: this.currentScreen.getRegisteredButtons()) {
			if (button.getBoundingRectangle().contains(currentMouseX, flipped_mouseY)) {
				this.onHover(button, currentMouseX, flipped_mouseY);
			} else {
				this.onHoverOut(button, currentMouseX, flipped_mouseY);
			}
		}
	}

	void onLeftClick(int screenX, int screenY, int buttonId) {
		if (buttonId != Buttons.LEFT) {
			return;
		}
		int flipped_screenY = this.game.getConfig().window.height - screenY;
		for (Button button: this.currentScreen.getRegisteredButtons()) {
			if (button.getBoundingRectangle().contains(screenX, flipped_screenY)) {
				if (this.isScreenAnimationInvalidated()) {
					this.game.getSoundManager().playSoundEffect(button.getClickSound());
				}
				try {
					button.getClickBehavior().onClick(button, screenX, flipped_screenY);
				} catch (NullPointerException nullBehavior) {
					if(!button.flag_nullClickBehavior) {
						button.flag_nullClickBehavior = true;
						LOGGER.warn("No click behavior for " + button);
					}
				}
			}
		}
//		if (Gdx.app.getType() != ApplicationType.Android) {
//			this.computeClickRelease(screenX, screenY, buttonId);
//			System.out.println("computing on windows");
//		} else {
//			if (Gdx.input.isTouched()) {
//				System.out.println("computing on android");
//				this.computeClickRelease(screenX, screenY, buttonId);
//			}
//		}
	}

	void onScroll(float amountX, float amountY) {
		this.scrollEventListeners.forEach((scrollEventListener) -> scrollEventListener.onScroll(amountX, amountY));
	}

	private void onHover(Button button, int xpos, int ypos) {
		ButtonHoverBehavior hoverBehavior = button.getHoverBehavior();
		if (hoverBehavior != null) {
			hoverBehavior.onHover(button, xpos, ypos);
		}
	}

	private void onHoverOut(Button button, int xpos, int ypos) {
		ButtonHoverOutBehavior hoverOutBehavior = button.getHoverOutbehavior();
		if (hoverOutBehavior != null) {
			hoverOutBehavior.onHoverOut(button, xpos, ypos);
		}
	}

	public void switchScreen(Screen newScreen) {
		try {
			if (Screens.fromInstance(newScreen) == Screens.PREVIOUS) {
				this.screenNavigator.remove(this.currentScreen);
				newScreen = this.previousScreen;
			} else if (Screens.fromInstance(newScreen) == Screens.MAIN_MENU) {
				List<Screen> fromMainMenu = this.screenNavigator.subList(1, this.screenNavigator.size());
				this.screenNavigator.removeAll(fromMainMenu);
			} else {
				this.screenNavigator.add(newScreen);
			}
			this.currentScreen = newScreen;
			this.updatePreviousScreen();
			this.screenAnimation = newScreen.getScreenAnimation();
		} catch (NullPointerException nullScreen) {
			LOGGER.error("Cannot switch to null screen");
			nullScreen.printStackTrace();
		}
	}

	void updatePreviousScreen() {
		try {
			Screen previousIndexedScreen = this.screenNavigator.get(this.screenNavigator.indexOf(this.currentScreen) - 1);
			this.previousScreen = previousIndexedScreen;
		} catch (IndexOutOfBoundsException outOfBoundsException) {
			this.previousScreen = this.getScreenInstance(Screens.MAIN_MENU);
		}
	}

	public void defaultScreenSwitchBehavior(Button button, float xpos, float ypos) {
		if (this.isScreenAnimationInvalidated()) {
			this.switchScreen(button.getBoundScreen());
		}
	}

	public void dispose() {
		this.referencedScreens.forEach((name, screen) -> screen.dispose());
		this.referencedScreens.clear();
		this.screenNavigator.clear();
		this.gameModeScreens.clear();
	}

	public Screen getScreenInstance(Screens screen) {
		return this.referencedScreens.get(screen);
	}

	public GameModeScreen getGameModeScreenInstance(String name) {
		return this.gameModeScreens.get(name);
	}

	public void addScrollEventListener(ScrollEventListener panel) {
		this.scrollEventListeners.add(panel);
	}

	public void removeScrollEventListener(ScrollEventListener panel) {
		this.scrollEventListeners.remove(panel);
	}

	public void invalidateScreenAnimation() {
		this.screenAnimation = null;
	}

	public boolean isScreenAnimationInvalidated() {
		return this.screenAnimation == null;
	}

	public List<Screen> getAllScreens() {
		return allScreens;
	}

	public Map<Screens, Screen> getScreens() {
		return referencedScreens;
	}
}
