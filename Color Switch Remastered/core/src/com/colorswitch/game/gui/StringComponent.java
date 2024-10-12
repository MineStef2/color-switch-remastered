package com.colorswitch.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.colorswitch.game.DrawCall;
import com.colorswitch.game.screens.Screen;

public class StringComponent extends Sprite implements DrawCall {
	public static final FileHandle DEFAULT_FONT_FILE = Gdx.files.internal("II Vorkurs Bold.ttf");
	public static final FileHandle MEDIUM_FONT_FILE = Gdx.files.internal("II Vorkurs Medium.ttf");
	private String text;
	private GlyphLayout layout;
	private BitmapFont font;
	private FileHandle fontFile;
	private final Screen owner;
	private FreeTypeFontParameter parameter;

	public StringComponent(String text, FileHandle fontFile, Screen owner, Vector2 position) {
		this.text = text;
		this.fontFile = fontFile;
		this.owner = owner;
		this.parameter = new FreeTypeFontParameter();
		this.reload();
		this.setPosition(position.x, position.y);
		this.owner.addDrawCall(this);
	}

	public StringComponent(String text, Screen owner, Vector2 position) {
		this(text, DEFAULT_FONT_FILE, owner, position);
	}

	public StringComponent(String text, Screen owner) {
		this(text, DEFAULT_FONT_FILE, owner, new Vector2(0, 0));
	}

	public void reload() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(this.fontFile);
		this.font = generator.generateFont(this.parameter);
		this.layout = new GlyphLayout(this.font, this.text);
	}

	@Override
	public void draw(float deltaTime) {
		this.font.draw(this.owner.getSpriteBatch(), this.layout, this.getX(), this.getY());
	}

	public StringComponent setParameter(FreeTypeFontParameter parameter) {
		this.parameter = parameter;
		this.reload();
		return this;
	}

	public StringComponent setText(String text) {
		this.text = text;
		this.reload();
		return this;
	}

	public StringComponent setPosition(Vector2 position) {
		super.setPosition(position.x, position.y);
		return this;
	}

	public StringComponent setSize(int size) {
		this.parameter.size = size;
		this.reload();
		return this;
	}

	public StringComponent setFont(FileHandle fontFile) {
		this.fontFile = fontFile;
		this.reload();
		return this;
	}

	@Override
	public void setColor(Color color) {
		this.parameter.color = color;
		this.reload();
	}

	public void dispose() {
		this.font.dispose();
	}

	@Override
	public float getWidth() {
		return this.layout.width;
	}

	@Override
	public float getHeight() {
		return this.layout.height;
	}

	public GlyphLayout getLayout() {
		return layout;
	}

	public String getText() {
		return text;
	}
}
