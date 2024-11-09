package com.colorswitch.game.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.Logger;
import com.colorswitch.game.ScrollEventListener;
import com.colorswitch.game.screens.Screen;

public class ScrollPanel implements ScrollEventListener {
	private static final Logger LOGGER = new Logger(ScrollPanel.class);
	private boolean flag_noComponents = false;
	private final Screen owner;
	private List<Sprite> scrollingComponents;
	private Map<Sprite, Vector2> originalComponentPositions;
	private float scrollXMultiplier;
	private float scrollYMultiplier;
	private float topHeightLimit;
	private float bottomHeightLimit;
	private float leftWidthLimit;
	private float rightWidthLimit;

	public ScrollPanel(Screen owner, float scrollXMultplier, float scrollYMultiplier) {
		this.owner = owner;
		this.owner.getScreenManager().addScrollEventListener(this);
		this.scrollingComponents = new ArrayList<Sprite>();
		this.originalComponentPositions = new HashMap<Sprite, Vector2>();
		this.scrollXMultiplier = scrollXMultplier;
		this.scrollYMultiplier = scrollYMultiplier;
	}

	public void addAll(Sprite... components) {
		for (Sprite component: components) {
			this.addComponent(component);
		}
	}

	public void addComponent(Sprite component) {
		this.scrollingComponents.add(component);
		this.originalComponentPositions.put(component, new Vector2(component.getX(), component.getY()));
	}

	public void removeComponent(Sprite component) {
		this.scrollingComponents.remove(component);
		this.originalComponentPositions.remove(component);
	}

	public void resetPositions() {
		this.scrollingComponents.forEach((component) -> {
			Vector2 originalPosition = this.originalComponentPositions.get(component);
			component.setPosition(originalPosition.x, originalPosition.y);
		});
	}

	public void onScroll(float amountX, float amountY) {
		if (!this.owner.isActive()) {
			return;
		}

		Map<Sprite, Float> Ypositions = new HashMap<Sprite, Float>();
		this.scrollingComponents.forEach((component) -> Ypositions.put(component, component.getY()));
		List<Float> sortedYpositions = new ArrayList<Float>(Ypositions.size());
		Ypositions.forEach((component, ypos) -> sortedYpositions.add(ypos));
		sortedYpositions.sort(Comparator.naturalOrder());

		try {
			float lowestYpos = sortedYpositions.get(0);
			float highestYpos = sortedYpositions.get(sortedYpositions.size() - 1);
			float scrollYamount = 0;
			if (amountY == 1.0f && lowestYpos < this.bottomHeightLimit) {
				scrollYamount = 1.0f * this.scrollYMultiplier;
				if (lowestYpos + scrollYamount > this.bottomHeightLimit) {
					this.translateYAll(this.bottomHeightLimit - lowestYpos);
				} else {
					this.translateYAll(scrollYamount);
				}
			} else if (amountY == -1.0f && highestYpos > this.topHeightLimit) {
				scrollYamount = -1.0f * this.scrollYMultiplier;
				if (highestYpos + scrollYamount < this.topHeightLimit) {
					this.translateYAll(this.topHeightLimit - highestYpos);
				} else {
					this.translateYAll(scrollYamount);
				}
			}
		} catch (IndexOutOfBoundsException noComponentsAdded) {
			if (!this.flag_noComponents) {
				this.flag_noComponents = true;
				LOGGER.warn("No components added for scrollPanel " + this);
			}
		}

		/* TODO: implement scroll X */
	}

	private void translateYAll(float amountY) {
		this.scrollingComponents.forEach((component) -> component.translateY(amountY));
	}

	public void setTopHeightLimit(float height) {
		this.topHeightLimit = height;
	}

	public void setBottomHeightLimit(float height) {
		this.bottomHeightLimit = height;
	}

	public float getTopHeight() {
		return topHeightLimit;
	}

	public float getBottomHeight() {
		return bottomHeightLimit;
	}

	public float getRightWidthLimit() {
		return rightWidthLimit;
	}

	public void setRightWidthLimit(float rightWidthLimit) {
		this.rightWidthLimit = rightWidthLimit;
	}

	public float getLeftWidthLimit() {
		return leftWidthLimit;
	}

	public void setLeftWidthLimit(float leftWidthLimit) {
		this.leftWidthLimit = leftWidthLimit;
	}

	public Screen getOwner() {
		return owner;
	}
}
