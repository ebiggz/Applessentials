package com.gmail.erikbigler.applessentials;


public class WarnEntry {

	private String mod;
	private String message;
	private String timeStamp;

	public WarnEntry(String mod, String message, String timeStamp) {
		this.mod = mod;
		this.message = message;
		this.timeStamp = timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public String getMod() {
		return mod;
	}

	public String getTimeStamp() {
		return timeStamp;
	}
}
