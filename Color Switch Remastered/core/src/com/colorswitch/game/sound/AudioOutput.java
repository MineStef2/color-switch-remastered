package com.colorswitch.game.sound;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;

public class AudioOutput {
	private OnCompletionListener completionListener = null;
	private Sound sound;
	private Music music;
	private final String name;
	private float volume;
	private boolean loop, playing;

	public AudioOutput(String name, float volume, boolean loop) {
		this.name = name;
		this.volume = volume;
		this.loop = loop;
	}

	/** Invalidates any existing Music value**/
	public void setSound(Sound sound) {
		this.music = null;
		this.sound = sound;
	}

	/** Invalidates any existing Sound value**/
	public void setMusic(Music music) {
		this.sound = null;
		this.music = music;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
		if(this.music != null) {
			this.music.setVolume(volume);
		}
	}

	public boolean isLooping() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public void play() {
		if(this.sound != null) {
			if(this.loop) {
				this.sound.loop(this.volume);
			}else {
				this.sound.play(this.volume);
			}
		}else {
			this.music.setLooping(loop);
			this.music.play();
		}
		this.playing = true;
	}

	public void pause() {
		if(this.sound != null) {
			this.sound.pause();
		}else {
			this.music.pause();
		}
		this.playing = false;
	}

	public void resume() {
		if(this.sound != null) {
			this.sound.resume();
		}else {
			this.music.play();
		}
		this.playing = true;
	}

	public void stop() {
		if(this.sound != null) {
			this.sound.stop();
		}else {
			this.music.stop();
		}
		this.playing = false;
	}

	public String getName() {
		return name;
	}

	/** Works only for music types **/
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
