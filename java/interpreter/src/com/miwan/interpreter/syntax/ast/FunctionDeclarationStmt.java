package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;
import com.miwan.interpreter.runtime.FunctionDefinition;
import com.miwan.interpreter.runtime.MatchCallFailedException;
import com.miwan.interpreter.runtime.UserDefined;

import java.util.*;

public class FunctionDeclarationStmt extends Statement {

	final public String name;
	final public List<IdExpr> parms;
	final public BlockStmt body;

	public FunctionDeclarationStmt(String name, List<IdExpr> parms, BlockStmt body) {
		this.name = name;
		this.parms = parms;
		this.body = body;
	}

	@Override
	public Object execute(Environment env) {
		Class<?>[] p = new Class<?>[parms.size()];
		Arrays.fill(p, Object.class);
		UserDefined.installFunction(this.name, FunctionDefinition.define(p, args -> {
			if (env.hasReturned())
				throw new RuntimeException("there is a returned value holding");
			env.enterScope();
			if (args.length != parms.size())
				throw new RuntimeException("bad argument list");
			for (int i = 0; i < parms.size(); i++) {
				env.declVar(parms.get(i).id, args[i]);
			}
			this.body.execute(env);
			env.quitScope();
			if (!env.hasReturned())
				throw new RuntimeException("function aren't returned a value");
			return env.retrieveReturned();
		}));
		return null;
	}

	@Override
	public Collection<Node> children() {
		return Collections.emptyList();
	}
}
