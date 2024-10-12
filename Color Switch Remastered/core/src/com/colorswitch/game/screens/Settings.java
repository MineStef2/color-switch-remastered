package com.colorswitch.game.screens;

import static com.colorswitch.game.ColorSwitch.TRANSPARENT;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.Logger;
import com.colorswitch.game.ScreenManager;
import com.colorswitch.game.SettingTracker;
import com.colorswitch.game.TextureManager;
import com.colorswitch.game.WindowUtils;
import com.colorswitch.game.gui.GUIComponent;
import com.colorswitch.game.gui.button.Button;
import com.colorswitch.game.sound.AudioOutput;

public class Settings extends Screen {
	static final Map<String, SettingEntry<?>> SETTING_MAP = new HashMap<String, SettingEntry<?>>();
	public static SettingEntry<Boolean> music = new SettingEntry<Boolean>("music", ValueType.booleanValue);
	public static SettingEntry<Boolean> soundEffects = new SettingEntry<Boolean>("soundEffects", ValueType.booleanValue);
	public static SettingEntry<Boolean> vibrations = new SettingEntry<Boolean>("vibrations", ValueType.booleanValue);
	public static SettingEntry<Boolean> gameOverFlashFX = new SettingEntry<Boolean>("gameOverFlashFX", ValueType.booleanValue);
	private static final Logger LOGGER = new Logger(Settings.class);
	private static final String DEFAULT_SETTINGS = "{\"music\":true,\"gameOverFlashFX\":true,\"soundEffects\":true,\"vibrations\":true}";
	private static final Vector2 DESKTOP_SETTING_SCALE = new Vector2(0.45f, 0.45f);
	private static final Vector2 PLATFORM_SCALE = ColorSwitch.getPlatformScale(DESKTOP_SETTING_SCALE);
	private static final List<SettingTracker> SETTING_TRACKERS = new ArrayList<SettingTracker>();
	private final FileHandle file;
	private Button musicButton;
	private Button soundEffectsButton;
	private Button vibrationsButton;
	private Button flashFXButton;
	private GUIComponent flashFX;

