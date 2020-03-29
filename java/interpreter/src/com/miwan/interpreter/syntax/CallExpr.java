package com.miwan.interpreter.syntax;

import com.miwan.interpreter.CollectionCombinator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CallExpr extends Node {
	public final IdNode func;
	public final ArrayList<Node> args;

	public CallExpr(String func, Collection<Node> args) {
		this.func = new IdNode(func);
		this.args = new ArrayList<>(args);
		this.func.parent = this;
		args.forEach(a -> a.parent = this);
	}

	@Override
	public Collection<Node> children() {
		return CollectionCombinator.createFrom(Collections.singletonList(func), args);
	}

	@Override
	public String toString() {
		return func + ":" + args.toString();
	}
}
