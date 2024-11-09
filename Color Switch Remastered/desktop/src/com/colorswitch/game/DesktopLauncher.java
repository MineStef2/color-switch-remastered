package com.colorswitch.game;

import java.util.Arrays;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application.GLDebugMessageSeverity;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.colorswitch.game.Logger.LogLevel;
// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

	/*
	 * Available arguments:
	 * 	--width (int, default 0): used to specify the width of the window; mandatory argument on desktop environment;
	 *	 height is automatically scaled after width, using 1:2 or 9:18 ratio
	 * 	--title (String, default "Color Switch Remastered"): used to specify the title of the window
	 * 	--windowDecorated (void): used to specify whether the window is decorated or not; used in debug purposes
	 * 	--windowResizable (void): used to specify whether the window can be resized or not; used in debug purposes
	 * 	--vsync (void): used to specify whether the game uses vsync or not
	 * 	--logLevel (int, default ERROR or 1): used to specify what log level the game uses; use LogLevel.[LEVEL].getId() to get the id
	 * 	--textureFileExtension (String, default ".png"): used to specify the extension of the texture files; the string format should contain the dot '.' and a compatible image file type
	 * 	--textureFileType (FileType, default FileType.Internal): used to specify the location of the textures directory
	 * 	--texturesDir (String, default "textures"): used to specify the name of the directory in which the texture files are kept
	 * 	--audioConfigPath (String, default "audioConfig"): used to specify the name of the file in which the audio information is kept; must be a .json file
	 * 	--audioFileType (FileType, default FileType.Internal): used to specify the location of the audio directory
	 * 	--audioDir (String, default "audio"): used to specify the name of the directory in which the audio files and information are kept
	 * 	--disableAudio (void): used to disable the game's audio; for dev and debug purposes
	 * 	--settingsFile (String, default "settings"): used to specify the name of the file in which the setting information is kept
	 * 	--settingsFileType (FileType, default FileType.Local): used to specify the location of the settings file
	 * 	--glDebugMessageSeverity (String, default GLDebugMessageSeverity.NOTIFICATION): used to specify the level of gl messages that can be printed out
	 * 	--iconPath (String, default "icon.jpg"): used to specify the icon file name of the icon image; must contain a compatible image extension
	 * 	--iconFileType (FileType, default FileType.Internal): used to specify the location of the icon image file
	 * 	--userDataFile (String, default "userData"): used to specify the name of the file in which user data is kept
	 * 	--userDataFileType (FileType, default FileType.Local): used to specify the location of the userData file
	 * 	--modesDir (String, default "modes"): used to specify the name of the directory in which game mode information is kept
	 * 	--modesDirFileType (FileType, default FileType.Internal): used to specify the location of the modes directory
	 * 	--modesSourcesFile (String, default "sourceFiles"): used to specify the name o the .json file in which class sources are kept for game modes
	 * Non-arguments will be ignored, incorrect or incompatible argument values will throw an error
	 */
	public static final String[] DESKTOP_ARGS = new String[] {
			"--width", "490",
			"--logLevel", String.valueOf(LogLevel.DEBUG.getId()),
			"--glDebugMessageSeverity", GLDebugMessageSeverity.MEDIUM.name(),
			"--enableGlDebugMessages",
			"--iconPath", "textures/icon.jpg",
			"--windowDecorated",
			"--windowResizable",
//			"--disableAudio",
			"--vsync",
	};

	public static void main(String[] args) {
		GameConfig gameConfig = ColorSwitch.processArgs(concat(args, DESKTOP_ARGS));
		Lwjgl3ApplicationConfiguration appConfig = new Lwjgl3ApplicationConfiguration();
		appConfig.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate); //to avoid screen tearing
		appConfig.setTitle(gameConfig.window.title);
		appConfig.setDecorated(gameConfig.window.decorated);
		appConfig.setResizable(gameConfig.window.resizable);
		appConfig.setInitialBackgroundColor(ColorSwitch.BACKGROUND_COLOR);
		appConfig.enableGLDebugOutput(gameConfig.gl.enabledGlDebugMessages, System.out);
		appConfig.useVsync(gameConfig.window.vsync);
		appConfig.disableAudio(gameConfig.file.audio.disableAudio);
		appConfig.setHdpiMode(HdpiMode.Pixels);
		appConfig.setIdleFPS(10);
		appConfig.setWindowedMode(gameConfig.window.width, gameConfig.window.height);
		appConfig.setWindowListener(new Window(gameConfig));
		appConfig.setWindowIcon(gameConfig.file.icon.iconFileType, gameConfig.file.icon.iconFile.getPath());
		new Lwjgl3Application(new ColorSwitch(gameConfig), appConfig);
	}

	static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}