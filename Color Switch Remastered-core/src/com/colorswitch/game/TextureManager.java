package com.colorswitch.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public final class TextureManager {
	private final FileHandle file;
	private final String extension;
	private Map<String, Texture> registeredTextures = new HashMap<String, Texture>();

	public TextureManager(FileHandle file, String extension) {
		this.file = file;
		this.extension = extension;

		JsonReader jsonReader = new JsonReader();
		JsonValue fields = jsonReader.parse(file);
		fields.forEach((field) -> {
			registeredTextures.put(field.name, new Texture(field.asString() + this.extension));
		});
	}

	public Texture getTexture(String name) {
		return this.registeredTextures.get(name);
	}

	public FileHandle getFile() {
		return file;
	}

	public String getExtension() {
		return extension;
	}
}