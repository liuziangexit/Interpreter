package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;

import java.util.Collection;
import java.util.Collections;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 表示一个括号表达式
 */

public class ParenExpr extends Expression {

	public ParenExpr(Expression inner) {
		this.inner = inner;
		if (inner != null)
			inner.parent = this;
	}

	public Expression inner;

	@Override
	public Collection<Node> children() {
		return Collections.singletonList(this.inner);
	}

	@Override
	public Object execute(Environment env) {
		if (this.inner == null)
			return null;
		return this.inner.execute(env);
	}

	@Override
	public String toString() {
		return "(" + this.inner + ")";
	}
}
