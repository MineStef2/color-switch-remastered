package com.colorswitch.game.gui.button;

import com.badlogic.gdx.graphics.Color;
import com.colorswitch.game.sound.AudioOutput;

public class ButtonBehavior {
	private ButtonClickBehavior clickBehavior;
	private ButtonHoverBehavior hoverBehavior;
	private ButtonHoverOutBehavior hoverOutBehavior;
	private AudioOutput clickSound;
	private final Button targetButton;

	public ButtonBehavior(Button targetButton) {
		this.targetButton = targetButton;
		this.clickBehavior = null;
		this.hoverBehavior = (button, xpos, ypos) -> this.targetButton.setColor(targetButton.isHoverColorOverridden()
				? targetButton.getOverridenHoverColor()
				: Button.HOVER_COLOR);
		this.hoverOutBehavior = (button, xpos, ypos) -> this.targetButton.setColor(Color.WHITE);
		this.clickSound = this.targetButton.getOwner().getSoundManager().getAudioOutput("click");
	}

	public void setClickBehavior(ButtonClickBehavior clickBehavior) {
		this.clickBehavior = clickBehavior;
	}

	public void setHoverBehavior(ButtonHoverBehavior hoverBehavior) {
		this.hoverBehavior = hoverBehavior;
	}

	public void setHoverOutBehavior(ButtonHoverOutBehavior hoverOutBehavior) {
		this.hoverOutBehavior = hoverOutBehavior;
	}

	public void setClickSound(AudioOutput clickSound) {
		this.clickSound = clickSound;
	}

	public ButtonClickBehavior getClickBehavior() {
		return clickBehavior;
	}

	public ButtonHoverBehavior getHoverBehavior() {
		return hoverBehavior;
	}

	public ButtonHoverOutBehavior getHoverOutBehavior() {
		return hoverOutBehavior;
	}

	public AudioOutput getClickSound() {
		return clickSound;
	}
}
