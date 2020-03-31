package com.miwan.interpreter.runtime;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.miwan.interpreter.runtime.TypeSystem.implicitConvert;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 此类用于实现(我们语言内的)函数和运算符的调用决议
 */

public class FunctionCall {

	/*
	比较两个参数列表，返回它们之间的决议顺序
	1.参数少者优先
	2.顺序比较各个参数，参数类型小者优先(Boolean<Integer<Double)

	若返回true，则表明a应早于b决议；若返回false，则表明a应晚于b决议；
	 */
	static public boolean compareArgumentList(Class<?>[] a, Class<?>[] b) {
		if (a.length < b.length)
			return true;
		for (int i = 0; i < a.length; i++) {
			if (TypeSystem.typePrecedence(a[i]) < TypeSystem.typePrecedence(b[i]))
				return true;
		}
		throw new IllegalArgumentException("they are identical...");
	}

	//根据函数名和参数类型做mangling
	static public String mangling(String name, Class<?>[] args) {
		StringBuilder sb = new StringBuilder(name).append('$');
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i].getName());
			if (i != args.length - 1) {
				sb.append(',');
			}
		}
		return sb.toString();
	}

	static public Object makeCall(String funcName, Object[] args, boolean isOperator) {
		//精确匹配
		FunctionDefinition def = Builtin.find(funcName, isOperator, types(args));
		if (def == null) {
			//精确匹配失败，进行决议
			List<FunctionDefinition> candidate = Builtin.find(funcName, isOperator);
			Object[] convertedArgs = new Object[args.length];
			for (FunctionDefinition test : candidate) {
				if (args.length == test.arguments.length) {
					for (int i = 0; i < args.length; i++) {
						convertedArgs[i] = implicitConvert(args[i], test.arguments[i]);
						if (convertedArgs[i] == null)
							break;
					}
					if (Arrays.stream(convertedArgs).allMatch(Objects::nonNull)) {
						//匹配到兼容的prototype
						args = convertedArgs;
						def = test;
						break;
					}
				}
			}
		}
		if (def == null) {
			throw new RuntimeException("could not match any prototype");
		}
		return def.invoke(args);
	}

	static private Class<?>[] types(Object[] args) {
		Class<?>[] types = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			types[i] = args[i].getClass();
		}
		return types;
	}

}
