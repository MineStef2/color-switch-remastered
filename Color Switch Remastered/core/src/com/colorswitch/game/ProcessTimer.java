package com.colorswitch.game;

import java.time.Duration;
import java.time.Instant;

public interface ProcessTimer {

	default Instant startTimer() {
		return Instant.now();
	}

	default long getMillisElapsed(Instant startTime) {
		return Duration.between(startTime, Instant.now()).toMillis();
	}

	default String getStringMillisElapsed(Instant startTime) {
		return String.valueOf(this.getMillisElapsed(startTime) + "ms");
	}

	default String getTotalTimeElapsed(Instant startTime) {
		long totalMillis = this.getMillisElapsed(startTime);
		int secondsElapsed = (int) totalMillis / 1000;
		int millisElapsed = (int) totalMillis % 1000;
		return String.valueOf(secondsElapsed + "s, " + millisElapsed + "ms");
	}
}
