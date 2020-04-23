package com.miwan.interpreter.runtime;

import com.miwan.interpreter.syntax.ast.*;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 执行AST的虚拟机
 */

public class VirtualMachine {

	static public Object eval(Expression node) {
		return eval(node, null);
	}

	static public Object eval(Expression node, Environment env) {
		if (node == null)
			return null;
		return node.execute(env);
	}
}
