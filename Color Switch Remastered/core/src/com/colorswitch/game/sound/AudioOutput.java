package com.colorswitch.game.sound;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;
import com.colorswitch.game.Logger;

public class AudioOutput {
	private static final Logger LOGGER = new Logger(AudioOutput.class);
	private OnCompletionListener completionListener;
	private String audioType;
	private Sound sound;
	private Music music;
	private final String name;
	private float volume;
	private boolean loop;
	private boolean playing;
	private boolean flag_NullAudio = false;

	public AudioOutput(String name, float volume, boolean loop, String audioType) {
		this.name = name;
		this.volume = volume;
		this.loop = loop;
		this.audioType = audioType;
	}

	/** Invalidates any existing Music value**/
	public void setSound(Sound sound) {
		this.music = null;
		this.sound = sound;
	}

	/** Invalidates any existing Sound value **/
	public void setMusic(Music music) {
		this.sound = null;
		this.music = music;
	}

	public float getVolume() {
		return volume;
	}

	/** Only works for music audio types **/
	public void setVolume(float volume) {
		this.volume = volume;
		if (this.music != null) {
			this.music.setVolume(volume);
		}
	}

	private void attempt(Runnable runnable) {
		try {
			runnable.run();
		} catch (NullPointerException nullAudio) {
			if (!this.flag_NullAudio) {
				LOGGER.error("Audio attempt failed because " + name + " has null value");
				this.flag_NullAudio = true;
			}
		}
	}

	public void play() {
		this.attempt(() -> {
			if (this.audioType.equals("sound")) {
				if(this.loop) {
					this.sound.loop(this.volume);
				}else {
					this.sound.play(this.volume);
				}
			} else {
				this.music.setLooping(this.loop);
				this.music.play();
			}
			this.playing = true;
		});
	}

	public void pause() {
		this.attempt(() -> {
			if (this.audioType.equals("sound")) {
				this.sound.pause();
			} else {
				this.music.pause();
			}
			this.playing = false;
		});
	}

	public void resume() {
		this.attempt(() -> {
			if (this.audioType.equals("sound")) {
				this.sound.resume();
			} else {
				this.music.play();
			}
			this.playing = true;
		});
	}

	public void stop() {
		this.attempt(() -> {
			if (this.audioType.equals("sound")) {
				this.sound.stop();
			} else {
				this.music.stop();
			}
			this.playing = false;
		});
	}

	public String getName() {
		return name;
	}

	/** Only works for music types **/
	public void setCompletionListener(OnCompletionListener completionListener) {
		this.completionListener = completionListener;
		if(this.music != null) {
			this.music.setOnCompletionListener(completionListener);
		}
	}

	/** Will always return null if the type is sound **/
	public OnCompletionListener getCompletionListener() {
		return completionListener;
	}

	public boolean isLooping() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public boolean isPlaying() {
		return playing;
	}

	public Music getMusic() {
		return music;
	}

	public Sound getSound() {
		return sound;
	}
}
