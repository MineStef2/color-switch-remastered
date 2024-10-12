package com.colorswitch.game.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.DrawCall;
import com.colorswitch.game.screens.Screen;

public class GUIComponent extends Sprite implements DrawCall {
	protected final Screen owner;
	private Vector2 scale;
	private final Vector2 originalDimension;
	private boolean flippedX;
	private boolean flippedY;
	private DrawCall additionalDrawCall;

	public GUIComponent(Texture texture, Screen owner, Vector2 scale, Vector2 position) {
		this(texture, owner, scale);
		this.setPosition(position.x, position.y);
	}

	public GUIComponent(Texture texture, Screen owner, Vector2 scale) {
		this(texture, owner);
		this.scale = scale;
		this.applyScale(scale);
	}

	public GUIComponent(Texture texture, Screen owner) {
		super(texture);
		this.owner = owner;
		this.originalDimension = new Vector2(this.getWidth(), this.getHeight());
		this.additionalDrawCall = null;
		this.owner.addComponent(this);
	}

	public GUIComponent applyScale(Vector2 scale) {
		if (scale != null) {
			this.scale = scale;
			this.setSize(this.originalDimension.x * scale.x, this.originalDimension.y * scale.y);
		}
		return this;
	}

	/** Note: This also flips the x axis of the shiftPosition() method **/
	public GUIComponent flipXCoordinate() {
		this.flippedX = !this.flippedX;
		float windowWidth = ColorSwitch.getInstance().getConfig().window.width;
		this.setPosition(this.flippedX
				? (windowWidth - this.getWidth() - this.getX())
				: (this.getX() - windowWidth + this.getWidth()), this.getY());
		return this;
	}

	/** Note: This also flips the y axis of the shiftPosition() method **/
	public GUIComponent flipYCoordinate() {
		this.flippedY = !this.flippedY;
		float windowHeight = ColorSwitch.getInstance().getConfig().window.height;
		this.setPosition(this.getX(), this.flippedY
				? (windowHeight - this.getHeight() - this.getY())
				: (this.getY() - windowHeight + this.getHeight()));
		return this;
	}

	public GUIComponent shiftPosition(float x, float y) {
		this.setPosition(this.getX() + (this.flippedX ? -x : x), this.getY() + (this.flippedY ? -y : y));
		return this;
	}

	public GUIComponent withDrawCall(DrawCall drawCall) {
		this.additionalDrawCall = drawCall;
		return this;
	}

	public GUIComponent setOriginInCenter() {
		this.setOriginCenter();
		return this;
	}

	public GUIComponent flipXAxis() {
		this.flip(true, false);
		return this;
	}

	public GUIComponent flipYAxis() {
		this.flip(false, true);
		return this;
	}

	@Override
	public void draw(float deltaTime) {
		super.draw(this.owner.getSpriteBatch());
		if (this.additionalDrawCall != null) {
			this.additionalDrawCall.draw(deltaTime);
		}
	}

	public Screen getOwner() {
		return owner;
	}

	public Vector2 getScale() {
		return scale;
	}
}
