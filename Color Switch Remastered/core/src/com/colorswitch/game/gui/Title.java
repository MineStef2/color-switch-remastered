package com.colorswitch.game.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.screens.Screen;

public class Title {
	private GUIComponent[] circles = new GUIComponent[2];
	private static final Vector2 TITLE_SCALE = new Vector2(0.4f, 0.4f);
	private static final Vector2 CIRCLE_SCALE = new Vector2(0.4f, 0.4f);
	private static final float CIRCLE_ROTATION_SPEED = 129f;
	private final Screen owner;

	public Title(Texture titleTexture, Texture circleTexture, Screen owner) {
		this.owner = owner;
		new GUIComponent(titleTexture, owner).applyScale(TITLE_SCALE).flipYCoordinate().shiftPosition(ColorSwitch.findWindowCenterX(titleTexture.getWidth() * TITLE_SCALE.x), 0);
		float circleWidth = circleTexture.getWidth() * CIRCLE_SCALE.x;
		this.circles[0] = new GUIComponent(circleTexture, owner).applyScale(CIRCLE_SCALE).flipYCoordinate().setOriginInCenter().shiftPosition(ColorSwitch.findWindowCenterX(circleWidth) - circleWidth + 7, ColorSwitch.EDGE_OFFSET).withDrawCall((deltaTime) -> this.circles[0].rotate(CIRCLE_ROTATION_SPEED * deltaTime));
		this.circles[1] = new GUIComponent(circleTexture, owner).applyScale(CIRCLE_SCALE).flipYCoordinate().setOriginInCenter().shiftPosition(ColorSwitch.findWindowCenterX(circleWidth) + circleWidth - 3, ColorSwitch.EDGE_OFFSET + 2).withDrawCall((deltaTime) -> this.circles[1].rotate(-CIRCLE_ROTATION_SPEED * deltaTime));
	}

	public Screen getOwner() {
		return owner;
	}
}
