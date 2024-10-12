package com.colorswitch.game;

import java.util.Arrays;

import org.lwjgl.glfw.GLFW;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application.GLDebugMessageSeverity;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.colorswitch.game.Logger.LogLevel;
// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static final String[] DESKTOP_ARGS = new String[] {
			"--width", "480",
			"--height", "840",
			"--logLevel", String.valueOf(LogLevel.DEBUG.getId()), //for development, on release will be on ERROR (default)
			"--glDebugMessageSeverity", GLDebugMessageSeverity.MEDIUM.name(),
			"--enableGlDebugMessages",
			"--iconPath", "textures/icon.jpg",
			//on release, both gl debug message args will not be in this list
			"--windowDecorated", //for development (debugging and UI code)
			"--vsync",
	};
	private static final Logger LOGGER = new Logger(DesktopLauncher.class);

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

	public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

	public static class Window implements Lwjgl3WindowListener {
		private Lwjgl3Window window;
		private final GameConfig config;

		public Window(GameConfig config) {
			this.config = config;
		}

		@Override
		public void created(Lwjgl3Window window) {
			GLDebugMessageSeverity severity = GLDebugMessageSeverity.valueOf(this.config.gl.glDebugMessageSeverity);
			boolean enabled = this.config.gl.enabledGlDebugMessages;

			if (Lwjgl3Application.setGLDebugMessageControl(severity, enabled)) {
				LOGGER.info("GLDebugMessageSeverity: "+ severity +", enabled: "+ enabled);
			} else {
				LOGGER.error("Could not apply GLDebugMessageSeverity, this is usually due to a window error");
			}
			this.window = window;
			this.window.setSizeLimits(config.window.width, config.window.height,
					config.window.width, config.window.height);
		}

		@Override
		public boolean closeRequested() {
			return GLFW.glfwWindowShouldClose(this.window.getWindowHandle());
		}

		@Override
		public void refreshRequested() {
			this.window.restoreWindow();
		}

		@Override
		public void maximized(boolean isMaximized) {}

		@Override
		public void iconified(boolean isIconified) {}

		@Override
		public void focusLost() {}

		@Override
		public void focusGained() {}

		@Override
		public void filesDropped(String[] files) {}
	}
}