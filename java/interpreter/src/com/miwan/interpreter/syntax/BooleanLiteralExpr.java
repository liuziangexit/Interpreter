package com.miwan.interpreter.syntax;

import java.util.Collection;
import java.util.Collections;

public class BooleanLiteralExpr extends Node {
	public BooleanLiteralExpr(boolean value) {
		this.value = value;
	}

	final public boolean value;

	@Override
	public Collection<Node> children() {
		return Collections.emptyList();
	}

	@Override
	public String toString() {
		return String.valueOf(this.value);
	}
}
