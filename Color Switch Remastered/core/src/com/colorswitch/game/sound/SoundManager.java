package com.colorswitch.game.sound;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.colorswitch.game.Logger;
import com.colorswitch.game.ProcessTimer;
import com.colorswitch.game.screens.Settings;

public class SoundManager implements ProcessTimer{
	private static final Logger LOGGER = new Logger(SoundManager.class);
	private static final Map<String, AudioOutput> AUDIO_OUTPUTS = new HashMap<String, AudioOutput>();
	private final FileHandle audioDir;
	private final FileHandle configFile;

	public SoundManager(FileHandle audioDir, String configName) {
		LOGGER.info("Starting sound engine...");
		Instant startTime = this.startTimer();
		this.audioDir = audioDir;
		this.configFile = Gdx.files.getFileHandle(audioDir.name() + "/" + configName, audioDir.type());
		if (!this.configFile.exists()) {
			throw new GdxRuntimeException("File " + this.audioDir.file().getAbsolutePath() + " is not a directory");
		}
		LOGGER.info("Using audio config file [" + this.configFile.file().getAbsolutePath() + "]");

		JsonReader reader = new JsonReader();
		JsonValue objects = reader.parse(this.configFile);
		for(JsonValue object : objects) {
			String name = object.name;
			String source = object.get("source").asString();
			String type = object.get("audioType").asString();
			float volume = object.get("volume").asFloat() / 100;
			if(Gdx.app.getType() != ApplicationType.Android) {
				volume /= 10;
			}
			boolean loop = object.get("loop").asBoolean();

			AudioOutput audioOutput = new AudioOutput(name, volume, loop, type);
			FileHandle audioSource = Gdx.files.getFileHandle(audioDir.name() + "/" + source, audioDir.type());
			if (type.equals("sound")) {
				audioOutput.setSound(Gdx.audio.newSound(audioSource));
			} else {
				audioOutput.setMusic(Gdx.audio.newMusic(audioSource));
			}
			audioOutput.setVolume(volume);
			AUDIO_OUTPUTS.put(name, audioOutput);
		}
		LOGGER.info("Initialized sound engine in " + this.getTotalTimeElapsed(startTime));
	}

	public AudioOutput getAudioOutput(String name) {
		return AUDIO_OUTPUTS.get(name);
	}

	public void playSoundEffect(AudioOutput sound) {
		if (sound != null && Settings.soundEffects.getValue()) {
			sound.play();
		}
	}
}
