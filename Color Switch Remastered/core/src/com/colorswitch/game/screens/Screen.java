package com.colorswitch.game.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.DrawCall;
import com.colorswitch.game.Logger;
import com.colorswitch.game.ScreenManager;
import com.colorswitch.game.TextureManager;
import com.colorswitch.game.gui.GUIComponent;
import com.colorswitch.game.gui.button.Button;

public abstract class Screen extends ScreenAdapter {
	protected static final boolean androidInstance = Gdx.app.getType() == ApplicationType.Android;
	public static final float EDGE_OFFSET = androidInstance ? 50 : 20;
	public static final float STATUS_BAR_OFFSET = 70f;
	private static final Logger LOGGER = new Logger(Screen.class);
	protected final TextureManager textureManager;
	protected ScreenManager screenManager;
	protected final ColorSwitch game;
	protected boolean active;
	private Button backButton;
	private SpriteBatch batch;
	private final List<DrawCall> drawCalls;
	private final List<GUIComponent> components;
	private ScreenAnimation screenAnimation;

	public Screen(SpriteBatch batch, TextureManager textureManager, ScreenManager screenManager) {
		LOGGER.info("creating screen " + ColorSwitch.format(this));
		this.batch = batch;
		this.drawCalls = new ArrayList<DrawCall>();
		this.components = new ArrayList<GUIComponent>();
		this.textureManager = textureManager;
		this.screenManager = screenManager;
		this.screenAnimation = this.screenManager.getDefaultAnimation();
		this.game = ColorSwitch.getInstance();
	}

	public void addDrawCall(DrawCall call) {
		this.drawCalls.add(call);
	}

	public void removeDrawCall(DrawCall call) {
		this.drawCalls.remove(call);
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
		ScreenUtils.clear(ColorSwitch.BACKGROUND_COLOR, true);
		super.render(delta);
		this.batch.begin();
		this.drawCalls.forEach((call) -> call.draw(delta));
		this.batch.end();
	}

	protected Button addDefaultBackButton() {
		this.backButton = (Button) ColorSwitch.addButton(this.getTexture("back"), this)
				.applyScreen("mainMenu") 								/* XXX: not suitable for later screens */
				.applyScale(Button.PLATFORM_SCALE)
				.flipYCoordinate()
				.shiftPosition(EDGE_OFFSET, EDGE_OFFSET + (androidInstance ? Screen.STATUS_BAR_OFFSET : 0));
		return backButton;
	}

	protected void removeCurrentBackButton() {
		ColorSwitch.removeButton(this.backButton);
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

	public Texture getTexture(String name) {
		return this.screenManager.getTexture(name);
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
	}

	public TextureManager getTextureManager() {
		return textureManager;
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

	public ScreenAnimation getScreenAnimation() {
		return screenAnimation;
	}
}
