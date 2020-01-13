package com.miwan.interpreter.lexical;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 12/18/2019
 * <p>
 * Token属性
 */

public enum TokenFlag {
	Operator(1),
	ArithmeticOperator(2),
	BooleanOperator(4),
	Literal(8),
	NumberLiteral(16),
	BooleanLiteral(32);

	TokenFlag(int val) {
		this.val = val;
	}

	public final int val;
}
