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
	static private HashMap<String, Token> pool = new HashMap<>();

	//创建token
	static public Token createToken(String text, TokenKind kind) {
		Token find = pool.get(text);
		if (find != null)
			return find;
		Token newToken = new Token(text, kind);
		pool.put(text, newToken);
		return newToken;
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
