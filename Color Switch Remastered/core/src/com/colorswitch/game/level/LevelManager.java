package com.colorswitch.game.level;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.colorswitch.game.Logger;
import com.colorswitch.game.level.modes.GameMode;

public final class LevelManager {
	private static final Logger LOGGER = new Logger(LevelManager.class);
	private final FileHandle modesDir;
	private List<GameMode> registeredGameModes;

	public LevelManager(FileHandle modesDir, String sourcesFileName) {
		LOGGER.info("Registering levels...");
		Instant startTime = Instant.now();

		/* FIXME: no exception handling */
		this.modesDir = modesDir;

		this.registeredGameModes = new ArrayList<GameMode>();

		List<FileHandle> modeFolders = new ArrayList<FileHandle>();
		if (Gdx.app.getType() != ApplicationType.Android) {
			String[] names = this.modesDir.readString().split("\n");
			for (int i = 0; i < names.length; i++) {
				if (!names[i].equals(sourcesFileName)) {
					modeFolders.add(Gdx.files.getFileHandle(this.concatDir(names[i]), modesDir.type()));
				}
			}
		} else {
			List<FileHandle> folders = List.of(this.modesDir.list());
			folders.forEach((folder) -> {
				if (!folder.name().equals(sourcesFileName)) {
					modeFolders.add(folder);
				}
			});
		}

		FileHandle sourceFiles = Gdx.files.getFileHandle(this.concatDir(sourcesFileName), modesDir.type());
		JsonValue sources = new JsonReader().parse(sourceFiles);
		sources.forEach((source) -> {
			FileHandle modeFolder = Gdx.files.getFileHandle(this.concatDir(source.name), modesDir.type());
			Class<?> classSource = null;
			try {
				classSource = Class.forName(source.asString());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			Constructor<?> constructor = classSource.getConstructors()[0];					/* There should be only one constructor per game mode subclass,
			 															                     * having only one FileHandle argument */
			try {
				GameMode gameMode = (GameMode) constructor.newInstance(new Object[] {modeFolder});
				this.registeredGameModes.add(gameMode);
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		});

		Instant endTime = Instant.now();
		LOGGER.info("Finished registering levels in "+ Duration.between(startTime, endTime).toMillis() + "ms");
	}

	public List<GameMode> getRegisteredGameModes() {
		return registeredGameModes;
	}

	private String concatDir(String name) {
		return this.modesDir.name() + "/" + name;
	}
}
