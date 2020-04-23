package com.miwan.interpreter.lexical;

import static com.miwan.interpreter.lexical.TokenFlag.*;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 12/17/2019
 * <p>
 * Token类别
 */

public enum TokenKind {
	Number(Literal, NumberLiteral),// Any numerical literal token
	True(Literal, BooleanLiteral),// "true"
	False(Literal, BooleanLiteral),// "false"
	Identifier,
	Assign,//=

	//boolean operator↓
	AndAnd(Operator, BooleanOperator),// "&&"
	OrOr(Operator, BooleanOperator),// "||"
	Not(Operator, BooleanOperator),// '!'
	EqualEqual(Operator, BooleanOperator),// "=="
	NotEquals(Operator, BooleanOperator),// "!="
	Greater(Operator, BooleanOperator),// '>'
	Less(Operator, BooleanOperator),// '<'
	GreaterEquals(Operator, BooleanOperator),// ">="
	LessEquals(Operator, BooleanOperator),// "<="

	//arithmetic operator↓
	Plus(Operator, ArithmeticOperator),// '+'
	Minus(Operator, ArithmeticOperator),// '-'
	Multiply(Operator, ArithmeticOperator),// '*'
	Divide(Operator, ArithmeticOperator),// '/'
	Rem(Operator, ArithmeticOperator),// '%'
	Pow(Operator, ArithmeticOperator),// '^'

	//others
	QMark,// '?'
	Colon,// ':'
	USD,// '$'
	Comma,// ','
	Sem,// ';'
	LParen, RParen,// '(' ')'
	LBracket, RBracket,//'[' ']'
	LCurly, RCurly;//'{' '}'


	TokenKind(final TokenFlag... settingFlags) {
		this.flags = combine(settingFlags);
	}

	TokenKind() {
		this.flags = 0;
	}

	//all match
	public boolean is(final TokenFlag... test) {
		final int combined = combine(test);
		return (this.flags & combined) == combined;
	}

	//any match
	public boolean isAny(final TokenFlag... test) {
		final int combined = combine(test);
		return (this.flags & combined) != 0;
	}

	//non match
	public boolean not(final TokenFlag... test) {
		final int combined = combine(test);
		return (this.flags & combined) == 0;
	}

	static private int combine(final TokenFlag[] flags) {
		int result = 0;
		for (TokenFlag f : flags)
			result |= f.val;
		return result;
	}

	final private int flags;
}
