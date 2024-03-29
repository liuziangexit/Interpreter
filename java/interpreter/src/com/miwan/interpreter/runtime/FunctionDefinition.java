package com.miwan.interpreter.runtime;

import java.util.function.Function;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 此类用于表示一个运算符或函数的实现
 */

public abstract class FunctionDefinition {

	//定义一个函数
	static public FunctionDefinition define(Class<?>[] args, Function<Object[], Object> body) {
		return new FunctionDefinition(args) {
			@Override
			public Object invoke(Object[] args) {
				return body.apply(args);
			}
		};
	}

	private FunctionDefinition(Class<?>[] args) {
		this.arguments = args;
	}

	public abstract Object invoke(Object[] args);

	public final Class<?>[] arguments;
}
