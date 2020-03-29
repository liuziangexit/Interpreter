package com.miwan.interpreter.syntax;

import java.util.Collection;

public abstract class Node {

	public Node() {
		this(null);
	}

	public Node(Node parent) {
		this.parent = parent;
	}

	abstract public Collection<Node> children();

	public Node parent;

}
