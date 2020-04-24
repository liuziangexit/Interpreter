package com.miwan.interpreter.runtime;

import java.util.HashMap;
import java.util.Map;

public class UserDefined {
	static public void installFunction(String name, FunctionDefinition func) {
		name2func.putIfAbsent(name, func);
	}

	static public FunctionDefinition find(String name) {
		return name2func.get(name);
	}

	static private final Map<String, FunctionDefinition> name2func = new HashMap<>();
}
