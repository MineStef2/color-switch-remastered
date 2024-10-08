package com.colorswitch.game;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate);
		config.setTitle("Color Switch Remastered");
		config.setWindowIcon(FileType.Internal, "icon.jpg");
		config.setDecorated(false);
//		config.setForegroundFPS(10);
		config.setIdleFPS(30);
		final int windowWidth = 480, windowHeight = 840;
		config.setWindowedMode(windowWidth, windowHeight);
		new Lwjgl3Application(new ColorSwitch(new GameConfig(false, windowWidth, windowHeight)), config);
	}
}
