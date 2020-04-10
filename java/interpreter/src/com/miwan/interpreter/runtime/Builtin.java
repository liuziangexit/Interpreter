package com.miwan.interpreter.runtime;

import java.util.*;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 5/29/2019
 * <p>
 * 此类实现语言内置的函数和运算符
 */

public class Builtin {

	static public List<FunctionDefinition> find(String name, boolean isOperator) {
		if (!isOperator)
			return name2func.get(name);
		else
			return name2op.get(name);
	}

	static public FunctionDefinition find(String name, boolean isOperator, Class<?>[] args) {
		if (!isOperator)
			return mlname2func.get(FunctionCall.mangling(name, args));
		else
			return mlname2op.get(FunctionCall.mangling(name, args));
	}

	static public Integer precedence(String lex) {
		return opPrecedence.get(lex);
	}

	static private final Map<String, FunctionDefinition> mlname2func = new HashMap<>();
	static private final Map<String, ArrayList<FunctionDefinition>> name2func = new HashMap<>();
	static private final Map<String, FunctionDefinition> mlname2op = new HashMap<>();
	static private final Map<String, ArrayList<FunctionDefinition>> name2op = new HashMap<>();
	static private final Map<String, Integer> opPrecedence = new HashMap<>();

	static private void installFunction(String name, FunctionDefinition func) {
		Builtin.mlname2func.put(FunctionCall.mangling(name, func.arguments), func);
		ArrayList<FunctionDefinition> functionDefinitions = Builtin.name2func.computeIfAbsent(name, k -> new ArrayList<>());
		if (functionDefinitions.isEmpty()) {
			functionDefinitions.add(func);
		} else {
			for (int i = 0; i < functionDefinitions.size(); i++) {
				if (TypeSystem.compareArgumentList(func.arguments, functionDefinitions.get(i).arguments)) {
					functionDefinitions.add(i, func);
					return;
				}
			}
			throw new IllegalStateException();
		}
	}

	//数字越大优先级越高
	static private void installOperator(String lex, int precedence, FunctionDefinition func) {
		Builtin.mlname2op.put(FunctionCall.mangling(lex, func.arguments), func);
		opPrecedence.put(lex, precedence);
		ArrayList<FunctionDefinition> functionDefinitions = name2op.computeIfAbsent(lex, k -> new ArrayList<>());
		if (functionDefinitions.isEmpty()) {
			functionDefinitions.add(func);
		} else {
			for (int i = 0; i < functionDefinitions.size(); i++) {
				if (TypeSystem.compareArgumentList(func.arguments, functionDefinitions.get(i).arguments)) {
					functionDefinitions.add(i, func);
					return;
				}
			}
			throw new IllegalStateException();
		}
	}

	//内置函数
	static {
		installFunction("pow",//
				FunctionDefinition.define(new Class[]{Double.class, Double.class}, //
						args -> Math.pow((Double) args[0], (Double) args[1])));
		installFunction("log",//
				FunctionDefinition.define(new Class[]{Double.class}, //
						args -> Math.log((Double) args[0])));
		installFunction("abs",//
				FunctionDefinition.define(new Class[]{Double.class}, //
						args -> Math.abs((Double) args[0])));
		installFunction("abs",//
				FunctionDefinition.define(new Class[]{Integer.class}, //
						args -> Math.abs((Integer) args[0])));
		installFunction("max",//
				FunctionDefinition.define(new Class[]{Double.class, Double.class}, //
						args -> Math.max((Double) args[0], (Double) args[1])));
		installFunction("min",//
				FunctionDefinition.define(new Class[]{Double.class, Double.class}, //
						args -> Math.min((Double) args[0], (Double) args[1])));
		installFunction("round",//
				FunctionDefinition.define(new Class[]{Double.class}, //
						args -> (double) Math.round((Double) args[0])));
		installFunction("floor",//
				FunctionDefinition.define(new Class[]{Double.class}, //
						args -> Math.floor((Double) args[0])));
		installFunction("random",//
				FunctionDefinition.define(new Class[]{}, //
						args -> Math.random()));
		installFunction("if",//
				FunctionDefinition.define(new Class[]{Boolean.class, Object.class, Object.class}, //
						args -> {
							if ((Boolean) args[0])
								return args[1];
							else
								return args[2];
						}));
	}

