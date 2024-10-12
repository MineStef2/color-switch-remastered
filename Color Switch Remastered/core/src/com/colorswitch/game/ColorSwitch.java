package com.colorswitch.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ScreenUtils;
import com.colorswitch.game.GameConfig.FileConfig;
import com.colorswitch.game.GameConfig.FileConfig.AudioConfig;
import com.colorswitch.game.GameConfig.FileConfig.IconConfig;
import com.colorswitch.game.GameConfig.FileConfig.SettingsConfig;
import com.colorswitch.game.GameConfig.FileConfig.TextureConfig;
import com.colorswitch.game.GameConfig.FileConfig.UserConfig;
import com.colorswitch.game.GameConfig.GLConfig;
import com.colorswitch.game.GameConfig.WindowConfig;
import com.colorswitch.game.Logger.LogLevel;
import com.colorswitch.game.gui.button.Button;
import com.colorswitch.game.gui.button.Button.ButtonEventType;
import com.colorswitch.game.gui.button.ButtonHoverBehavior;
import com.colorswitch.game.gui.button.ButtonHoverOutBehavior;
import com.colorswitch.game.screens.Screen;
import com.colorswitch.game.screens.ScreenBinding;
import com.colorswitch.game.screens.Settings;
import com.colorswitch.game.sound.AudioOutput;
import com.colorswitch.game.sound.SoundManager;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class ColorSwitch extends Game {
	public static final Color BACKGROUND_COLOR = new Color(0.16078f, 0.16078f, 0.16078f, 1);
	public static final Vector2 ANDROID_SCALE = new Vector2(1, 1);
	public static final Vector2 COMMON_DESKTOP_SCALE = new Vector2(0.5f, 0.5f);
	public static final Color TRANSPARENT = new Color(1, 1, 1, 0);
	private static final Logger LOGGER = new Logger(ColorSwitch.class);
	private static final List<Button> REGISTERED_BUTTONS = new ArrayList<Button>();
	private static ColorSwitch instance;
	private final GameConfig config;
	private SpriteBatch batch;
	private TextureManager textureManager;
	private int currentMouseX = Integer.MIN_VALUE;
	private int currentMouseY = Integer.MIN_VALUE;
	private ScreenManager screenManager;
	private SoundManager soundManager;
	private User user;
	private boolean reloaded = false;

	public ColorSwitch(GameConfig config) {
		instance = this;
		this.config = config;
	}

	@Override
	public void create() {
		long startTime = System.currentTimeMillis();
		Gdx.app.setLogLevel(this.config.logLevel);
		LOGGER.info("Creating game");
		if (Gdx.app.getType() == ApplicationType.Android && this.config.window.width == 0 && this.config.window.height == 0) {
			TextureRegion screen = ScreenUtils.getFrameBufferTexture();
			this.config.window.width = screen.getRegionWidth();
			this.config.window.height = screen.getRegionHeight();
		}
		this.batch = new SpriteBatch();
		TextureConfig textureConfig = this.config.file.texture;
		UserConfig userConfig = this.config.file.user;
		this.user = new User(Gdx.files.getFileHandle(userConfig.dataFile.getName(), userConfig.dataFileType), this);
		this.textureManager = new TextureManager(Gdx.files.getFileHandle(textureConfig.texturesDir, textureConfig.texturesFileType),textureConfig.fileExtension);
		this.screenManager = new ScreenManager(this.textureManager, this.batch);
		AudioConfig audioConfig = this.config.file.audio;
		FileHandle musicDir = Gdx.files.getFileHandle(audioConfig.audioDir, audioConfig.audioFileType);
		this.soundManager = new SoundManager(musicDir, audioConfig.configFile, audioConfig.audioFileType);

		if (!reloaded) {
			Screen mainMenu = this.screenManager.getScreen("mainMenu");
			this.setScreen(mainMenu);
			this.screenManager.currentScreen = this.screenManager.previousScreen = mainMenu;
			LOGGER.info("Initialized core game");
			if (Settings.music.getValue()) {
				this.soundManager.getAudioOutput("mainMenu").play();
			}
		}

		REGISTERED_BUTTONS.forEach((button) -> {
			ScreenBinding binding = button.getScreenBinding();
			if (binding != null) {
				button.bindScreen(binding.bind());
			}
			button.reloadClickSound();
		});
		long timeElapsed = System.currentTimeMillis() - startTime;
		LOGGER.info("Finished creating game in "+ timeElapsed + "ms");
	}

	/**
	 * Developer note: Every animation speed value (rotation, fading, scaling, etc.)
	 * is affected by the deltaTime, hence the values will be higher than what they
	 * might seem.
	 **/
	@Override
	public void render() {
		super.render();
		int currentMouseX = Gdx.input.getX();
		int currentMouseY = Gdx.input.getY();
		if (this.currentMouseX == Integer.MIN_VALUE && this.currentMouseY == Integer.MIN_VALUE) {
			this.currentMouseX = currentMouseX;
			this.currentMouseY = currentMouseY;
		} else if (this.currentMouseX != currentMouseX || this.currentMouseY != currentMouseY) {
			this.currentMouseX = currentMouseX;
			this.currentMouseY = currentMouseY;
		}
		REGISTERED_BUTTONS.forEach((button) -> {
			int convertedCoord_mouseY = this.config.window.height - this.currentMouseY;
			if (button.checkEventAt(currentMouseX, convertedCoord_mouseY, ButtonEventType.HOVER)
					&& !Gdx.input.isTouched()
					&& !(Gdx.app.getType() == ApplicationType.Android)) {
				this.onHover(button, currentMouseX, convertedCoord_mouseY);
			}
			if (button.checkEventAt(currentMouseX, convertedCoord_mouseY, ButtonEventType.HOVER_OUT)
					&& !Gdx.input.isTouched()
					&& !(Gdx.app.getType() == ApplicationType.Android)) {
				this.onHoverOut(button, currentMouseX, convertedCoord_mouseY);
			}
			if (Gdx.input.justTouched()) {
				int clickX = currentMouseX;
				if (button.checkEventAt(clickX, convertedCoord_mouseY, ButtonEventType.CLICK)
						&& button.getOwner().isActive()) {
					this.onButtonClicked(button, currentMouseX, convertedCoord_mouseY);
				}
			}
		});
		this.screenManager.updateAnimations();

		// developer tools
		if (Gdx.input.isKeyJustPressed(Keys.F1) && this.config.logLevel == LogLevel.DEBUG.getId()) {
			LOGGER.debug("reloading!");
			this.reloaded = true;
			LOGGER.debug("current screen: " + format(this.getScreen()) + " (old)");
			Screen prevScreen = this.screenManager.previousScreen;
			LOGGER.debug("prev screen: " + format(prevScreen) + " (old)");
			String name = null;
			String prevName = null;
			for (Map.Entry<String, Screen> entry : this.screenManager.getScreens().entrySet()) {
				Screen target = entry.getValue();
				if (this.getScreen().equals(target)) {
					name = entry.getKey();
				}
				if (prevScreen.equals(target)) {
					prevName = entry.getKey();
				}
			}
			REGISTERED_BUTTONS.clear();
			this.dispose();
			this.create();
			Map<String, Screen> reloadedScreens = this.screenManager.getScreens();
			Screen screen = reloadedScreens.get(name);
			prevScreen = reloadedScreens.get(prevName);
			LOGGER.debug("post reload screen: " + format(screen));
			LOGGER.debug("post reload prev screen: " + format(prevScreen));
			this.setScreen(screen);
			this.screenManager.previousScreen = prevScreen;
		}
	}

	public static GameConfig processArgs(String[] args) {
		OptionParser optionParser = new OptionParser();
		optionParser.allowsUnrecognizedOptions();
		OptionSpec<Integer> widthSpec = optionParser.accepts("width").withRequiredArg().ofType(Integer.TYPE).defaultsTo(0);
		OptionSpec<Integer> heightSpec = optionParser.accepts("height").withRequiredArg().ofType(Integer.TYPE).defaultsTo(0);
		OptionSpec<String> titleSpec = optionParser.accepts("title").withRequiredArg().ofType(String.class).defaultsTo("Color Switch Remastered");
		OptionSpec<Void> windowDecoratedSpec = optionParser.accepts("windowDecorated");
		OptionSpec<Void> windowResizableSpec = optionParser.accepts("windowResizable");
		OptionSpec<Void> vsyncSpec = optionParser.accepts("vsync");
		OptionSpec<Integer> logLevelSpec = optionParser.accepts("logLevel").withRequiredArg().ofType(Integer.TYPE).defaultsTo(Logger.LogLevel.ERROR.getId());
		OptionSpec<String> textureFileExtSpec = optionParser.accepts("textureFileExtension").withRequiredArg().ofType(String.class).defaultsTo(".png");
		OptionSpec<FileType> textureFileTypeSpec = optionParser.accepts("textureFileType").withRequiredArg().ofType(FileType.class).defaultsTo(FileType.Internal);
		OptionSpec<String> texturesDirSpec = optionParser.accepts("texturesDir").withRequiredArg().ofType(String.class).defaultsTo("textures");
		OptionSpec<String> audioConfigFileSpec = optionParser.accepts("audioConfigPath").withRequiredArg().ofType(String.class).defaultsTo("audioConfig.json");
		OptionSpec<FileType> audioFileTypeSpec = optionParser.accepts("audioFileType").withRequiredArg().ofType(FileType.class).defaultsTo(FileType.Internal);
		OptionSpec<String> audioDirSpec = optionParser.accepts("audioDir").withRequiredArg().ofType(String.class).defaultsTo("audio");
		OptionSpec<Void> disableAudioSpec = optionParser.accepts("disableAudio");
		OptionSpec<String> settingsFileSpec = optionParser.accepts("settingsPath").withRequiredArg().ofType(String.class).defaultsTo("settings.json");
		OptionSpec<FileType> settingsFileTypeSpec = optionParser.accepts("settingsFileType").withRequiredArg().ofType(FileType.class).defaultsTo(FileType.Local);
		OptionSpec<String> glDebugMsgSeveritySpec = optionParser.accepts("glDebugMessageSeverity").withRequiredArg().ofType(String.class).defaultsTo("HIGH");
		OptionSpec<Void> glDebugMsgSpec = optionParser.accepts("enableGlDebugMessages");
		OptionSpec<String> iconFileSpec = optionParser.accepts("iconPath").withRequiredArg().ofType(String.class).defaultsTo("icon.jpg");
		OptionSpec<FileType> iconFileTypeSpec = optionParser.accepts("iconFileType").withRequiredArg().ofType(FileType.class).defaultsTo(FileType.Internal);
		OptionSpec<String> userDataFileSpec = optionParser.accepts("userDataPath").withRequiredArg().ofType(String.class).defaultsTo("userData.json");
		OptionSpec<FileType> userDataFileTypeSpec = optionParser.accepts("userDataFileType").withRequiredArg().ofType(FileType.class).defaultsTo(FileType.Local);
		OptionSet optionSet = optionParser.parse(args);
		List<?> unrecognizedOptions = optionSet.nonOptionArguments();
		if (!unrecognizedOptions.isEmpty()) {
			System.out.println("Found unrecognized options: " + unrecognizedOptions);
		}

		int windowWidth = widthSpec.value(optionSet);
		int windowHeight = heightSpec.value(optionSet);
		String title = titleSpec.value(optionSet);
		boolean decorated = optionSet.has(windowDecoratedSpec);
		boolean resizable = optionSet.has(windowResizableSpec);
		boolean vsync = optionSet.has(vsyncSpec);
		WindowConfig windowConfig = new WindowConfig(windowWidth, windowHeight, title, decorated, resizable, vsync);

		String textureFileExtension = textureFileExtSpec.value(optionSet).toLowerCase();
		if (!textureFileExtension.contains(".")) {
			String extension = textureFileExtension;
			textureFileExtension = "." + extension;
		}
		String texturesDir = texturesDirSpec.value(optionSet);
		FileType textureConfigFileType = textureFileTypeSpec.value(optionSet);
		TextureConfig textureConfig = new TextureConfig(textureFileExtension, texturesDir, textureConfigFileType);
		String audioConfigFile = audioConfigFileSpec.value(optionSet);
		String audioDir = audioDirSpec.value(optionSet);
		FileType audioFileType = audioFileTypeSpec.value(optionSet);
		boolean disableAudio = optionSet.has(disableAudioSpec);
		AudioConfig audioConfig = new AudioConfig(audioConfigFile, audioDir, audioFileType, disableAudio);
		File settingsFile = new File(settingsFileSpec.value(optionSet));
		FileType settingsFileType = settingsFileTypeSpec.value(optionSet);
		SettingsConfig settingsConfig = new SettingsConfig(settingsFile, settingsFileType);
		File iconFile = new File(iconFileSpec.value(optionSet));
		FileType iconFileType = iconFileTypeSpec.value(optionSet);
		IconConfig iconConfig = new IconConfig(iconFile, iconFileType);
		File userDataFile = new File(userDataFileSpec.value(optionSet));
		FileType userDataFileType = userDataFileTypeSpec.value(optionSet);
		UserConfig userConfig = new UserConfig(userDataFile, userDataFileType);
		FileConfig fileConfig = new FileConfig(textureConfig, audioConfig, iconConfig, settingsConfig, userConfig);

		String glDebugMsgSeverity = glDebugMsgSeveritySpec.value(optionSet);
		boolean enabledGlDebugMessages = optionSet.has(glDebugMsgSpec);
		GLConfig glConfig = new GLConfig(glDebugMsgSeverity, enabledGlDebugMessages);

		int logLevel = logLevelSpec.value(optionSet);
		return new GameConfig(windowConfig, fileConfig, glConfig, logLevel);
	}

	@Null @Debug
	public static String format(@Null Object object) {
		try {
			return object.toString().replace("com.colorswitch.game.screens.", "");
		} catch (NullPointerException nullObject) {
			return null;
		}
	}

	@Null @Debug
	public static String format(@Null Object object, String target) {
		try {
			return object.toString().replace(target, "");
		} catch (NullPointerException nullObject) {
			return null;
		}
	}

	public void onButtonClicked(Button button, float xpos, float ypos) {
		try {
			button.getClickBehavior().onClick(button, xpos, ypos);
		} catch (NullPointerException nullBehavior) {
			LOGGER.warn("Attempt to run a null onClick behavior, which is a stupid thing of course... Anyway here's the stack trace:");
			nullBehavior.printStackTrace();
		}
		AudioOutput clickSound = button.getClickSound();
		if (clickSound != null && Settings.soundEffects.getValue()) {
			clickSound.play();
		}
	}

	public void onHover(Button button, float xpos, float ypos) {
		ButtonHoverBehavior hoverBehavior = button.getHoverBehavior();
		if (hoverBehavior != null) {
			hoverBehavior.onHover(button, xpos, ypos);
		}
	}

	public void onHoverOut(Button button, float xpos, float ypos) {
		ButtonHoverOutBehavior hoverOutBehavior = button.getHoverOutbehavior();
		if (hoverOutBehavior != null) {
			hoverOutBehavior.onHoverOut(button, xpos, ypos);
		}
	}

	public static Button addButton(Texture texture, Vector2 position, Vector2 scale, Screen owner) {
		Button button = new Button(texture, position, scale, owner);
		REGISTERED_BUTTONS.add(button);
		return button;
	}

	public static Button addButton(Texture texture, Vector2 position, Screen owner) {
		return addButton(texture, position, new Vector2(1f, 1f), owner);
	}

	public static Button addButton(Texture texture, Screen owner) {
		return addButton(texture, new Vector2(0, 0), new Vector2(1f, 1f), owner);
	}

	public static Button addButton(Button button) {
		REGISTERED_BUTTONS.add(button);
		return button;
	}

	public static void removeButton(Button button) {
		REGISTERED_BUTTONS.remove(button);
	}

	public static Vector2 getPlatformScale(Vector2 desktopScale) {
		return Gdx.app.getType() == ApplicationType.Android ? ANDROID_SCALE : desktopScale;
	}

	public static Vector2 getPlatformScale() {
		return Gdx.app.getType() == ApplicationType.Android ? ANDROID_SCALE : COMMON_DESKTOP_SCALE;
	}

	@Override
	public void dispose() {
		super.dispose();
		this.screenManager.dispose();
	}

	public static List<Button> getRegisteredButtons() {
		return REGISTERED_BUTTONS;
	}

	public static ColorSwitch getInstance() {
		return instance;
	}

	public GameConfig getConfig() {
		return config;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	public User getUser() {
		return user;
	}
}
