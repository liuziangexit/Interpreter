package com.miwan.interpreter.lexical;

public class Lexeme {
	public Lexeme(String text, TokenKind kind) {
		this.text = text;
		this.kind = kind;
	}

	@Override
	public String toString() {
		return text;
	}

	public final String text;
	public final TokenKind kind;
}
