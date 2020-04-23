package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;

import java.util.Collection;
import java.util.Collections;

public class ReturnStatement extends Statement {

	public final Expression expr;

	public ReturnStatement(Expression expr) {
		this.expr = expr;
	}

	@Override
	public Object execute(Environment env) {
		throw new RuntimeException();
	}

	@Override
	public Collection<Node> children() {
		return Collections.singletonList(this.expr);
	}
}
