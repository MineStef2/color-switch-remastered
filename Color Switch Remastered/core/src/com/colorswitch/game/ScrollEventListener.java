package com.colorswitch.game;

@FunctionalInterface
public interface ScrollEventListener {
	abstract void onScroll(float amountX, float amountY);
}
