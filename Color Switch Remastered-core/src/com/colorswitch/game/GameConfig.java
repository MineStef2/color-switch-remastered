package com.colorswitch.game;

public class GameConfig {
	public final boolean androidInstance;
	public int width, height;

	public GameConfig(boolean androidInstance, int width, int height) {
		this.androidInstance = androidInstance;
		this.width = width;
		this.height = height;
	}

	public GameConfig(boolean androidInstance) {
		this.androidInstance = androidInstance;
		this.width = this.height = 0;
	}
}
