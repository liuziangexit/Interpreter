package com.miwan.interpreter;

public class InterpreterException extends RuntimeException {
	public InterpreterException() {
	}

	public InterpreterException(String message) {
		super(message);
	}

	public InterpreterException(String message, String rawContent) {
		super(System.lineSeparator() + message + System.lineSeparator() + "source: " + rawContent);
	}
}
