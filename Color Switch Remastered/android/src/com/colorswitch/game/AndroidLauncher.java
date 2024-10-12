package com.colorswitch.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;


public class AndroidLauncher extends AndroidApplication {
	public static final String[] ANDROID_ARGS = new String[] {};

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new ColorSwitch(ColorSwitch.processArgs(ANDROID_ARGS)), config);
	}
}