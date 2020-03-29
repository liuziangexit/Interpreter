package com.miwan.interpreter.syntax;

import java.util.Collection;
import java.util.Collections;

public class NumberNode extends Node {
	public NumberNode(Number value) {
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
