package com.colorswitch.game.level;

import com.colorswitch.game.level.modes.GameMode;
import com.colorswitch.game.screens.Screen;

public class Level extends Screen{
	private final int number;
	private final LevelDifficulty difficulty;
	private final GameMode owner;

	public Level(int number, LevelDifficulty difficulty, GameMode owner) {
		this.number = number;
		this.difficulty = difficulty;
		this.owner = owner;
	}

	@Override
	public void initializeUI() {
		//create objects...
	}

	public int getNumber() {
		return number;
	}

	public LevelDifficulty getDifficulty() {
		return difficulty;
	}

	public GameMode getOwner() {
		return owner;
	}
}
