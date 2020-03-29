package com.miwan.interpreter.syntax;

import java.util.Collection;
import java.util.Collections;

public class ParenExpr extends Node {

	public ParenExpr(Node inner) {
		this.inner = inner;
		if (inner != null)
			inner.parent = this;
	}

	public Node inner;

	@Override
	public Collection<Node> children() {
		return Collections.singletonList(this.inner);
	}

	@Override
	public String toString() {
		return "(" + this.inner + ")";
	}
}
