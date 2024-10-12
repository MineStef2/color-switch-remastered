package com.colorswitch.game.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.colorswitch.game.ColorSwitch;
import com.colorswitch.game.ScreenManager;
import com.colorswitch.game.TextureManager;
import com.colorswitch.game.User;
import com.colorswitch.game.WindowUtils;
import com.colorswitch.game.gui.GUIComponent;
import com.colorswitch.game.gui.StringComponent;
import com.colorswitch.game.gui.button.Button;

public class Challenges extends Screen {
	private static final Color TOP_COLOR = new Color(0.255f, 0.3725f, 0.753f, 1);
//	private static final Logger LOGGER = new Logger(Challenges.class);
	private final User user;
	private final int commonTextSize = (androidInstance ? 60 : 20);

	public Challenges(SpriteBatch batch, TextureManager textureManager, ScreenManager screenManager) {
		super(batch, textureManager, screenManager);
		this.user = ColorSwitch.getInstance().getUser();

		Button back = this.addDefaultBackButton();
		this.removeCurrentBackButton();
		GUIComponent top = new GUIComponent(this.getTexture("top"), this)
				.applyScale(ColorSwitch.getPlatformScale())
				.flipYCoordinate();
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			top.shiftPosition(0, -30);
		}
		top.setColor(TOP_COLOR);
		new GUIComponent(this.getTexture("dailyChallenges"), this)
				.applyScale(ColorSwitch.getPlatformScale())
				.shiftPosition(back.getX() + back.getWidth() + (androidInstance ? 30 : 15),
							   back.getY() - (androidInstance ? 10 : 5));
		this.addDefaultBackButton();

		int stars = this.user.getStars();
		String starsText = stars + "";
		if (stars >= 1000) {
			stars /= 1000;
			starsText = stars + "k";
		}
		StringComponent starsAmount = new StringComponent(starsText, this)
				.setSize(androidInstance ? 90 : 45);
		starsAmount.setColor(Color.YELLOW);
		int windowWidth = ColorSwitch.getInstance().getConfig().window.width;
		starsAmount.setPosition(new Vector2(windowWidth - starsAmount.getWidth() - 30,
				back.getY() + (back.getHeight() + starsAmount.getHeight()) / 2));

		Vector2 starScale = ColorSwitch.getPlatformScale();
		GUIComponent star = new GUIComponent(this.getTexture("star"), this).applyScale(starScale);
		star.setPosition(starsAmount.getX() - star.getWidth(),
				back.getY() + (back.getHeight() - star.getHeight()) / 2);

		int size = (androidInstance ? 70 : 30);
		StringComponent reminder = new StringComponent("Come back every day for", this).setSize(size);
		reminder.setPosition(new Vector2(WindowUtils.getCenterX(reminder.getWidth()),
				top.getY() - reminder.getHeight() + (androidInstance ? 30 : 15)));
		StringComponent reminder2 = new StringComponent("new challenges!", this).setSize(size);
		reminder2.setPosition(new Vector2(WindowUtils.getCenterX(reminder2.getWidth()),
				reminder.getY() - reminder2.getHeight() - (androidInstance ? 20 : 10)));

		JsonValue challenges = this.user.getChallenges();
		GUIComponent line = null;
		StringComponent challengeName = null;
		GUIComponent bannerHolder = null;
		String challengeText = null;
		for (int i = 0; i < 4; i++) {
			line = this.newDashedLine(bannerHolder);
			if (i == 0) {
				line.shiftPosition(0, (reminder2.getY() - reminder2.getHeight() - line.getHeight()
						- (androidInstance ? 40 : 20)));
				challengeText = "CHALLENGE LEVEL";
			} else if (i == 1) {
				challengeText = "DAILY DOUBLE";
			} else if (i == 2) {
				challengeText = "FINISH X LEVELS";
			} else if (i == 3) {
				challengeText = "SCORE X POINTS";
			}
			challengeName = this.newChallengeName(challengeText, line);
			bannerHolder = this.newBannerHolder(challengeName);
			JsonValue challenge = challenges.get(i);

			int completed = challenge.get("completed").asInt();
			int required = challenge.get("required").asInt();
			StringComponent progress = new StringComponent(completed + "/" + required, this)
					.setSize(this.commonTextSize);
			progress.setPosition(new Vector2(windowWidth - progress.getWidth() - EDGE_OFFSET,
					challengeName.getY()));
			if(challengeText.contains("X")) {
				challengeText = challengeText.replace("X", String.valueOf(required));
				challengeName.setText(challengeText);
			}

			star = new GUIComponent(this.getTexture("star"), this).applyScale(ColorSwitch.getPlatformScale());
			star.shiftPosition(windowWidth - star.getWidth() - EDGE_OFFSET,
					bannerHolder.getY() + (bannerHolder.getHeight() - star.getHeight()) / 2 - 1);

			int reward = challenge.get("reward").asInt();
			StringComponent rewardText = new StringComponent(String.valueOf(reward), this)
					.setSize(androidInstance ? 100 : 40);
			rewardText.setPosition(new Vector2(windowWidth - rewardText.getWidth() - star.getWidth() - EDGE_OFFSET * 1.2f,
					star.getY() + (rewardText.getHeight() + star.getHeight()) / 2));
		}

		// TODO : implement the challenge progress and completion system
	}

	private GUIComponent newDashedLine(GUIComponent previousBannerHolder) {
		GUIComponent line = new GUIComponent(this.getTexture("line_dashed"), this)
				.applyScale(ColorSwitch.getPlatformScale());
		float centerX = WindowUtils.getCenterX(line.getWidth());
		if(previousBannerHolder != null) {
			line.shiftPosition(centerX, previousBannerHolder.getY() - line.getHeight() - EDGE_OFFSET / 2);
		}else {
			line.shiftPosition(centerX, 0);
		}
		return line;
	}

	private StringComponent newChallengeName(String text, GUIComponent previousLine) {
		StringComponent challengeName = new StringComponent(text, this)
				.setSize(this.commonTextSize);
		if (previousLine != null) {
			challengeName.setPosition(new Vector2(EDGE_OFFSET, previousLine.getY() - previousLine.getHeight() - EDGE_OFFSET / 2));
		}
		return challengeName;
	}

	private GUIComponent newBannerHolder(StringComponent previousName) {
		GUIComponent bannerHolder = new GUIComponent(this.getTexture("bannerHolder"), this)
				.applyScale(ColorSwitch.getPlatformScale());
		if (previousName != null) {
			bannerHolder.shiftPosition(0,
					previousName.getY() - previousName.getHeight() - bannerHolder.getHeight() - EDGE_OFFSET / 2);
		}
		return bannerHolder;
	}
}
