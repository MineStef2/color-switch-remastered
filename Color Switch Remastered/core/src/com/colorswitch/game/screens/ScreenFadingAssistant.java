package com.colorswitch.game.screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.colorswitch.game.ColorSwitch;

public class ScreenFadingAssistant {
	private final Sprite assistant;
	private final ColorSwitch game;

	public ScreenFadingAssistant() {
		this.game = ColorSwitch.getInstance();
		this.assistant = new Sprite(game.getTextureManager().getTexture("screenFadingAssistant"));
		this.assistant.setColor(ColorSwitch.BACKGROUND_COLOR.sub(0, 0, 0, 1));
		this.assistant.setSize(game.getConfig().window.width + 1, game.getConfig().window.height);
		this.assistant.translateX(-1);
	}

	public void draw(Batch batch) {
		batch.begin();
		this.assistant.draw(batch);
		batch.end();
	}

	public void fadeIn(float amount, float deltaTime) {
		this.assistant.setColor(this.assistant.getColor().add(0, 0, 0, amount * deltaTime));
	}

	public void fadeOut(float amount, float deltaTime) {
		this.assistant.setColor(this.assistant.getColor().sub(0, 0, 0, amount * deltaTime));
	}

	public float getAlpha() {
		return this.assistant.getColor().a;
	}
}
