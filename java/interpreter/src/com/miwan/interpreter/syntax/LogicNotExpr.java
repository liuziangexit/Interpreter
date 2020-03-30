package com.miwan.interpreter.syntax;

import java.util.Collection;

public class LogicNotExpr extends Node {
	public LogicNotExpr(Node inner) {
		this.inner = inner;
	}

	public final Node inner;

	@Override
	public Collection<Node> children() {
		return null;
	}

	@Override
	public String toString() {
		return "(!" + this.inner + ")";
	}
}
