package com.miwan.interpreter.lexical;

import java.util.HashMap;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 12/17/2019
 * <p>
 * 词法单元(token)
 */

public class Token {

	//符号表
	static private HashMap<String, Lexeme> pool = new HashMap<>();

	//创建token
	static public Lexeme createLexeme(String text, TokenKind kind) {
		Lexeme find = pool.get(text);
		if (find != null)
			return find;
		Lexeme newLex = new Lexeme(text, kind);
		pool.put(text, newLex);
		return newLex;
	}

	private Token(String text, TokenKind kind) {
		this.text = text;
		this.kind = kind;
	}

	public final String text;
	public final TokenKind kind;

	@Override
	public String toString() {
		return kind + "( " + text + " )";
	}
}
