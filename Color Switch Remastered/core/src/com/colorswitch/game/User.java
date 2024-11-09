package com.colorswitch.game;

import java.io.FileWriter;
import java.io.IOException;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class User {
	private static final Logger LOGGER = new Logger(User.class);
	private final FileHandle dataFile;
	private int stars;
	private JsonValue challenges;

	public User(FileHandle dataFile) {
		this.dataFile = dataFile;

		LOGGER.info("Gathering user data...");
		if (!this.dataFile.exists()) {
			try {
				if (this.dataFile.file().createNewFile()) {
					this.resetDataAndFile();
					LOGGER.warn("Created new user data file since no " + this.dataFile + " was found");
				}
			} catch (IOException e) {}
		}

		JsonValue fields = null;
		try {
			fields = new JsonReader().parse(this.dataFile);
			if (fields == null) {
				LOGGER.warn("Writing new user data since " + this.dataFile + " is empty");
				this.resetDataAndFile();
			}
		} catch (Exception e) {
			LOGGER.error("Failed to read json fields, resetting data and file...");
			e.printStackTrace();
			this.resetDataAndFile();
		} finally {
			fields = new JsonReader().parse(this.dataFile);
		}
		try {
			this.stars = fields.get("stars").asInt();
			this.challenges = fields.get("challenges");

			// if challenges == null, roll new challenges and write them
		} catch (NullPointerException invalidChild) {
			LOGGER.error("Invalid child found!");
			invalidChild.printStackTrace();
		}
	}

	public void updateFile() {
		Json json = new Json(OutputType.json);
		try (FileWriter writer = new FileWriter(this.dataFile.file())) {
			json.setWriter(writer);
			json.writeObjectStart();
			this.writeData(json);
			json.writeObjectEnd();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void resetData() {
		this.stars = 0;
	}

	public void writeData(Json json) {
		json.writeValue("stars", this.stars);
	}

	public void resetDataAndFile() {
		this.resetData();
		this.updateFile();
	}

	public void addStars(int amount) {
		this.stars += amount;
		this.updateFile();
	}

	public void substractStars(int amount) {
		this.stars -= amount;
		this.updateFile();
	}

	public void setStars(int amount) {
		this.stars = amount;
		this.updateFile();
	}

	public int getStars() {
		return stars;
	}

	public JsonValue getChallenges() {
		return challenges;
	}
}
