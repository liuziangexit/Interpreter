package com.miwan.interpreter.syntax;

public class BadSyntaxException extends ParserException {

	public BadSyntaxException(String message, String rawContent) {
		super("Syntax error checked: " + message, rawContent);
	}
}
