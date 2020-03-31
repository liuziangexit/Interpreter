package com.miwan.interpreter.syntax;

import java.util.Arrays;
import java.util.Collection;

public class BinaryExpr extends Node {
	public String op;
	public Node lhs, rhs;

	public BinaryExpr(String op, Node lhs, Node rhs) {
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
		lhs.parent = this;
		rhs.parent = this;
	}

	@Override
	public Collection<Node> children() {
		return Arrays.asList(lhs, rhs);
	}

	@Override
	public String toString() {
		return "(" + lhs + op + rhs + ")";
	}
}
