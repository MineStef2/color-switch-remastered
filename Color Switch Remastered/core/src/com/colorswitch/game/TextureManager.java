package com.colorswitch.game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public final class TextureManager {
	private final FileHandle file;
	private final String extension;
	private Map<String, Texture> registeredTextures = new HashMap<String, Texture>();

	public TextureManager(FileHandle file, String extension) {
		this.file = file;
		this.extension = extension;

		Gson gson = new GsonBuilder().setLenient().create();
		try(Reader reader = new FileReader(file.file())) {
			JsonReader jsonreader = gson.newJsonReader(reader);
			jsonreader.beginObject();
			this.read(jsonreader);
			jsonreader.endObject();
		} catch (FileNotFoundException e) {
			throw new GdxRuntimeException("file "+ this.file.name() +" does not exist");
		} catch (IOException e) {
			throw new GdxRuntimeException("file "+ this.file.name() +" is a directory when it should be a json file");
		}
	}

	private void read(JsonReader reader) {
		try {
			String name = reader.nextName();
			String path = reader.nextString() + this.extension;
			this.registeredTextures.put(name, new Texture(path));
			this.read(reader);
		} catch (IllegalStateException wrongType) {
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
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