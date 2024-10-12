package com.colorswitch.game;

import java.util.Calendar;

import com.badlogic.gdx.Gdx;

public class Logger {
	private final Class<?> callerClass;

	public Logger(Class<?> callerClass) {
		this.callerClass = callerClass;
	}

	public void log(Object message, LogLevel level) {
		final Calendar calendar = Calendar.getInstance();
		String tag = new StringBuilder().append(calendar.get(Calendar.HOUR_OF_DAY)).append(":")
				.append(calendar.get(Calendar.MINUTE)).append(":").append(calendar.get(Calendar.SECOND)).append(":")
				.append(calendar.get(Calendar.MILLISECOND)).append(" ").append(this.callerClass.getSimpleName())
				.toString();
		if (level == LogLevel.INFO) {
			Gdx.app.log(tag + " INFO", message.toString());
		}else if (level == LogLevel.DEBUG) {
			Gdx.app.debug(tag + " DEBUG", message.toString());
		}else if (level == LogLevel.ERROR) {
			Gdx.app.error(tag + " ERROR", message.toString());
		}else if (level == LogLevel.WARN) {
			Gdx.app.log(tag + " WARN", message.toString());
		}
	}

	public void info(Object message) {
		log(message, LogLevel.INFO);
	}

	public void debug(Object message) {
		log(message, LogLevel.DEBUG);
	}

	public void error(Object message) {
		log(message, LogLevel.ERROR);
	}

	public void warn(Object message) {
		log(message, LogLevel.WARN);
	}

	public static enum LogLevel {
		NONE(0), ERROR(1), INFO(2), DEBUG(3), WARN(4);

		private final int id;

		LogLevel(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}
}
