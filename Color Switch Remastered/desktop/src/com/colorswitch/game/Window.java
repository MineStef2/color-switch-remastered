package com.colorswitch.game;

import org.lwjgl.glfw.GLFW;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application.GLDebugMessageSeverity;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;

public class Window implements Lwjgl3WindowListener {
	private Lwjgl3Window window;
	private final GameConfig config;
	private static final Logger LOGGER = new Logger(DesktopLauncher.class);

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

		int e;		// XXX: should make the window resizable in the future
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
	public void maximized(boolean isMaximized) {
	}

	@Override
	public void iconified(boolean isIconified) {
	}

	@Override
	public void focusLost() {
	}

	@Override
	public void focusGained() {
	}

	@Override
	public void filesDropped(String[] files) {
	}
}
