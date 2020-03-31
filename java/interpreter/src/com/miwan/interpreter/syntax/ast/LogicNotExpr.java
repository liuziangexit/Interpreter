package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;
import com.miwan.interpreter.runtime.OperatorDefinition;

import java.util.Collection;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 表示一个逻辑非表达式
 */

public class LogicNotExpr extends Node {
	public LogicNotExpr(Node inner) {
		this.inner = inner;
	}

	public final Node inner;

	@Override
	public Collection<Node> children() {
		return null;
	}

	@Override
	public Object eval(Environment env) {
		OperatorDefinition.OperatorInfo impl = OperatorDefinition.operators.get("!");
		return impl.calculation.calculate(new Object[]{this.inner.eval(env)});
	}

	@Override
	public String toString() {
		return "(!" + this.inner + ")";
	}
}
