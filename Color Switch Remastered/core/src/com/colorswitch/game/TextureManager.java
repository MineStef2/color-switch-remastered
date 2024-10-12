package com.colorswitch.game;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public final class TextureManager {
	private static final Logger LOGGER = new Logger(TextureManager.class);
	private final FileHandle texturesFolder;
	private final String extension;
	private final AssetManager assetManager;

	public TextureManager(FileHandle texturesFolder, String extension) {
		LOGGER.info("Loading textures...");
		Instant startTime = Instant.now();
		this.texturesFolder = texturesFolder;
		this.extension = extension;
		this.assetManager = new AssetManager();

		List<String> fileNames = new ArrayList<String>();
		if (Gdx.app.getType() != ApplicationType.Android) {
			String[] names = this.texturesFolder.readString().split("\n");                     /* XXX */
			fileNames = List.of(names);
		} else {
			FileHandle[] files = this.texturesFolder.list();
			for (FileHandle file : files) {
				fileNames.add(file.name());
			}
		}
		fileNames.forEach((fileName) -> this.assetManager.load(texturesFolder.name() + "/" + fileName, Texture.class));
		this.assetManager.finishLoading();
		Instant endTime = Instant.now();
		LOGGER.info("Finished loading in " + Duration.between(startTime, endTime).toMillis() + "ms");
	}

	public String getFolderName() {
		return texturesFolder.name();
	}

	public String getExtension() {
		return extension;
	}

	Texture getTexture(String name) {
		return this.assetManager.get(this.texturesFolder.name() + "/" + name + this.extension, Texture.class);
	}
}