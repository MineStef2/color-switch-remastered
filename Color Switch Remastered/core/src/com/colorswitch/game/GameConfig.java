package com.colorswitch.game;

import java.io.File;

import com.badlogic.gdx.Files.FileType;

public class GameConfig {
	public final WindowConfig window;
	public final FileConfig file;
	public final GLConfig gl;
	public final int logLevel;

	public GameConfig(WindowConfig windowConfig, FileConfig fileConfig, GLConfig glConfig, int logLevel) {
		this.window = windowConfig;
		this.file = fileConfig;
		this.gl = glConfig;
		this.logLevel = logLevel;
	}

	public static class FileConfig {
		public final TextureConfig texture;
		public final AudioConfig audio;
		public final IconConfig icon;
		public final SettingsConfig settings;
		public final UserConfig user;
		public final GameModesConfig gameModes;

		public FileConfig(TextureConfig textureConfig, AudioConfig audioConfig, IconConfig iconConfig,
				SettingsConfig settingsConfig, UserConfig userConfig, GameModesConfig gameModesConfig) {
			this.audio = audioConfig;
			this.texture = textureConfig;
			this.icon = iconConfig;
			this.settings = settingsConfig;
			this.user = userConfig;
			this.gameModes = gameModesConfig;
		}

		static class TextureConfig {
			public final String texturesDir;
			public final String fileExtension;
			public final FileType texturesFileType;

			public TextureConfig(String fileExtension, String texturesDir, FileType texturesFileType) {
				this.texturesDir = texturesDir;
				this.fileExtension = fileExtension;
				this.texturesFileType = texturesFileType;
			}
		}

		static class AudioConfig {
			public final String configFile;
			public final String audioDir;
			public final FileType audioFileType;
			public final boolean disableAudio;

			public AudioConfig(String configFile, String audioDir, FileType audioFileType, boolean disableAudio) {
				this.configFile = configFile;
				this.audioFileType = audioFileType;
				this.audioDir = audioDir;
				this.disableAudio = disableAudio;
			}
		}

		static class IconConfig {
			public final File iconFile;
			public final FileType iconFileType;

			public IconConfig(File iconFile, FileType iconFileType) {
				this.iconFile = iconFile;
				this.iconFileType = iconFileType;
			}
		}

		public static class SettingsConfig {
			public final File settingsFile;
			public final FileType settingsFileType;

			public SettingsConfig(File settingsFile, FileType settingsFileType) {
				this.settingsFile = settingsFile;
				this.settingsFileType = settingsFileType;
			}
		}

		static class UserConfig {
			public final File dataFile;
			public final FileType dataFileType;

			public UserConfig(File dataFile, FileType dataFileType) {
				this.dataFile = dataFile;
				this.dataFileType = dataFileType;
			}
		}

		static class GameModesConfig {
			public final File modesDir;
			public final FileType modesDirFileType;
			public final File modeSourcesFile;

			public GameModesConfig(File modesDir, FileType modesDirFileType, File modeSourcesFile) {
				this.modesDir = modesDir;
				this.modesDirFileType = modesDirFileType;
				this.modeSourcesFile = modeSourcesFile;
			}
		}
	}

	public static class WindowConfig {
		public int width, height;
		public final String title;
		public final boolean decorated, resizable, vsync;

		public WindowConfig(int width, int height, String title, boolean decorated, boolean resizable, boolean vsync) {
			this.width = width;
			this.height = height;
			this.title = title;
			this.decorated = decorated;
			this.resizable = resizable;
			this.vsync = vsync;
		}
	}

	public static class GLConfig {
		public final String glDebugMessageSeverity;
		public final boolean enabledGlDebugMessages;

		public GLConfig(String glDebugMessageSeverity, boolean enabledGlDebugMessages) {
			this.glDebugMessageSeverity = glDebugMessageSeverity;
			this.enabledGlDebugMessages = enabledGlDebugMessages;
		}
	}
}