	//内置运算符
	static {
		//算数运算符
		installOperator("^", 5,//
				FunctionDefinition.define(new Class[]{Double.class, Double.class}, //
						args -> Math.pow((Double) args[0], (Double) args[1])));
		installOperator("*", 4,//
				FunctionDefinition.define(new Class[]{Double.class, Double.class}, //
						args -> (Double) args[0] * (Double) args[1]));
		installOperator("*", 4,//
				FunctionDefinition.define(new Class[]{Integer.class, Integer.class}, //
						args -> (Integer) args[0] * (Integer) args[1]));
		installOperator("/", 4,//
				FunctionDefinition.define(new Class[]{Double.class, Double.class}, //
						args -> (Double) args[0] / (Double) args[1]));
		installOperator("/", 4,//
				FunctionDefinition.define(new Class[]{Integer.class, Integer.class}, //
						args -> (Integer) args[0] / (Integer) args[1]));
		installOperator("%", 4,//
				FunctionDefinition.define(new Class[]{Double.class, Double.class}, //
						args -> (Double) args[0] % (Double) args[1]));
		installOperator("%", 4,//
				FunctionDefinition.define(new Class[]{Integer.class, Integer.class}, //
						args -> (Integer) args[0] % (Integer) args[1]));
		installOperator("+", 3,//
				FunctionDefinition.define(new Class[]{Double.class, Double.class}, //
						args -> (Double) args[0] + (Double) args[1]));
		installOperator("+", 3,//
				FunctionDefinition.define(new Class[]{Integer.class, Integer.class}, //
						args -> (Integer) args[0] + (Integer) args[1]));
		installOperator("-", 3,//
				FunctionDefinition.define(new Class[]{Double.class, Double.class}, //
						args -> (Double) args[0] - (Double) args[1]));
		installOperator("-", 3,//
				FunctionDefinition.define(new Class[]{Integer.class, Integer.class}, //
						args -> (Integer) args[0] - (Integer) args[1]));

		//布尔运算符
		//逻辑非运算符是特殊实现的
		installOperator(">", 2,//
				FunctionDefinition.define(new Class[]{Double.class, Double.class}, //
						args -> (Double) args[0] > (Double) args[1]));
		installOperator(">", 2,//
				FunctionDefinition.define(new Class[]{Integer.class, Integer.class}, //
						args -> (Integer) args[0] > (Integer) args[1]));
		installOperator("<", 2,//
				FunctionDefinition.define(new Class[]{Double.class, Double.class}, //
						args -> (Double) args[0] < (Double) args[1]));
		installOperator("<", 2,//
				FunctionDefinition.define(new Class[]{Integer.class, Integer.class}, //
						args -> (Integer) args[0] < (Integer) args[1]));
		installOperator(">=", 2,//
				FunctionDefinition.define(new Class[]{Double.class, Double.class}, //
						args -> (Double) args[0] >= (Double) args[1]));
		installOperator(">=", 2,//
				FunctionDefinition.define(new Class[]{Integer.class, Integer.class}, //
						args -> (Integer) args[0] >= (Integer) args[1]));
		installOperator("<=", 2,//
				FunctionDefinition.define(new Class[]{Double.class, Double.class}, //
						args -> (Double) args[0] <= (Double) args[1]));
		installOperator("<=", 2,//
				FunctionDefinition.define(new Class[]{Integer.class, Integer.class}, //
						args -> (Integer) args[0] <= (Integer) args[1]));
		installOperator("==", 2,//
				FunctionDefinition.define(new Class[]{Double.class, Double.class}, //
						args -> ((Double) args[0]).equals((Double) args[1])));
		installOperator("==", 2,//
				FunctionDefinition.define(new Class[]{Integer.class, Integer.class}, //
						args -> ((Integer) args[0]).equals((Integer) args[1])));
		installOperator("!=", 2,//
				FunctionDefinition.define(new Class[]{Double.class, Double.class}, //
						args -> !((Double) args[0]).equals((Double) args[1])));
		installOperator("!=", 2,//
				FunctionDefinition.define(new Class[]{Integer.class, Integer.class}, //
						args -> !((Integer) args[0]).equals((Integer) args[1])));
		installOperator("&&", 1,//
				FunctionDefinition.define(new Class[]{Boolean.class, Boolean.class}, //
						args -> (Boolean) args[0] && (Boolean) args[1]));
		installOperator("||", 0,//
				FunctionDefinition.define(new Class[]{Boolean.class, Boolean.class}, //
						args -> (Boolean) args[0] || (Boolean) args[1]));
	}

}
