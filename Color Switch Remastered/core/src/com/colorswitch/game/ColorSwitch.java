package com.colorswitch.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.screens.MainMenu;
import com.colorswitch.game.screens.Screen;
import com.colorswitch.game.screens.gui.Button;
import com.colorswitch.game.screens.gui.Button.ButtonEventType;
import com.colorswitch.game.screens.gui.ButtonEventListener;


public class ColorSwitch extends Game {
	public SpriteBatch batch;
	private MainMenu mainMenuScreen;
	public static final Color BACKGROUND_COLOR = new Color(0.16078f, 0.16078f, 0.16078f, 1);
	public static final int WINDOW_WIDTH = 480, WINDOW_HEIGHT = 840;
	public static final float EDGE_OFFSET = 20;
	private static final List<Button> REGISTERED_BUTTONS = new ArrayList<Button>();
	private static final List<ButtonEventListener> BUTTON_EVENT_LISTENERS = new ArrayList<ButtonEventListener>();
	private int currentMouseX = Integer.MIN_VALUE, currentMouseY = Integer.MIN_VALUE;


	@Override
	public void create() {
		this.batch = new SpriteBatch();
		this.mainMenuScreen = new MainMenu(this.batch);
		this.setScreen(this.mainMenuScreen);
	}

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
				int convertedCoord_mouseY = WINDOW_HEIGHT - this.currentMouseY;
				if(button.checkEventAt(currentMouseX, convertedCoord_mouseY, ButtonEventType.HOVER)) {
					BUTTON_EVENT_LISTENERS.forEach((listener) -> listener.onHover(button, this.currentMouseX, convertedCoord_mouseY));
				}
				if(button.checkEventAt(currentMouseX, convertedCoord_mouseY, ButtonEventType.HOVER_OUT)) {
					BUTTON_EVENT_LISTENERS.forEach((listener) -> listener.onHoverOut(button, this.currentMouseX, convertedCoord_mouseY));
				}
			});
		}

		if(Gdx.input.justTouched()) {
			REGISTERED_BUTTONS.forEach((button) -> {
				int clickX = currentMouseX;
				int convertedCoord_clickY = WINDOW_HEIGHT - currentMouseY;
				if(button.checkEventAt(clickX, convertedCoord_clickY, ButtonEventType.CLICK)) {
					BUTTON_EVENT_LISTENERS.forEach((listener) -> listener.onButtonClicked(button, clickX, convertedCoord_clickY));
				}
			});
		}

		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE) && this.getScreen() == this.mainMenuScreen) {
			Gdx.app.exit();
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

	public static float findXAxisCenter(float componentWidth) {
		return (WINDOW_WIDTH - componentWidth) / 2;
	}

	public static float findYAxisCenter(float componentHeight) {
		return (WINDOW_HEIGHT - componentHeight) / 2;
	}

	public static void addButtonEventListener(ButtonEventListener listener) {
		BUTTON_EVENT_LISTENERS.add(listener);
	}

	@Override
	public void dispose() {
		super.dispose();
		this.mainMenuScreen.dispose();
	}

	public static List<Button> getRegisteredButtons() {
		return REGISTERED_BUTTONS;
	}
}
