package com.miwan.interpreter;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 4/2/2020
 * <p>
 * 所有异常的基类
 */

public class InterpreterException extends RuntimeException {
	public InterpreterException() {
	}

	public InterpreterException(String message) {
		super(message);
	}

	public InterpreterException(String message, String rawContent) {
		super(System.lineSeparator() + message + System.lineSeparator() + "source: " + rawContent);
	}
}
