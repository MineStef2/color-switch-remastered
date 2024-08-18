package com.colorswitch.game.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.screens.Screen;

public class Title {
	private GUIComponent[] circles = new GUIComponent[2];
	public static final Vector2 DESKTOP_TITLE_SCALE = new Vector2(0.4f, 0.4f);
	private static final boolean androidInstance = ColorSwitch.getInstance().getConfig().androidInstance;
	public static final Vector2 PLATFORM_SCALE = androidInstance ? ColorSwitch.ANDROID_SCALE : DESKTOP_TITLE_SCALE;
	private static final float CIRCLE_ROTATION_SPEED = 129f;
	private final Screen owner;

	public Title(Texture titleTexture, Texture circleTexture, Screen owner) {
		this.owner = owner;
		new GUIComponent(titleTexture, owner).applyScale(PLATFORM_SCALE).flipYCoordinate().shiftPosition(ColorSwitch.getInstance().findWindowCenterX(titleTexture.getWidth() * PLATFORM_SCALE.x), (androidInstance ? Screen.STATUS_BAR_OFFSET : 0));
		float circleWidth = circleTexture.getWidth() * PLATFORM_SCALE.x;
		this.circles[0] = new GUIComponent(circleTexture, owner).applyScale(PLATFORM_SCALE).flipYCoordinate().setOriginInCenter().shiftPosition(ColorSwitch.getInstance().findWindowCenterX(circleWidth) - circleWidth  + (androidInstance ? 17.5f : 7), Screen.EDGE_OFFSET + (androidInstance ? Screen.STATUS_BAR_OFFSET : 0)).withDrawCall((deltaTime) -> this.circles[0].rotate(CIRCLE_ROTATION_SPEED * deltaTime));
		this.circles[1] = new GUIComponent(circleTexture, owner).applyScale(PLATFORM_SCALE).flipYCoordinate().setOriginInCenter().shiftPosition(ColorSwitch.getInstance().findWindowCenterX(circleWidth) + circleWidth  - (androidInstance ? 8 : 3), Screen.EDGE_OFFSET + (androidInstance ? 5 + Screen.STATUS_BAR_OFFSET : 2)).withDrawCall((deltaTime) -> this.circles[1].rotate(-CIRCLE_ROTATION_SPEED * deltaTime));
	}

	public Screen getOwner() {
		return owner;
	}
}
