package com.miwan.interpreter.runtime;

import java.util.function.Supplier;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 4/2/2020
 * <p>
 * 找不到对应的函数原型时抛出
 */

public class MatchCallFailedException extends VirtualMachineException {
	public MatchCallFailedException(String func, Class<?>[] argsType) {
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
