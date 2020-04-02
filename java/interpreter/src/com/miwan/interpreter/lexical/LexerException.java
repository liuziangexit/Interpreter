package com.miwan.interpreter.lexical;

import com.miwan.interpreter.InterpreterException;

public class LexerException extends InterpreterException {
	public LexerException() {
	}

	public LexerException(String message, String rawContent) {
		super("An exception is thrown while scanning tokens: " + message, rawContent);
	}
}
