package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;

import java.util.Collection;
import java.util.Collections;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 4/21/2020
 * <p>
 * Statement ends with an ';'
 */

public class ExpressionStmt extends Statement {
	public final Expression expr;

	public ExpressionStmt(Expression expr) {
		this.expr = expr;
	}

	@Override
	public Object execute(Environment env) {
		return expr.execute(env);
	}

	@Override
	public Collection<Node> children() {
		return Collections.singletonList(expr);
	}
}
