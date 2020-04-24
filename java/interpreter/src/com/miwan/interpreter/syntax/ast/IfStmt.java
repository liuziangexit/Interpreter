package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;
import com.miwan.interpreter.runtime.TypeSystem;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class IfStmt extends Statement {
	final public Expression cond;
	final public Statement trueBranch;
	final public Statement falseBranch;

	public IfStmt(Expression cond, Statement trueBranch, Statement falseBranch) {
		this.cond = cond;
		this.trueBranch = trueBranch;
		this.falseBranch = falseBranch;
	}

	@Override
	public Object execute(Environment env) {
		if (Objects.requireNonNull(TypeSystem.builtinConvert(cond.execute(env), Boolean.class))) {
			this.trueBranch.execute(env);
		} else {
			if (this.falseBranch != null)
				this.falseBranch.execute(env);
		}
		return null;
	}

	@Override
	public Collection<Node> children() {
		return Arrays.asList(cond, trueBranch, falseBranch);
	}
}
