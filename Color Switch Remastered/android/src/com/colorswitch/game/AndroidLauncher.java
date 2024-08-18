package com.colorswitch.game;

import android.app.ActionBar;
import android.graphics.Color;
import android.icu.util.Measure;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.surfaceview.ResolutionStrategy;
import com.colorswitch.game.ColorSwitch;

import java.util.Objects;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new ColorSwitch(new GameConfig(true)), config);
		setImmersiveMode();
	}

	private void setImmersiveMode() {
		Window window = getWindow();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			// For Android 11 (API level 30) and above
			WindowInsetsController insetsController = window.getInsetsController();
			if (insetsController != null) {
				insetsController.hide(android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars());
				insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
			}
		} else {
			// For Android versions below 11
			window.getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
							| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
							| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); // immersive mode
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setImmersiveMode(); // Reapply immersive mode when the activity resumes
	}

}