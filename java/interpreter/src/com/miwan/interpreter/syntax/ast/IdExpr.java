package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 表示一个标识符
 */

public class IdExpr extends Expression {
	public IdExpr(String id) {
		this.id = id;
	}

	public String id;

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Node> children() {
		return (List<Node>) Collections.EMPTY_LIST;
	}

	@Override
	public Object execute(Environment env) {
		return env.getVar(this.id);
	}

	@Override
	public String toString() {
		return id;
	}
}
