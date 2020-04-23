package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class VariableDeclarationStmt extends Statement {
	public final List<AbstractMap.SimpleImmutableEntry<IdExpr, Expression>> initList;

	public VariableDeclarationStmt(List<AbstractMap.SimpleImmutableEntry<IdExpr, Expression>> initializeList) {
		this.initList = initializeList;
	}

	@Override
	public Object execute(Environment env) {
		for (Map.Entry<IdExpr, Expression> v : this.initList) {
			if (env.declVar(v.getKey().id, v.getValue().execute(env)))
				throw new RuntimeException("could not declare variable " + v.getKey().id + " in current scope");
		}
		return null;
	}

	@Override
	public Collection<Node> children() {
		return null;
	}
}
