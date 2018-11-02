package com.applenick.InfinitySnap.command.exceptions;

public class UnknownSnapTypeException extends Exception {
	
	private String snapType;
	
	public UnknownSnapTypeException(String name) {
		this.snapType = name;
	}
	
	public String getSnapType() {
		return snapType;
	}
}
