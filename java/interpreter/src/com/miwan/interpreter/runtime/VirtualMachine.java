package com.miwan.interpreter.runtime;

import com.miwan.interpreter.Interpreter;
import com.miwan.interpreter.syntax.ast.*;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 虚拟机
 * <p>
 * TODO 实现图灵完备
 */

public class VirtualMachine {

	static public Object eval(Node node) {
		return eval(node, null);
	}

	static public Object eval(Node node, Environment env) {
		return node.eval(env);
	}
}
