package com.miwan.interpreter.runtime;

import com.miwan.interpreter.Interpreter;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 运行环境，用于储存运行上下文
 */

public class Environment {
	public Environment(Interpreter.VariableSource varSource) {
		this.varSource = varSource;
	}

	public Object getVar(String id) {
		return varSource.get(id);
	}

	private final Interpreter.VariableSource varSource;
}
