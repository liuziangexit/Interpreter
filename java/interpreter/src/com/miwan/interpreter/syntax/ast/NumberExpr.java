package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;

import java.util.Collection;
import java.util.Collections;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 表示一个数字字面量
 */

public class NumberExpr extends Expression {
	public NumberExpr(Number value) {
		this.value = value;
	}

	public Number value;

	@Override
	public Collection<Node> children() {
		return Collections.emptyList();
	}

	@Override
	public Object execute(Environment env) {
		return this.value;
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
