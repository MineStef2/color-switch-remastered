package com.colorswitch.game.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.DrawCall;
import com.colorswitch.game.TextureManager;
import com.colorswitch.game.gui.Button;
import com.colorswitch.game.gui.GUIComponent;

public abstract class Screen extends ScreenAdapter{
	private SpriteBatch batch;
	protected final TextureManager textureManager;
	protected boolean active;
	private final List<DrawCall> drawCalls;
	private final List<GUIComponent> components;
	private Button backButton;

	public Screen(SpriteBatch batch, TextureManager textureManager) {
		this.batch = batch;
		this.drawCalls = new ArrayList<DrawCall>();
		this.components = new ArrayList<GUIComponent>();
		this.textureManager = textureManager;
		this.backButton = null;
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

	public void setBackButton(Button button) {
		this.backButton = button;
		this.backButton.applyScreenBinding(ColorSwitch.getInstance()::getPreviousScreen);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(ColorSwitch.BACKGROUND_COLOR, true);
		super.render(delta);
		this.batch.begin();
		this.drawCalls.forEach((call) -> call.draw(delta));
		this.batch.end();
	}

	public List<DrawCall> getDrawCalls() {
		return this.drawCalls;
	}

	public List<GUIComponent> getComponents() {
		return this.components;
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
}
