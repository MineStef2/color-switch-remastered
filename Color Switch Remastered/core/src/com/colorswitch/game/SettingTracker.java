package com.colorswitch.game;

import com.colorswitch.game.screens.Settings.SettingEntry;

@FunctionalInterface
public interface SettingTracker {
	abstract <T> void onSettingChanged(SettingEntry<T> setting, T oldValue, T newValue);
}
