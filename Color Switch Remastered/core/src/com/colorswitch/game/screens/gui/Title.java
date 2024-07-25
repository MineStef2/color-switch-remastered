package com.colorswitch.game.screens.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.DrawCall;
import com.colorswitch.game.screens.Screen;

public final class Title implements DrawCall{
	private GUIObject title;
	private GUIObject[] circles = new GUIObject[2];
	private static final Vector2 TITLE_SCALE = new Vector2(0.4f, 0.4f);
	private static final Vector2 CIRCLE_SCALE = new Vector2(0.4f, 0.4f);

	public Title(Texture mainTexture, Texture circleTexture, Screen owner) {
		this.title = new GUIObject(mainTexture, owner).applyScale(TITLE_SCALE).flipYCoord();
		this.title.setPosition(ColorSwitch.findXAxisCenter(this.title.getWidth()), this.title.getY());

		this.circles[0] = new GUIObject(circleTexture, owner).applyScale(CIRCLE_SCALE).flipYCoord();
		this.circles[0].shiftPosition(ColorSwitch.findXAxisCenter(this.circles[0].getWidth()) - this.circles[0].getWidth() + 7, ColorSwitch.EDGE_OFFSET);
		this.circles[0].setOriginCenter();

		this.circles[1] = new GUIObject(circleTexture, owner).applyScale(CIRCLE_SCALE).flipYCoord();
		this.circles[1].shiftPosition(ColorSwitch.findXAxisCenter(this.circles[1].getWidth()) + this.circles[1].getWidth() - 3, ColorSwitch.EDGE_OFFSET + 2);
		this.circles[1].setOriginCenter();
	}

	@Override
	public void draw(SpriteBatch batch) {
		this.circles[0].rotate(0.9f);
		this.circles[1].rotate(-0.9f);
	}
}
