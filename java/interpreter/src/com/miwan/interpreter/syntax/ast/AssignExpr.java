package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;

import java.util.Collection;

public class AssignExpr extends Expression {
	public final IdExpr lhs;
	public final Expression rhs;

	public AssignExpr(IdExpr lhs, Expression rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public Object execute(Environment env) {
		return env.setVar(lhs.id, rhs.execute(env));
	}

	@Override
	public Collection<Node> children() {
		return null;
	}
}
