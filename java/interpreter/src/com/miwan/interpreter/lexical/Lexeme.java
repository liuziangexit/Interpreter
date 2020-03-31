package com.miwan.interpreter.lexical;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 12/18/2019
 * <p>
 * 词法单元
 */

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
