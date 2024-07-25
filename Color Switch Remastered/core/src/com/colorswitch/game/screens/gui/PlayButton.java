package com.colorswitch.game.screens.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.DrawCall;
import com.colorswitch.game.screens.Screen;

public class PlayButton implements DrawCall{
	private static final Vector2 LAYER_SCALE = new Vector2(0.4f, 0.4f);
	private final GUIObject[] layers = new GUIObject[4];

	public PlayButton(Texture layer1, Texture layer2, Texture layer3, Texture layer4, Screen owner) {
		this.layers[0] = new GUIObject(layer1, owner);
		this.layers[1] = new GUIObject(layer2, owner);
		this.layers[2] = new GUIObject(layer3, owner);
		this.layers[3] = ColorSwitch.addButton(layer4, owner);

		for(GUIObject layer: this.layers) {
			layer.applyScale(LAYER_SCALE);
			layer.setPosition(ColorSwitch.findXAxisCenter(layer.getWidth()), ColorSwitch.findYAxisCenter(layer.getHeight()));
			layer.setOriginCenter();
		}
	}

	public Button getButton() {
		return (Button) this.layers[3];
	}

	@Override
	public void draw(SpriteBatch batch) {
		this.layers[0].rotate(0.5f);
		this.layers[1].rotate(-0.5f);
		this.layers[2].rotate(0.5f);
	}
}
