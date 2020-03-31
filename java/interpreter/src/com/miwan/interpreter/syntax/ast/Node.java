package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;

import java.util.Collection;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 所有语法树节点的基类
 */

public abstract class Node {

	public Node() {
		this(null);
	}

	public Node(Node parent) {
		this.parent = parent;
	}

	abstract public Collection<Node> children();

	abstract public Object eval(Environment env);

	public Node parent;

}
