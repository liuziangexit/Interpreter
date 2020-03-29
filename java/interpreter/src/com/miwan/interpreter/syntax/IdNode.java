package com.miwan.interpreter.syntax;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class IdNode extends Node {
	public IdNode(String id) {
		this.id = id;
	}

	public String id;

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Node> children() {
		return (List<Node>) Collections.EMPTY_LIST;
	}

	@Override
	public String toString() {
		return id;
	}
}
