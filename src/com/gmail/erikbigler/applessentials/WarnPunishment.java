package com.gmail.erikbigler.applessentials;


public class WarnPunishment {

	private int points;
	private String time;

	public WarnPunishment(int points, String time) {
		this.points = points;
		this.time = time;
	}

	public int getPoints() {
		return points;
	}

	public String getTime() {
		return time;
	}
}