package com.miwan.interpreter.lexical;

import java.util.HashMap;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 12/17/2019
 * <p>
 * TokenHelper
 */

public class TokenHelper {

	//符号表
	static private HashMap<String, Lexeme> pool = new HashMap<>();

	//创建Lex
	static public Lexeme createLexeme(String text, TokenKind kind) {
		Lexeme find = pool.get(text);
		if (find != null)
			return find;
		Lexeme newLex = new Lexeme(text, kind);
		pool.put(text, newLex);
		return newLex;
	}
}
