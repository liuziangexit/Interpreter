package com.miwan.interpreter.lexical;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 4/2/2020
 * <p>
 * 词法器解析数字字面量出错时抛出
 */

public class InvalidNumberFormatException extends LexerException {
	public InvalidNumberFormatException(String message, String rawContent) {
		super(message, rawContent);
	}
}
