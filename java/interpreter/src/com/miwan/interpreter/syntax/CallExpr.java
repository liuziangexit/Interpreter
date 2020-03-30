package com.miwan.interpreter.syntax;

import com.miwan.interpreter.CollectionCombinator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CallExpr extends Node {
	public final IdExpr func;
	public final ArrayList<Node> args;

	public CallExpr(String func, Collection<Node> args) {
		this.func = new IdExpr(func);
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
		StringBuilder sb = new StringBuilder();
		sb.append(this.func).append('(');
		for (int i = 0; i < this.args.size(); i++) {
			sb.append(this.args.get(i).toString());
			if (i != this.args.size() - 1)
				sb.append(", ");
		}
		sb.append(')');
		return sb.toString();
	}
}
