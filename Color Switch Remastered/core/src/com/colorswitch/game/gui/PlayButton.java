package com.colorswitch.game.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.screens.Screen;

public class PlayButton{
	private static final Vector2 LAYER_SCALE = new Vector2(0.4f, 0.4f);
	private static final float CIRCLE_ROTATION_SPEED = 85f;
	private final GUIComponent[] layers = new GUIComponent[4];

	public PlayButton(Texture layer1, Texture layer2, Texture layer3, Texture layer4, Screen owner) {
		this.layers[0] = new GUIComponent(layer1, owner).applyScale(LAYER_SCALE).shiftPosition(ColorSwitch.findWindowCenterX(layer1.getWidth() * LAYER_SCALE.x), ColorSwitch.findWindowCenterY(layer1.getHeight() * LAYER_SCALE.y)).setOriginInCenter().withDrawCall((deltaTime) -> this.layers[0].rotate(CIRCLE_ROTATION_SPEED * deltaTime));
		this.layers[1] = new GUIComponent(layer2, owner).applyScale(LAYER_SCALE).shiftPosition(ColorSwitch.findWindowCenterX(layer2.getWidth() * LAYER_SCALE.x), ColorSwitch.findWindowCenterY(layer2.getHeight() * LAYER_SCALE.y)).setOriginInCenter().withDrawCall((deltaTime) -> this.layers[1].rotate(-CIRCLE_ROTATION_SPEED * deltaTime));
		this.layers[2] = new GUIComponent(layer3, owner).applyScale(LAYER_SCALE).shiftPosition(ColorSwitch.findWindowCenterX(layer3.getWidth() * LAYER_SCALE.x), ColorSwitch.findWindowCenterY(layer3.getHeight() * LAYER_SCALE.y)).setOriginInCenter().withDrawCall((deltaTime) -> this.layers[2].rotate(CIRCLE_ROTATION_SPEED * deltaTime));
		this.layers[3] = ColorSwitch.addButton(layer4, owner).applyScale(LAYER_SCALE).shiftPosition(ColorSwitch.findWindowCenterX(layer4.getWidth() * LAYER_SCALE.x), ColorSwitch.findWindowCenterY(layer4.getHeight() * LAYER_SCALE.y));
	}

	public Button getButton() {
		return (Button) this.layers[3];
	}
}
