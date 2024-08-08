package com.colorswitch.game;

@FunctionalInterface
public interface DrawCall {
	abstract void draw(float deltaTime);
}
