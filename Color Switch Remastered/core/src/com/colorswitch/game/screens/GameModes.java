package com.colorswitch.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.WindowUtils;
import com.colorswitch.game.gui.GUIComponent;
import com.colorswitch.game.gui.ScrollPanel;
import com.colorswitch.game.gui.StringComponent;
import com.colorswitch.game.gui.button.Button;
import com.colorswitch.game.level.modes.GameMode;

public class GameModes extends Screen{
	private static final Color TOP_COLOR = Color.valueOf("#1b69cd");
//	private static final Logger LOGGER = new Logger(GameModes.class);
	private ScrollPanel scrollPanel;

	@Override
	public void initializeUI() {
		GUIComponent top = this.addDefaultTopPadding();
		top.setColor(TOP_COLOR);
		this.addDefaultTitle("GAME MODES", this.addDefaultHomeButton());

		this.scrollPanel = new ScrollPanel(this, 0, 15);
		GUIComponent totalPane = new GUIComponent(this.getTexture("rectanglePane"), this)
				.applyScale(ColorSwitch.getPlatformScale());
		totalPane.shiftPosition(WindowUtils.getCenterX(totalPane.getWidth()),
				top.getY() - (top.getHeight() + totalPane.getHeight()) / 2.5f);
		totalPane.setColor(new Color(0, 0, 0, 0.4f));							/* FIXME magic numbers */
		this.scrollPanel.addComponent(totalPane);
		int totalCompleted = 0;
		int totalLevels = 0;
		Object[] gameModes = this.game.getLevelManager().getRegisteredGameModes().toArray();
		for (int i = 0; i < gameModes.length; i++) {
			GameMode gameMode = (GameMode) gameModes[i];
			totalCompleted += gameMode.getCompletedLevels();
			totalLevels += gameMode.getTotalLevels();
		}
		StringComponent total = new StringComponent("TOTAL: " + totalCompleted + " / " + totalLevels, this);
		total.setSize(45);
		total.setPosition(new Vector2(EDGE_OFFSET, totalPane.getY() + (totalPane.getHeight() + total.getHeight()) / 2));

		ColorSwitch.getInstance().getLevelManager().getRegisteredGameModes().forEach((gameMode) -> {
			Button gameModeButton = (Button) this.newGameModeButton(gameMode.getBanner(), gameMode.getName())
					.applyScale(ColorSwitch.getPlatformScale());
			gameModeButton.shiftPosition(0, totalPane.getY() - gameModeButton.getHeight()
					- (top.getY() - totalPane.getY()) + totalPane.getHeight());
			// offset banners, add completion percentage...
		});

		/* XXX: Implement level data reading*/
		this.scrollPanel.setTopHeightLimit(totalPane.getY());
		this.scrollPanel.setBottomHeightLimit(0);
	}

	@Override
	public void show() {
		super.show();
		this.scrollPanel.resetPositions();
	}
}
