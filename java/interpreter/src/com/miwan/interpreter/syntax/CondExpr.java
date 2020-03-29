package com.miwan.interpreter.syntax;

import java.util.Arrays;
import java.util.Collection;

public class CondExpr extends Node {

	public Node cond, yes, no;

	public CondExpr(Node cond, Node yes, Node no) {
		this.cond = cond;
		this.yes = yes;
		this.no = no;
		cond.parent = this;
		yes.parent = this;
		no.parent = this;
	}


	@Override
	public Collection<Node> children() {
		return Arrays.asList(cond, yes, no);
	}

	@Override
	public String toString() {
		return cond + "?" + yes + no;
	}
}
