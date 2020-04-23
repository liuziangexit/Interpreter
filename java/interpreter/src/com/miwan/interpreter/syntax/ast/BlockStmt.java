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
	List<Node> subStmts;

	@Override
	public Object execute(Environment env) {
		return null;
	}

	@Override
	public Collection<Node> children() {
		return this.subStmts;
	}

}
