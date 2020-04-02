package com.miwan.interpreter.runtime;

import java.util.function.Supplier;

public class CallNotMatchedException extends VirtualMachineException {
	public CallNotMatchedException(String func, Class<?>[] argsType) {
		super(((Supplier<String>) () -> {
			StringBuilder sb = new StringBuilder("no suitable function found for ").append(func).append('(');
			for (int i = 0; i < argsType.length; i++) {
				sb.append(argsType[i].getName());
				if (i != argsType.length - 1) {
					sb.append(", ");
				}
			}
			sb.append(')');
			return sb.toString();
		}).get());
	}
}
