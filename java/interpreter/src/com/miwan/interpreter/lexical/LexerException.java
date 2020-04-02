package com.miwan.interpreter.lexical;

import com.miwan.interpreter.InterpreterException;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 4/2/2020
 * <p>
 * Lexer异常基类
 */

public class LexerException extends InterpreterException {
	public LexerException() {
	}

	public LexerException(String message, String rawContent) {
		super("An exception is thrown while scanning tokens: " + message, rawContent);
	}
}
