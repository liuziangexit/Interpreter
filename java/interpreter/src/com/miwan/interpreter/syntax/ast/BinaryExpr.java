package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;
import com.miwan.interpreter.runtime.OperatorDefinition;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 表示一个二元运算符
 */

public class BinaryExpr extends Node {
	public String op;
	public Node lhs, rhs;

	public BinaryExpr(String op, Node lhs, Node rhs) {
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
	public Object eval(Environment env) {
		OperatorDefinition.OperatorInfo impl = OperatorDefinition.operators.get(this.op);
		return impl.calculation.calculate(new Object[]{this.lhs.eval(env), this.rhs.eval(env)});
	}

	@Override
	public String toString() {
		return "(" + lhs + op + rhs + ")";
	}
}
