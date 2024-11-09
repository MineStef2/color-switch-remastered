package com.colorswitch.game.level.modes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.colorswitch.game.level.LevelDifficulty;

public abstract class GameMode {
	private List<LevelDifficulty> existingDifficulties;
	private Map<LevelDifficulty, List<Integer>> levels;
	private final String name;
	private int totalLevels;
	private int completedLevels;
	private final String banner;
	private final Color topColor;

	public GameMode(FileHandle sourceFolder) {
		this.name = sourceFolder.name();
		FileHandle objectDataFile = sourceFolder.child("objectData.json");
		FileHandle levelDataFile = sourceFolder.child("levelData.json");

		JsonValue objectData = new JsonReader().parse(objectDataFile);					/* XXX: read objectData.json */
		this.banner = objectData.get("banner").asString();
		this.topColor = Color.valueOf(objectData.get("topColor").asString());

		this.existingDifficulties = new ArrayList<LevelDifficulty>(LevelDifficulty.values().length);
		this.levels = new HashMap<LevelDifficulty, List<Integer>>();
		JsonValue levels = objectData.get("levels");
		levels.forEach((level) -> {
			this.totalLevels++;
			int number = Integer.parseInt(level.name);
			LevelDifficulty difficulty = LevelDifficulty.valueOf(level.get("difficulty").asString());
			if (!this.existingDifficulties.contains(difficulty)) {
				this.existingDifficulties.add(difficulty);
				this.levels.put(difficulty, new ArrayList<Integer>());
				this.levels.get(difficulty).add(number);
			} else {
				this.levels.get(difficulty).add(number);
			}
		});

		JsonValue levelData = new JsonReader().parse(levelDataFile);					/* XXX: read levelData.json */
		this.completedLevels = levelData.get("completed").asInt();
	}

	public List<LevelDifficulty> getExistingDifficulties() {
		return existingDifficulties;
	}

	public Map<LevelDifficulty, List<Integer>> getLevels() {
		return levels;
	}

	public int getTotalLevels() {
		return totalLevels;
	}

	public int getCompletedLevels() {
		return completedLevels;
	}

	public String getBanner() {
		return banner;
	}

	public String getName() {
		return name;
	}

	public Color getTopColor() {
		return topColor;
	}
}
