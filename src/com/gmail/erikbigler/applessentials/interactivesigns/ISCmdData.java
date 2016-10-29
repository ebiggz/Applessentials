package com.gmail.erikbigler.applessentials.interactivesigns;


public class ISCmdData {

	private String name;
	private String command;
	private CommandType type;

	public ISCmdData(String name, String command, CommandType type) {
		this.name = name;
		this.command = command;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getCommand() {
		return command;
	}

	public CommandType getType() {
		return type;
	}

	enum CommandType {
		ADD, REMOVE
	}

}
