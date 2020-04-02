package com.miwan.interpreter.syntax;

public class BadSyntaxException extends ParserException {

	public BadSyntaxException(String message) {
		super("Syntax error checked: " + message);
	}
}
