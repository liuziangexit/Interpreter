package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;

import java.util.Collection;
import java.util.List;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 表示一个逗号表达式
 */

public class CommaExpr extends Node {

	public CommaExpr(List<Node> subExpressions) {
		this.subExpressions = subExpressions;
		subExpressions.forEach(n -> n.parent = this);
	}

	public List<Node> subExpressions;

	@Override
	public Collection<Node> children() {
		return this.subExpressions;
	}

	@Override
	public Object eval(Environment env) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return this.subExpressions.toString();
	}
}
