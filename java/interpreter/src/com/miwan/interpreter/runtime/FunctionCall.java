package com.miwan.interpreter.runtime;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.miwan.interpreter.runtime.TypeSystem.builtinConvert;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 此类用于实现(我们语言内的)函数和运算符的调用决议
 */

public class FunctionCall {

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

	//进行函数调用
	static public Object makeCall(String funcName, Object[] args, boolean isOperator) {
		//精确匹配
		FunctionDefinition def = Builtin.find(funcName, isOperator, types(args));
		if (def == null) {
			//精确匹配失败，进行重载决议
			List<FunctionDefinition> candidates = Builtin.find(funcName, isOperator);
			if (candidates != null) {
				def = overloadResolution(candidates, args);
			}
			if (def == null) {
				//决议失败
				throw new CallNotMatchedException(funcName, types(args));
			}
		}
		return def.invoke(args);
	}

	/*
	重载决议
	传入候选函数和指向参数列表的指针

	如果匹配成功，则返回匹配到的候选函数，并且参数列表将被修改
	如果匹配失败，则返回null
	 */
	static private FunctionDefinition overloadResolution(List<FunctionDefinition> candidates, Object[] args) {
		Object[] convertedArgs = new Object[args.length];
		for (FunctionDefinition test : candidates) {
			if (args.length == test.arguments.length) {
				for (int i = 0; i < args.length; i++) {
					convertedArgs[i] = builtinConvert(args[i], test.arguments[i]);
					if (convertedArgs[i] == null)
						break;
				}
				if (Arrays.stream(convertedArgs).allMatch(Objects::nonNull)) {
					//匹配到兼容的prototype
					System.arraycopy(convertedArgs, 0, args, 0, args.length);
					return test;
				}
			}
		}
		return null;
	}

	static private Class<?>[] types(Object[] args) {
		Class<?>[] types = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			types[i] = args[i].getClass();
		}
		return types;
	}

}
