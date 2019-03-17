package com.infoscient.proteus;

public class ParseException extends Exception {

	public ParseException(String message) {
		super(message);
	}

	public ParseException(String expected, String found) {
		super("Could not parse, expected: " + expected + ", found: " + found);
	}
}
