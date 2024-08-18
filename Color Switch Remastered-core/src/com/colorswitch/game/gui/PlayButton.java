package com.colorswitch.game.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.screens.Screen;

public class PlayButton{
	public static final Vector2 DESKTOP_LAYER_SCALE = new Vector2(0.4f, 0.4f);
	public static final Vector2 PLATFORM_SCALE = ColorSwitch.getInstance().getConfig().androidInstance ? ColorSwitch.ANDROID_SCALE : DESKTOP_LAYER_SCALE;
	private static final float CIRCLE_ROTATION_SPEED = 85f;
	private final GUIComponent[] layers = new GUIComponent[4];

	public PlayButton(Texture layer1, Texture layer2, Texture layer3, Texture layer4, Screen owner) {
		this.layers[0] = new GUIComponent(layer1, owner).applyScale(PLATFORM_SCALE).shiftPosition(ColorSwitch.getInstance().findWindowCenterX(layer1.getWidth() * PLATFORM_SCALE.x), ColorSwitch.getInstance().findWindowCenterY(layer1.getHeight() * PLATFORM_SCALE.y)).setOriginInCenter().withDrawCall((deltaTime) -> this.layers[0].rotate(CIRCLE_ROTATION_SPEED * deltaTime));
		this.layers[1] = new GUIComponent(layer2, owner).applyScale(PLATFORM_SCALE).shiftPosition(ColorSwitch.getInstance().findWindowCenterX(layer2.getWidth() * PLATFORM_SCALE.x), ColorSwitch.getInstance().findWindowCenterY(layer2.getHeight() * PLATFORM_SCALE.y)).setOriginInCenter().withDrawCall((deltaTime) -> this.layers[1].rotate(-CIRCLE_ROTATION_SPEED * deltaTime));
		this.layers[2] = new GUIComponent(layer3, owner).applyScale(PLATFORM_SCALE).shiftPosition(ColorSwitch.getInstance().findWindowCenterX(layer3.getWidth() * PLATFORM_SCALE.x), ColorSwitch.getInstance().findWindowCenterY(layer3.getHeight() * PLATFORM_SCALE.y)).setOriginInCenter().withDrawCall((deltaTime) -> this.layers[2].rotate(CIRCLE_ROTATION_SPEED * deltaTime));
		this.layers[3] = ColorSwitch.addButton(layer4, owner).applyScale(PLATFORM_SCALE).shiftPosition(ColorSwitch.getInstance().findWindowCenterX(layer4.getWidth() * PLATFORM_SCALE.x), ColorSwitch.getInstance().findWindowCenterY(layer4.getHeight() * PLATFORM_SCALE.y));
	}

	public Button getButton() {
		return (Button) this.layers[3];
	}
}
