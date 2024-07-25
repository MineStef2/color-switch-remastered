package com.colorswitch.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

@FunctionalInterface
public interface DrawCall {
	abstract void draw(SpriteBatch batch);
}
