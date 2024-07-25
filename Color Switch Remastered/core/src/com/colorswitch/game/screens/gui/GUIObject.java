package com.colorswitch.game.screens.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.DrawCall;
import com.colorswitch.game.screens.Screen;

public class GUIObject extends Sprite implements DrawCall{
	private final Screen owner;
	private Vector2 scale;
	private boolean flippedX, flippedY;

	public GUIObject(Texture texture, Screen owner, Vector2 scale) {
		this(texture, owner);
		this.applyScale(scale);
	}

	public GUIObject(Texture texture, Screen owner) {
		super(texture);
		this.owner = owner;
		this.owner.addDrawCall(this);
	}

	public GUIObject applyScale(Vector2 scale) {
		this.setSize(this.getWidth() * scale.x, this.getHeight() * scale.y);
		return this;
	}

	public GUIObject flipXCoord() {
		this.flippedX = !this.flippedX;
		this.setPosition(this.flippedX ? ColorSwitch.WINDOW_WIDTH - this.getWidth() : this.getX() - ColorSwitch.WINDOW_WIDTH + this.getWidth(), this.getY());
		return this;
	}

	public GUIObject flipYCoord() {
		this.flippedY = !this.flippedY;
		this.setPosition(this.getX(), this.flippedY ? ColorSwitch.WINDOW_HEIGHT - this.getHeight() : this.getY() - ColorSwitch.WINDOW_HEIGHT + this.getHeight());
		return this;
	}

	public GUIObject flipBothCoords() {
		this.flipXCoord();
		this.flipYCoord();
		return this;
	}

	public void shiftPosition(float x, float y) {
		this.setPosition(this.getX() + (this.flippedX ? -x : x), this.getY() + (this.flippedY ? -y : y));
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
	}

	public Screen getOwner() {
		return owner;
	}

	public Vector2 getScale() {
		return scale;
	}
}
