package com.colorswitch.game.level;

import com.badlogic.gdx.graphics.Texture;
import com.colorswitch.game.ColorSwitch;

public enum LevelDifficulty {
	easy("indicator_easy"), medium("indicator_medium"), hard("indicator_hard"), bonus("indicator_bonus"), secret("indicator_secret");

	private final Texture indicatorTexture;

	LevelDifficulty(String indicatorTexture) {
		this.indicatorTexture = ColorSwitch.getInstance().getTextureManager().getTexture(indicatorTexture);
	}

	public Texture getIndicatorTexture() {
		return indicatorTexture;
	}
}
