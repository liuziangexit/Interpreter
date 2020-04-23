package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;
import com.miwan.interpreter.runtime.FunctionCall;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 表示一个二元运算符
 */

public class BinaryExpr extends Expression {
	public String op;
	public Expression lhs, rhs;

	public BinaryExpr(String op, Expression lhs, Expression rhs) {
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
		lhs.parent = this;
		rhs.parent = this;
	}

	@Override
	public Collection<Node> children() {
		return Arrays.asList(lhs, rhs);
	}

	@Override
	public Object execute(Environment env) {
		return FunctionCall.makeCall(this.op, new Object[]{this.lhs.execute(env), this.rhs.execute(env)}, true);
	}

	@Override
	public String toString() {
		return "(" + lhs + op + rhs + ")";
	}
}
