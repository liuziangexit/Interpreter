package com.miwan.interpreter.syntax;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 4/2/2020
 * <p>
 * 检查到语法错误时抛出
 */

public class BadSyntaxException extends ParserException {
	public BadSyntaxException(String message, String rawContent) {
		super("Syntax error checked: " + message, rawContent);
	}
}
