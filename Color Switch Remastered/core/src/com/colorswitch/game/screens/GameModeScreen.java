package com.colorswitch.game.screens;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.GameConfig.WindowConfig;
import com.colorswitch.game.Logger;
import com.colorswitch.game.WindowUtils;
import com.colorswitch.game.gui.GUIComponent;
import com.colorswitch.game.gui.ScrollPanel;
import com.colorswitch.game.gui.StringComponent;
import com.colorswitch.game.gui.button.Button;
import com.colorswitch.game.level.Level;
import com.colorswitch.game.level.LevelDifficulty;
import com.colorswitch.game.level.modes.GameMode;

public class GameModeScreen extends Screen {
	private static final Logger LOGGER = new Logger(GameModeScreen.class);
	private static final Vector2 LEVEL_BUTTON_SCALE = new Vector2(0.43f, 0.43f);
	private ScrollPanel scrollPanel;
	private final GameMode gameMode;

	public GameModeScreen(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	@Override
	public void initializeUI() {
		LOGGER.debug("creating game mode screen "+ logFormat(this));
		this.scrollPanel = new ScrollPanel(this, 0, 30);

		GUIComponent top = this.addDefaultTopPadding();
		top.setColor(this.gameMode.getTopColor());
		this.removeDrawCall(top);

		StringComponent progress = new StringComponent(this.gameMode.getCompletedLevels() + " / " + this.gameMode.getTotalLevels(), this).setSize(40);
		progress.setPosition(new Vector2(WindowUtils.getCenterX(progress.getWidth()), top.getY() - progress.getHeight() / 2));
		this.scrollPanel.addComponent(progress);

		Object[] difficulties = this.gameMode.getExistingDifficulties().toArray();
		WindowConfig window = ColorSwitch.getInstance().getConfig().window;
		Vector2 scale = ColorSwitch.getPlatformScale();
		float totalWidth = 0;
		for (int i = 0; i < difficulties.length; i++) {
			totalWidth += ((LevelDifficulty) difficulties[i]).getIndicatorTexture().getWidth() * scale.x + EDGE_OFFSET / 4;
		}
		float xpos = (window.width - totalWidth) / 2 + EDGE_OFFSET / 8;
		float ypos = 0;
		for (int i = 0; i < difficulties.length; i++) {
			Button difficultySender = (Button) this.newButton(((LevelDifficulty) difficulties[i]).getIndicatorTexture())
					.changeHoverBehaviors(null, null).changeClickSound(null).applyScale(scale);
			ypos = progress.getY() - difficultySender.getHeight() - progress.getHeight() - EDGE_OFFSET;
			difficultySender.shiftPosition(xpos, ypos);
			xpos += difficultySender.getWidth() + EDGE_OFFSET / 4;
			this.scrollPanel.addComponent(difficultySender);
			ypos -= difficultySender.getHeight() + EDGE_OFFSET;
		}
		for (int i = 0; i < difficulties.length; i++) {
			GUIComponent difficultyBanner = new GUIComponent(this.getTexture("difficultyBanner_" + difficulties[i]), this)
					.applyScale(ColorSwitch.getPlatformScale());
			difficultyBanner.shiftPosition(WindowUtils.getCenterX(difficultyBanner.getWidth()), ypos);
			this.scrollPanel.addComponent(difficultyBanner);

			Texture sampleTexture = this.getTexture("levelSlot_available");				// 189 x 189
			float buttonWidth = sampleTexture.getWidth() * LEVEL_BUTTON_SCALE.x;
			float buttonHeight = sampleTexture.getHeight() * LEVEL_BUTTON_SCALE.y;
			float rowWidth = (buttonWidth + EDGE_OFFSET * 1.5f) * 4;

			final float startX = WindowUtils.getCenterX(rowWidth) + EDGE_OFFSET * 1.5f / 2;
			ypos -= buttonWidth + EDGE_OFFSET;
			xpos = startX;
			List<Integer> levelNumbers = this.gameMode.getLevels().get(difficulties[i]);
			for (int j = 1; j <= levelNumbers.size(); j++) {
				int levelNumber = levelNumbers.get(j - 1);

				Level level = new Level(levelNumber, (LevelDifficulty) difficulties[i], this.gameMode);
				Button levelButton = (Button) this.newButton(sampleTexture).bindScreen(level).applyScale(LEVEL_BUTTON_SCALE);
				levelButton.shiftPosition(xpos, ypos);

				StringComponent levelText = new StringComponent(String.valueOf(levelNumbers.get(j - 1)), this).setSize(40);
				float middleX = (levelButton.getWidth() + levelText.getWidth()) / 2;
				float middleY = (levelButton.getHeight() + levelText.getHeight()) / 2;
				Vector2 textPos = new Vector2(levelButton.getX() + middleX - levelText.getWidth(), levelButton.getY() + middleY);
				levelText.setPosition(textPos);

				xpos += buttonWidth + EDGE_OFFSET * 1.5f;
				this.scrollPanel.addComponent(levelButton);
				this.scrollPanel.addComponent(levelText);
				if (j % 4 == 0 && j < levelNumbers.size()) {
					xpos = startX;
					ypos -= buttonWidth + EDGE_OFFSET;
				}
			}
			ypos -= buttonHeight + EDGE_OFFSET;
		}

		this.scrollPanel.setTopHeightLimit(progress.getY());
		this.scrollPanel.setBottomHeightLimit(0);
		this.addDrawCall(top);
		this.addDefaultTitle(this.gameMode.getName().toUpperCase(), this.addDefaultBackButton());
	}


	@Override
	public void show() {
		super.show();
		this.scrollPanel.resetPositions();
	}

}