	public Settings(SpriteBatch batch, TextureManager textureManager, ScreenManager screenManager, FileHandle file) {
		super(batch, textureManager, screenManager);
		this.file = file;

		Button back = this.addDefaultBackButton();
		GUIComponent title = new GUIComponent(this.getTexture("settings_title"), this)
				.applyScale(Button.PLATFORM_SCALE).flipYCoordinate().flipXCoordinate();
		title.shiftPosition(WindowUtils.getCenterX(title.getWidth()) - back.getWidth() / 2,
				EDGE_OFFSET + 5 + (androidInstance ? Screen.STATUS_BAR_OFFSET : 0));

		GUIComponent panel = new GUIComponent(this.getTexture("settingsPanel"), this)
				.applyScale(PLATFORM_SCALE);
		panel.shiftPosition(WindowUtils.getCenterX(panel.getWidth()), WindowUtils.getCenterY(panel.getHeight()));

		this.musicButton = (Button) ColorSwitch.addButton(this.getTexture("music"), this)
				.changeHoverBehaviors(null, null).applyClickBehavior((button, xpos, ypos) -> {
					this.<Boolean>changeSetting(music, !music.getValue());
					AudioOutput mainMenuAudio = this.game.getSoundManager().getAudioOutputs().get("mainMenu");
					if (music.getValue()) {
						this.musicButton.setColor(Color.WHITE);
						mainMenuAudio.resume();
					} else {
						this.musicButton.setColor(TRANSPARENT);
						mainMenuAudio.pause();
					}
				}).applyScale(PLATFORM_SCALE);
		this.musicButton.shiftPosition(this.musicButton.getWidth() - (androidInstance ? 20 : 12f),
				WindowUtils.getCenterY(this.musicButton.getHeight()) + this.musicButton.getHeight() / 1.505f
						+ (androidInstance ? 5 : 2.5f));

		this.soundEffectsButton = (Button) ColorSwitch.addButton(this.getTexture("audio"), this)
				.changeHoverBehaviors(null, null).applyClickBehavior((button, xpos, ypos) -> {
					this.<Boolean>changeSetting(soundEffects, !soundEffects.getValue());
					this.soundEffectsButton.setColor(soundEffects.getValue() ? Color.WHITE : TRANSPARENT);
				}).applyScale(PLATFORM_SCALE);
		this.soundEffectsButton.shiftPosition(WindowUtils.getCenterX(this.soundEffectsButton.getWidth()),
				WindowUtils.getCenterY(this.soundEffectsButton.getHeight())
						+ this.soundEffectsButton.getHeight() / 1.45f);

		this.vibrationsButton = (Button) ColorSwitch.addButton(this.getTexture("vibrations"), this)
				.changeHoverBehaviors(null, null).applyClickBehavior((button, xpos, ypos) -> {
					this.<Boolean>changeSetting(vibrations, !vibrations.getValue());
					this.vibrationsButton.setColor(vibrations.getValue() ? Color.WHITE : TRANSPARENT);
				}).applyScale(PLATFORM_SCALE);
		this.vibrationsButton.shiftPosition(this.musicButton.getX() - 0.5f, this.musicButton.getY() - 0.5f)
				.flipXCoordinate();

		this.flashFX = new GUIComponent(this.getTexture("power"), this).applyScale(PLATFORM_SCALE);
		this.flashFX.shiftPosition(this.flashFX.getWidth() - (androidInstance ? 40 : 20f),
				WindowUtils.getCenterY(this.flashFX.getHeight()) - this.flashFX.getHeight()
						- (androidInstance ? 16 : 8f));

		this.flashFXButton = (Button) ColorSwitch.addButton(this.getTexture("flashFxButton"), this)
				.changeHoverBehaviors(null, null).applyClickBehavior((button, xpos, ypos) -> {
					this.<Boolean>changeSetting(gameOverFlashFX, !gameOverFlashFX.getValue());
					this.flashFX.setColor(gameOverFlashFX.getValue() ? Color.WHITE : TRANSPARENT);
				}).applyScale(PLATFORM_SCALE);
		this.flashFXButton.shiftPosition(this.flashFXButton.getWidth() / 2.3544f,
				WindowUtils.getCenterY(this.flashFXButton.getHeight()) - this.flashFXButton.getHeight() - 3f);

		try {
			if (this.file.file().createNewFile()) {
				FileWriter writer = new FileWriter(this.file.file());
				writer.write(DEFAULT_SETTINGS);
				writer.close();
				LOGGER.info("Created new settings file [" + this.file.file().getPath() + "] with default setting values");
			} else {
				throw new IOException();
			}
		} catch (IOException e) {
			LOGGER.info("Found settings file [" + this.file.file().getPath() + "]");
		}
		JsonValue fields = new JsonReader().parse(this.file);
		if (fields == null) {
			LOGGER.error(this.file + " does not contain setting entries");
			throw new GdxRuntimeException(
					"Could not find setting entries in settings file " + this.file.file().getAbsolutePath());
		}
		try {
			SETTING_MAP.forEach((name, setting) -> setting.compute(fields));
			this.musicButton.setColor(music.getValue() ? Color.WHITE : TRANSPARENT);
			this.soundEffectsButton.setColor(soundEffects.getValue() ? Color.WHITE : TRANSPARENT);
			this.vibrationsButton.setColor(vibrations.getValue() ? Color.WHITE : TRANSPARENT);
			this.flashFX.setColor(gameOverFlashFX.getValue() ? Color.WHITE : TRANSPARENT);
		} catch (IllegalArgumentException noSuchValue) {
			noSuchValue.printStackTrace();
		}
	}

	public <T> void changeSetting(SettingEntry<T> setting, T newValue) {
		T oldValue = setting.changeValue(newValue);
		Json json = new Json(OutputType.json);
		try (FileWriter writer = new FileWriter(this.file.file())) {
			json.setWriter(writer);
			json.writeObjectStart();
			SETTING_MAP.forEach((name, setting1) -> {
				if (!setting1.equals(setting)) {
					json.writeValue(name, setting1.getValue());
				} else {
					json.writeValue(name, newValue);
				}
			});
			json.writeObjectEnd();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SETTING_TRACKERS.forEach((tracker) -> tracker.onSettingChanged(setting, oldValue, newValue));
	}

	public static void addSettingTracker(SettingTracker tracker) {
		SETTING_TRACKERS.add(tracker);
	}

	public static void removeSettingTracker(SettingTracker tracker) {
		SETTING_TRACKERS.remove(tracker);
	}

	@SuppressWarnings("unchecked")
	public static <T> T convert(JsonValue field) {
		String fieldValue = field.asString();
		switch (field.type()) {
		case booleanValue:
			return (T) Boolean.valueOf(fieldValue);
		case doubleValue:
			return (T) Double.valueOf(fieldValue);
		case longValue:
			return (T) Long.valueOf(fieldValue);
		case stringValue:
			return (T) fieldValue;
		default:
			return null;
		}
	}

	public FileHandle getFile() {
		return file;
	}

	public static final class SettingEntry<T> {
		private final ValueType type;
		private JsonValue field;
		private final String name;
		private T value;

		public SettingEntry(String name, ValueType type) {
			this.name = name;
			this.type = type;
			SETTING_MAP.put(this.name, this);
		}

		public void compute(JsonValue child) {
			this.field = child.get(this.name);
			this.field.setType(this.type);
			this.value = convert(this.field);
		}

		public T changeValue(T newValue) {
			T oldValue = this.value;
			this.value = newValue;
			return oldValue;
		}

		public T getValue() {
			return value;
		}

		public String getName() {
			return name;
		}
	}
}
