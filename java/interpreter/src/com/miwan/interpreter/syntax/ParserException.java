package com.miwan.interpreter.syntax;

import com.miwan.interpreter.InterpreterException;

public abstract class ParserException extends InterpreterException {
	public ParserException() {
	}

	public ParserException(String message, String rawContent) {
		super(message, rawContent);
	}
}
