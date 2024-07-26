package com.colorswitch.game.screens.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.DrawCall;
import com.colorswitch.game.screens.Screen;

public class Title implements DrawCall{
	private GUIComponent title;
	private GUIComponent[] circles = new GUIComponent[2];
	private static final Vector2 TITLE_SCALE = new Vector2(0.4f, 0.4f);
	private static final Vector2 CIRCLE_SCALE = new Vector2(0.4f, 0.4f);

	public Title(Texture titleTexture, Texture circleTexture, Screen owner) {
		this.title = new GUIComponent(titleTexture, owner).applyScale(TITLE_SCALE).flipYCoordinate().shiftPosition(ColorSwitch.findXAxisCenter(titleTexture.getWidth() * TITLE_SCALE.x), 0);
		float circleWidth = circleTexture.getWidth() * CIRCLE_SCALE.x;
		this.circles[0] = new GUIComponent(circleTexture, owner).applyScale(CIRCLE_SCALE).flipYCoordinate().setOriginInCenter().shiftPosition(ColorSwitch.findXAxisCenter(circleWidth) - circleWidth + 7, ColorSwitch.EDGE_OFFSET);
		this.circles[1] = new GUIComponent(circleTexture, owner).applyScale(CIRCLE_SCALE).flipYCoordinate().setOriginInCenter().shiftPosition(ColorSwitch.findXAxisCenter(circleWidth) + circleWidth - 3, ColorSwitch.EDGE_OFFSET + 2);
	}

	@Override
	public void draw(SpriteBatch batch) {
		this.circles[0].rotate(0.9f);
		this.circles[1].rotate(-0.9f);
	}

	public GUIComponent getTitle() {
		return title;
	}
}
