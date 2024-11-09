package com.colorswitch.game;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.colorswitch.game.GameConfig.FileConfig;
import com.colorswitch.game.GameConfig.FileConfig.AudioConfig;
import com.colorswitch.game.GameConfig.FileConfig.GameModesConfig;
import com.colorswitch.game.GameConfig.FileConfig.IconConfig;
import com.colorswitch.game.GameConfig.FileConfig.SettingsConfig;
import com.colorswitch.game.GameConfig.FileConfig.TextureConfig;
import com.colorswitch.game.GameConfig.FileConfig.UserConfig;
import com.colorswitch.game.GameConfig.GLConfig;
import com.colorswitch.game.GameConfig.WindowConfig;
import com.colorswitch.game.Logger.LogLevel;
import com.colorswitch.game.level.LevelManager;
import com.colorswitch.game.screens.GameModeScreen;
import com.colorswitch.game.screens.Screen;
import com.colorswitch.game.screens.Screens;
import com.colorswitch.game.screens.Settings;
import com.colorswitch.game.sound.SoundManager;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class ColorSwitch extends Game implements ProcessTimer {
	/*
	 * FIXME ASAP: The current scaling implementation is NOT okay;
	 * On desktop, the scale should be 0.43f instead of 0.5f, and on android it should be 1f (since it's
	 * the platform from which the original sprites were obtained)
	 */
	public static final Color BACKGROUND_COLOR = new Color(0.16078f, 0.16078f, 0.16078f, 1);
	public static final Vector2 ANDROID_SCALE = new Vector2(1, 1);
	public static final Vector2 COMMON_DESKTOP_SCALE = new Vector2(0.324f, 0.2917f);				/* XXX */
	public static final Color TRANSPARENT = new Color(1, 1, 1, 0);
	static Vector2 scale;
	private static final Logger LOGGER = new Logger(ColorSwitch.class);
	private static ColorSwitch instance;
	private final GameConfig config;
	private SpriteBatch batch;
	private TextureManager textureManager;
	private ScreenManager screenManager;
	private LevelManager levelManager;
	private SoundManager soundManager;
	private User user;
	private boolean reloaded = false;

	public ColorSwitch(GameConfig config) {
		instance = this;
		this.config = config;
	}

	@Override
	public void create() {
		Instant startTime = this.startTimer();
		Gdx.app.setLogLevel(this.config.logLevel);
		LOGGER.info("Creating game");

		WindowConfig window = this.config.window;
		if (Gdx.app.getType() == ApplicationType.Android && this.config.window.width == 0 && this.config.window.height == 0) {
			TextureRegion screen = ScreenUtils.getFrameBufferTexture();
			window.width = screen.getRegionWidth();
			window.height = screen.getRegionHeight();
		}
		SharedConstants.commonScale = new Vector2(SharedConstants.ASPECT_RATIO, SharedConstants.ASPECT_RATIO);
		this.batch = new SpriteBatch();

		UserConfig userConfig = this.config.file.user;
		this.user = new User(Gdx.files.getFileHandle(userConfig.dataFile.getName(), userConfig.dataFileType));

		TextureConfig textureConfig = this.config.file.texture;
		FileHandle texturesDir = Gdx.files.getFileHandle(textureConfig.texturesDir, textureConfig.texturesFileType);
		this.textureManager = new TextureManager(texturesDir, textureConfig.fileExtension);

		AudioConfig audioConfig = this.config.file.audio;
		FileHandle audioDir = Gdx.files.getFileHandle(audioConfig.audioDir, audioConfig.audioFileType);
		this.soundManager = new SoundManager(audioDir, audioConfig.configFile);

		GameModesConfig modesConfig = this.config.file.gameModes;
		FileHandle modesDir = Gdx.files.getFileHandle(modesConfig.modesDir.getName(), modesConfig.modesDirFileType);
		this.levelManager = new LevelManager(modesDir, modesConfig.modeSourcesFile.getName());

		this.screenManager = new ScreenManager();
		this.screenManager.bootstrap();

		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean scrolled(float amountX, float amountY) {
				screenManager.onScroll(amountX, amountY);
				return true;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int buttonId) {
				screenManager.onLeftClick(screenX, screenY, buttonId);
				return false;
			}

//			@Override
//			public boolean touchUp(int screenX, int screenY, int pointer, int buttonId) {
//				return false;
//			}
		});

		if (!this.reloaded) {
			LOGGER.info("Initialized core game");
			if (Settings.music.getValue()) {
				this.soundManager.getAudioOutput("mainMenu").play();
			}
		}

		LOGGER.info("Finished creating game in " + this.getTotalTimeElapsed(startTime));
	}

	/**
	 * Developer note: Every animation speed value (rotation, fading, scaling, etc.)
	 * is affected by deltaTime, hence the values will be higher than what they
	 * might seem.
	 **/
	@Override
	public void render() {
		super.render();
		int currentMouseX = Gdx.input.getX();
		int currentMouseY = Gdx.input.getY();

		// TODO: need to refactor input management because checking every frame can be slow
		this.screenManager.update(currentMouseX, currentMouseY, Gdx.graphics.getDeltaTime());

		/*
		 * The game is not tested for memory usage, if you encounter memory problems in
		 * the future, surround this code in a try-catch block with a OutOfMemoryError
		 *
		 * Another problem would be the number of draw calls being too high. If this
		 * happens, change the rendering engine with one that splits all the draw calls
		 * into multiple threads, then rendering them in each thread
		 */

		// developer tool
		if (Gdx.input.isKeyJustPressed(Keys.F1) && this.config.logLevel == LogLevel.DEBUG.getId()) {
			LOGGER.debug("reloading!");
			List<Screen> savedNavigator = this.screenManager.screenNavigator;
			Map<String, Integer> savedGameModeNavigator = new HashMap<String, Integer>();
			for (int i = 0; i < savedNavigator.size(); i++) {
				if (!(savedNavigator.get(i) instanceof GameModeScreen)) {
					continue;
				}

				for (Map.Entry<String, GameModeScreen> entry: this.screenManager.gameModeScreens.entrySet()) {
					if (screen.equals(entry.getValue())) {
						savedGameModeNavigator.put(entry.getKey(), i);
					}
				}
				savedNavigator.remove(i);
			}
			Screens[] savedReferences = new Screens[savedNavigator.size()];
			for (int i = 0; i < savedReferences.length; i++) {
				savedReferences[i] = Screens.fromInstance(savedNavigator.get(i));
			}
			LOGGER.debug("pre reload current screen: " + Screen.logFormat(this.screenManager.currentScreen) + " (old instance)");
			LOGGER.debug("pre reload previous screen: " + Screen.logFormat(this.screenManager.previousScreen) + " (old instance)");

			this.dispose();
			this.create();
			this.reloaded = true;
			savedNavigator = new ArrayList<Screen>(savedReferences.length);
			for (int i = 0; i < savedReferences.length; i++) {
				savedNavigator.add(this.screenManager.getScreenInstance(savedReferences[i]));
			}
			if (!savedGameModeNavigator.equals(Map.of())) {
				for (Map.Entry<String, Integer> entry: savedGameModeNavigator.entrySet()) {
					savedNavigator.add(entry.getValue(), this.screenManager.getGameModeScreenInstance(entry.getKey()));
				}
			}
			this.screenManager.screenNavigator = savedNavigator;
			this.screenManager.currentScreen = savedNavigator.get(savedNavigator.size() - 1);
			this.screenManager.updatePreviousScreen();
			this.setScreen(this.screenManager.currentScreen);
			LOGGER.debug("post reload current screen: " + Screen.logFormat(this.screenManager.currentScreen));
			LOGGER.debug("post reload previous screen: " + Screen.logFormat(this.screenManager.previousScreen));
		}
	}

	public static GameConfig processArgs(String[] args) {
		OptionParser optionParser = new OptionParser();
		optionParser.allowsUnrecognizedOptions();
		OptionSpec<Integer> widthSpec = optionParser.accepts("width").withRequiredArg().ofType(Integer.TYPE).defaultsTo(0);
		OptionSpec<String> titleSpec = optionParser.accepts("title").withRequiredArg().ofType(String.class).defaultsTo("Color Switch Remastered");
		OptionSpec<Void> windowDecoratedSpec = optionParser.accepts("windowDecorated");
		OptionSpec<Void> windowResizableSpec = optionParser.accepts("windowResizable");
		OptionSpec<Void> vsyncSpec = optionParser.accepts("vsync");
		OptionSpec<Integer> logLevelSpec = optionParser.accepts("logLevel").withRequiredArg().ofType(Integer.TYPE).defaultsTo(Logger.LogLevel.ERROR.getId());
		OptionSpec<String> textureFileExtSpec = optionParser.accepts("textureFileExtension").withRequiredArg().ofType(String.class).defaultsTo(".png");
		OptionSpec<FileType> textureFileTypeSpec = optionParser.accepts("textureFileType").withRequiredArg().ofType(FileType.class).defaultsTo(FileType.Internal);
		OptionSpec<String> texturesDirSpec = optionParser.accepts("texturesDir").withRequiredArg().ofType(String.class).defaultsTo("textures");
		OptionSpec<String> audioConfigFileSpec = optionParser.accepts("audioConfigPath").withRequiredArg().ofType(String.class).defaultsTo("audioConfig");
		OptionSpec<FileType> audioFileTypeSpec = optionParser.accepts("audioFileType").withRequiredArg().ofType(FileType.class).defaultsTo(FileType.Internal);
		OptionSpec<String> audioDirSpec = optionParser.accepts("audioDir").withRequiredArg().ofType(String.class).defaultsTo("audio");
		OptionSpec<Void> disableAudioSpec = optionParser.accepts("disableAudio");
		OptionSpec<String> settingsFileSpec = optionParser.accepts("settingsFile").withRequiredArg().ofType(String.class).defaultsTo("settings");
		OptionSpec<FileType> settingsFileTypeSpec = optionParser.accepts("settingsFileType").withRequiredArg().ofType(FileType.class).defaultsTo(FileType.Local);
		OptionSpec<String> glDebugMsgSeveritySpec = optionParser.accepts("glDebugMessageSeverity").withRequiredArg().ofType(String.class).defaultsTo("HIGH");
		OptionSpec<Void> glDebugMsgSpec = optionParser.accepts("enableGlDebugMessages");
		OptionSpec<String> iconFileSpec = optionParser.accepts("iconPath").withRequiredArg().ofType(String.class).defaultsTo("icon.jpg");
		OptionSpec<FileType> iconFileTypeSpec = optionParser.accepts("iconFileType").withRequiredArg().ofType(FileType.class).defaultsTo(FileType.Internal);
		OptionSpec<String> userDataFileSpec = optionParser.accepts("userDataFile").withRequiredArg().ofType(String.class).defaultsTo("userData");
		OptionSpec<FileType> userDataFileTypeSpec = optionParser.accepts("userDataFileType").withRequiredArg().ofType(FileType.class).defaultsTo(FileType.Local);
		OptionSpec<String> modesDirNameSpec = optionParser.accepts("modesDir").withRequiredArg().ofType(String.class).defaultsTo("modes");
		OptionSpec<FileType> modesDirFileTypeSpec = optionParser.accepts("modesDirFileType").withRequiredArg().ofType(FileType.class).defaultsTo(FileType.Internal);
		OptionSpec<String> modeSourcesFileSpec = optionParser.accepts("modeSourcesFile").withRequiredArg().ofType(String.class).defaultsTo("sourceFiles");
		OptionSet optionSet = optionParser.parse(args);
		List<?> unrecognizedOptions = optionSet.nonOptionArguments();
		if (!unrecognizedOptions.isEmpty()) {
			System.out.println("Found unrecognized options: " + unrecognizedOptions);
		}

		int windowWidth = widthSpec.value(optionSet);
		int windowHeight = windowWidth * 2;		// aspect ratio 9:18 or 1:2
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
		String audioConfigFile = audioConfigFileSpec.value(optionSet) + ".json";
		String audioDir = audioDirSpec.value(optionSet);
		FileType audioFileType = audioFileTypeSpec.value(optionSet);
		boolean disableAudio = optionSet.has(disableAudioSpec);
		AudioConfig audioConfig = new AudioConfig(audioConfigFile, audioDir, audioFileType, disableAudio);
		File settingsFile = new File(settingsFileSpec.value(optionSet) + ".json");
		FileType settingsFileType = settingsFileTypeSpec.value(optionSet);
		SettingsConfig settingsConfig = new SettingsConfig(settingsFile, settingsFileType);
		File iconFile = new File(iconFileSpec.value(optionSet));
		FileType iconFileType = iconFileTypeSpec.value(optionSet);
		IconConfig iconConfig = new IconConfig(iconFile, iconFileType);
		File userDataFile = new File(userDataFileSpec.value(optionSet) + ".json");
		FileType userDataFileType = userDataFileTypeSpec.value(optionSet);
		UserConfig userConfig = new UserConfig(userDataFile, userDataFileType);
		File modesDir = new File(modesDirNameSpec.value(optionSet));
		FileType modesDirFileType = modesDirFileTypeSpec.value(optionSet);
		File modeSourcesFile = new File(modeSourcesFileSpec.value(optionSet) + ".json");
		GameModesConfig modesConfig = new GameModesConfig(modesDir, modesDirFileType, modeSourcesFile);
		FileConfig fileConfig = new FileConfig(textureConfig, audioConfig, iconConfig, settingsConfig, userConfig, modesConfig);

		String glDebugMsgSeverity = glDebugMsgSeveritySpec.value(optionSet);
		boolean enabledGlDebugMessages = optionSet.has(glDebugMsgSpec);
		GLConfig glConfig = new GLConfig(glDebugMsgSeverity, enabledGlDebugMessages);

		int logLevel = logLevelSpec.value(optionSet);
		return new GameConfig(windowConfig, fileConfig, glConfig, logLevel);
	}

	public static Vector2 getPlatformScale(Vector2 desktopScale) {
		return Gdx.app.getType() == ApplicationType.Android ? ANDROID_SCALE : desktopScale;
	}

	public static Vector2 getPlatformScale() {
		return Gdx.app.getType() == ApplicationType.Android ? ANDROID_SCALE : SharedConstants.commonScale;
	}

	public void setScreen(Screens screen) {
		this.setScreen(this.screenManager.getScreenInstance(screen));
	}

	public Screens getActiveScreen() {
		return Screens.fromInstance((Screen) this.getScreen());
	}

	@Override
	public void dispose() {
		super.dispose();
		this.screenManager.dispose();
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

	public TextureManager getTextureManager() {
		return textureManager;
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	public User getUser() {
		return user;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public boolean isReloaded() {
		return reloaded;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}
}
