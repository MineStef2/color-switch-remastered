package com.colorswitch.game.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ScreenUtils;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.Debug;
import com.colorswitch.game.DrawCall;
import com.colorswitch.game.Logger;
import com.colorswitch.game.ScreenManager;
import com.colorswitch.game.SharedConstants;
import com.colorswitch.game.TextureManager;
import com.colorswitch.game.gui.GUIComponent;
import com.colorswitch.game.gui.StringComponent;
import com.colorswitch.game.gui.button.Button;
import com.colorswitch.game.sound.SoundManager;

public abstract class Screen extends ScreenAdapter {
	protected static final boolean androidInstance = Gdx.app.getType() == ApplicationType.Android;
	public static final float EDGE_OFFSET = androidInstance ? 50 : 20;
	public static final float STATUS_BAR_OFFSET = 70f;
	private static final Logger LOGGER = new Logger(Screen.class);
	protected ScreenManager screenManager;
	protected SoundManager soundManager;
	protected TextureManager textureManager;
	protected final ColorSwitch game;
	protected boolean active;
	private Button backButton;
	private SpriteBatch batch;
	private final List<DrawCall> drawCalls;
	private final List<GUIComponent> components;
	private List<Button> registeredButtons;
	private ScreenAnimation screenAnimation;

	public Screen() {
		LOGGER.debug("creating screen " + logFormat(this));
		this.game = ColorSwitch.getInstance();
		this.drawCalls = new ArrayList<DrawCall>();
		this.components = new ArrayList<GUIComponent>();
		this.screenManager = this.game.getScreenManager();
		this.soundManager = this.game.getSoundManager();
		this.textureManager = this.game.getTextureManager();
		this.registeredButtons = new ArrayList<Button>();
		this.screenAnimation = ScreenAnimation.DEFAULT_ANIMATION;
		this.batch = this.game.getBatch();
	}

	public abstract void initializeUI();

	public void addDrawCall(DrawCall call) {
		this.drawCalls.add(call);
	}

	public void addDrawCalls(DrawCall... calls) {
		for (DrawCall drawCall: calls) {
			this.addDrawCall(drawCall);
		}
	}

	public void removeDrawCall(DrawCall call) {
		this.drawCalls.remove(call);
	}

	public void removeDrawCalls(DrawCall... calls) {
		for (DrawCall drawCall: calls) {
			this.removeDrawCall(drawCall);
		}
	}

	public void addComponent(GUIComponent component) {
		this.components.add(component);
		this.drawCalls.add(component);
	}

	public void removeComponent(GUIComponent component) {
		this.components.remove(component);
		this.drawCalls.remove(component);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(ColorSwitch.BACKGROUND_COLOR);
		super.render(delta);
		this.batch.begin();
		this.drawCalls.forEach((call) -> call.draw(delta));
		this.batch.end();
	}

	protected Button addDefaultBackButton(Texture texture) {
		return this.backButton = (Button) this.newButton(texture)
				.bindScreen(Screens.PREVIOUS)
				.applyScale(SharedConstants.commonScale)
				.flipYCoordinate()
				.shiftPosition(EDGE_OFFSET, EDGE_OFFSET + (androidInstance ? Screen.STATUS_BAR_OFFSET : 0));
	}

	protected Button addDefaultBackButton() {
		return this.addDefaultBackButton(this.getTexture("back"));
	}

	protected Button addDefaultHomeButton() {
		return this.addDefaultBackButton(this.getTexture("home")).bindScreen(Screens.MAIN_MENU);
	}

	protected GUIComponent addDefaultTopPadding() {
		GUIComponent top = new GUIComponent(this.getTexture("top"), this)
				.applyScale(ColorSwitch.getPlatformScale()).flipYCoordinate();
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			top.translateY(20);
		}
		return top;
	}

	protected StringComponent addDefaultTitle(String text, Button backButton) {
		StringComponent title = new StringComponent(text, this)
				.setSize(50); 										/* invalid for android */
		title.setPosition(new Vector2(backButton.getX() + backButton.getWidth() + EDGE_OFFSET / 2,
				backButton.getY() + (backButton.getHeight() + title.getHeight()) / 2));
		return title;
	}

	@Null
	@Debug
	public static String logFormat(@Null Object object) {
		try {
			return object.toString().replace("com.colorswitch.game.screens.", "");
		} catch (NullPointerException nullObject) {
			return null;
		}
	}

	public Button newGameModeButton(String banner, String gameModeName) {
		return this.newButton(this.getTexture(banner))
				.bindScreen(this.screenManager.getGameModeScreenInstance(gameModeName));
	}

	public Button newButton(Texture texture, Vector2 position, Vector2 scale) {
		Button button = new Button(texture, position, scale, this);
		this.registeredButtons.add(button);
		return button;
	}

	public Button newButton(Texture texture, Vector2 position) {
		return this.newButton(texture, position, new Vector2(1f, 1f));
	}

	public Button newButton(Texture texture) {
		return this.newButton(texture, new Vector2(0, 0), new Vector2(1f, 1f));
	}

	public Button newButton(Button button) {
		this.registeredButtons.add(button);
		return button;
	}

	protected void removeCurrentBackButton() {
		this.registeredButtons.remove(this.backButton);
		this.backButton = null;
	}

	public Vector2 checkScale(Vector2 scale) {
		return androidInstance ? null : scale;
	}

	public void overrideScreenAnimation(ScreenAnimation newAnimation) {
		this.screenAnimation = newAnimation;
	}

	public List<DrawCall> getDrawCalls() {
		return this.drawCalls;
	}

	public List<GUIComponent> getComponents() {
		return this.components;
	}

	public List<Button> getRegisteredButtons() {
		return this.registeredButtons;
	}

	public Texture getTexture(String name) {
		return this.textureManager.getTexture(name);
	}

	@Override
	public void show() {
		super.show();
		this.active = true;
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void hide() {
		super.hide();
		this.active = false;
	}

	@Override
	public void dispose() {
		super.dispose();
		this.drawCalls.clear();
		this.components.clear();
		if (this.registeredButtons != null) {
			this.registeredButtons.clear();
		}
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public boolean isActive() {
		return active;
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	public ScreenAnimation getScreenAnimation() {
		return screenAnimation;
	}
}
