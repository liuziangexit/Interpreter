package com.miwan.interpreter.syntax;

import java.util.Collection;
import java.util.List;

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
	public String toString() {
		return this.subExpressions.toString();
	}
}
