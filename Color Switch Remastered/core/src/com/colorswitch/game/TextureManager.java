package com.colorswitch.game;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

public final class TextureManager implements ProcessTimer{
	private static final Logger LOGGER = new Logger(TextureManager.class);
	private final FileHandle texturesFolder;
	private final String extension;
	private final AssetManager assetManager;

	public TextureManager(FileHandle texturesFolder, String extension) {
		LOGGER.info("Loading textures...");
		Instant startTime = this.startTimer();
		this.texturesFolder = texturesFolder;
		this.extension = extension;
		this.assetManager = new AssetManager();

		List<String> fileNames = new ArrayList<String>();
		if (Gdx.app.getType() != ApplicationType.Android) {
			String[] names = this.texturesFolder.readString().split("\n");                     					/* XXX */
			fileNames = List.of(names);
		} else {
			for (FileHandle file : this.texturesFolder.list()) {
				fileNames.add(file.name());
			}
		}
		fileNames.forEach((fileName) -> this.assetManager.load(texturesFolder.name() + "/" + fileName, Texture.class));
		this.assetManager.finishLoading();
		LOGGER.info("Finished loading textures in " + this.getStringMillisElapsed(startTime));
	}

	public String getFolderName() {
		return texturesFolder.name();
	}

	public String getExtension() {
		return extension;
	}

	public Texture getTexture(String name) {
		try {
			return this.assetManager.get(this.texturesFolder.name() + "/" + name + this.extension, Texture.class);
		} catch (GdxRuntimeException assetNotLoaded) {
			LOGGER.error("Attempted to use texture "+ name + ", but it does not exist");
		}
		return new Texture("missingTexture.png");
	}
}