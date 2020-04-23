package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;

import java.util.Collection;
import java.util.Collections;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 表示一个boolean字面量
 */

public class BooleanLiteralExpr extends Expression {
	public BooleanLiteralExpr(boolean value) {
		this.value = value;
	}

	final public boolean value;

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
		return String.valueOf(this.value);
	}
}
