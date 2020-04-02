package com.miwan.interpreter.syntax;

import com.miwan.interpreter.InterpreterException;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 4/2/2020
 * <p>
 * Parser异常基类
 */

public abstract class ParserException extends InterpreterException {
	public ParserException() {
	}

	public ParserException(String message, String rawContent) {
		super(message, rawContent);
	}
}
