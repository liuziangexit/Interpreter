package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;

import java.util.Collection;
import java.util.List;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 4/21/2020
 * <p>
 * Statement surrounded with '{'、 '}'
 */

public class BlockStmt extends Statement {
	public final List<Statement> statements;

	public BlockStmt(List<Statement> subStmts) {
		this.statements = subStmts;
	}

	@Override
	public Object execute(Environment env) {
		env.enterScope();
		for (Statement stmt : this.statements) {
			stmt.execute(env);
			if (env.hasReturned()) {
				break;
			}
		}
		env.quitScope();
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Node> children() {
		return (Collection<Node>) (Object) this.statements;
	}

}
