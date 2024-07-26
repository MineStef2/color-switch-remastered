package com.colorswitch.game.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.colorswitch.game.DrawCall;

public class Screen extends ScreenAdapter{
	protected SpriteBatch batch;
	private static final List<DrawCall> DRAW_CALLS = new ArrayList<DrawCall>();

	public Screen(SpriteBatch batch) {
		this.batch = batch;
	}

	public void addDrawCall(DrawCall object) {
		DRAW_CALLS.add(object);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		this.batch.begin();
		DRAW_CALLS.forEach((object) -> object.draw(this.batch));
		this.batch.end();
	}

	@Override
	public void show() {
		super.show();
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
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
