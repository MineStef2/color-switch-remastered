package com.colorswitch.game.screens.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.DrawCall;
import com.colorswitch.game.screens.Screen;

public class PlayButton implements DrawCall{
	private static final Vector2 LAYER_SCALE = new Vector2(0.4f, 0.4f);
	private final GUIComponent[] layers = new GUIComponent[4];

	public PlayButton(Texture layer1, Texture layer2, Texture layer3, Texture layer4, Screen owner) {
		this.layers[0] = new GUIComponent(layer1, owner).applyScale(LAYER_SCALE).shiftPosition(ColorSwitch.findXAxisCenter(layer1.getWidth() * LAYER_SCALE.x), ColorSwitch.findYAxisCenter(layer1.getHeight() * LAYER_SCALE.y)).setOriginInCenter();
		this.layers[1] = new GUIComponent(layer2, owner).applyScale(LAYER_SCALE).shiftPosition(ColorSwitch.findXAxisCenter(layer2.getWidth() * LAYER_SCALE.x), ColorSwitch.findYAxisCenter(layer2.getHeight() * LAYER_SCALE.y)).setOriginInCenter();
		this.layers[2] = new GUIComponent(layer3, owner).applyScale(LAYER_SCALE).shiftPosition(ColorSwitch.findXAxisCenter(layer3.getWidth() * LAYER_SCALE.x), ColorSwitch.findYAxisCenter(layer3.getHeight() * LAYER_SCALE.y)).setOriginInCenter();
		this.layers[3] = ColorSwitch.addButton(layer4, owner).applyScale(LAYER_SCALE).shiftPosition(ColorSwitch.findXAxisCenter(layer4.getWidth() * LAYER_SCALE.x), ColorSwitch.findYAxisCenter(layer4.getHeight() * LAYER_SCALE.y));
	}

	@Override
	public void draw(SpriteBatch batch) {
		this.layers[0].rotate(0.5f);
		this.layers[1].rotate(-0.5f);
		this.layers[2].rotate(0.5f);
	}

	public Button getButton() {
		return (Button) this.layers[3];
	}
}
