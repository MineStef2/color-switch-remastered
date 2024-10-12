package com.colorswitch.game.sound;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.colorswitch.game.Logger;

public class SoundManager {
	private static final Logger LOGGER = new Logger(SoundManager.class);
	private static final Map<String, AudioOutput> AUDIO_OUTPUTS = new HashMap<String, AudioOutput>();
	private final FileHandle musicDir;
	private final FileHandle configFile;

	public SoundManager(FileHandle musicDir, String configName, FileType fileType) {
		LOGGER.info("Starting sound engine...");
		this.musicDir = musicDir;
		this.configFile = Gdx.files.getFileHandle(musicDir.name() + "/" + configName, fileType);
		if (!this.configFile.exists()) {
			throw new GdxRuntimeException("File " + this.musicDir.file().getAbsolutePath() + " is not a directory");
		}
		LOGGER.info("Using audio config file [" + this.configFile.file().getAbsolutePath() + "]");

		JsonReader reader = new JsonReader();
		JsonValue objects = reader.parse(this.configFile);
		for(JsonValue object : objects) {
			String name = object.name;
			String source = object.get("source").asString();
			String type = object.get("audioType").asString();
			float volume = object.get("volume").asFloat() / 100;
			if(!(Gdx.app.getType() == Application.ApplicationType.Android)) {
				volume /= 10;
			}
			boolean loop = object.get("loop").asBoolean();

			AudioOutput audioOutput = new AudioOutput(name, volume, loop);
			FileHandle audioSource = Gdx.files.getFileHandle(musicDir.name() + "/" + source, fileType);
			if (type.equals("music")) {
				audioOutput.setMusic(Gdx.audio.newMusic(audioSource));
			} else {
				audioOutput.setSound(Gdx.audio.newSound(audioSource));
			}
			audioOutput.setVolume(volume);
			AUDIO_OUTPUTS.put(name, audioOutput);
		}
	}

	public AudioOutput getAudioOutput(String name) {
		return AUDIO_OUTPUTS.get(name);
	}

	public Map<String, AudioOutput> getAudioOutputs() {
		return AUDIO_OUTPUTS;
	}
}
