package com.miwan.interpreter.syntax;

import java.util.Collection;
import java.util.Collections;

public class NumberExpr extends Node {
	public NumberExpr(Number value) {
		this.value = value;
	}

	public Number value;

	@Override
	public Collection<Node> children() {
		return Collections.emptyList();
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
