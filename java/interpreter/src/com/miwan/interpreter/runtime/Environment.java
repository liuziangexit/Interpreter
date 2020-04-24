package com.miwan.interpreter.runtime;

import com.miwan.interpreter.Interpreter;

import java.util.Optional;
import java.util.Stack;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 运行环境，用于储存运行上下文
 */

public class Environment {
	public Environment(Interpreter.VariableSource varSource) {
		//为了实现简单起见，目前语言只支持bool、int和double
		//那么外部传进来的值如果不是这些类型的话，我们就需要把它们转换成这些类型
		this.varSource = name -> {
			if (varSource == null) {
				throw new VariableSourceException("no VariableSource provided");
			}
			Object val = varSource.get(name);
			if (val == null) {
				return null;
			}
			if (val instanceof Integer || val instanceof Double || val instanceof Boolean)
				return val;
			if (val instanceof Byte)
				return ((Number) val).intValue();
			if (val instanceof Short)
				return ((Number) val).intValue();
			if (val instanceof Long) {
				if ((Long) val > (long) Integer.MAX_VALUE || (Long) val < (long) Integer.MIN_VALUE)
					throw new VariableSourceException(name + " is not a valid 32 bit integer");
				return ((Number) val).intValue();
			}
			if (val instanceof Float)
				return ((Number) val).doubleValue();
			throw new VariableSourceException(name + " is neither a number nor a boolean");
		};
		this.scope = new Stack<>();
	}

	public boolean declVar(String id, Object val) {
		if (this.varSource.get(id) != null)
			throw new RuntimeException();
		return this.scope.peek().putIfAbsent(id, val) == null;
	}

	public Object getVar(String id) {
		Object o = varSource.get(id);
		if (o != null)
			return o;
		for (int i = this.scope.size() - 1; i >= 0; i--) {
			if (this.scope.elementAt(i).containsKey(id)) {
				return this.scope.elementAt(i).get(id);
			}
		}
		return null;
	}

	public Object setVar(String id, Object val) {
		if (this.varSource.get(id) != null)
			throw new RuntimeException();
		for (int i = this.scope.size() - 1; i >= 0; i--) {
			if (this.scope.elementAt(i).containsKey(id)) {
				return this.scope.elementAt(i).put(id, val);
			}
		}
		throw new RuntimeException("no var named " + id);
	}

	public void enterScope() {
		scope.push(new TreeMap<>());
	}

	public void quitScope() {
		scope.pop();
	}

	public boolean hasReturned() {
		return returned != null;
	}

	public void returnValue(Object val) {
		if (hasReturned())
			throw new RuntimeException("another returned value is hold");
		this.returned = val;
	}

	public Object retrieveReturned() {
		if (returned == null)
			throw new RuntimeException("no returned value are waiting");
		Object tmp = returned;
		returned = null;
		return tmp;
	}

	private Object returned = null;
	private final Stack<TreeMap<String, Object>> scope;
	private final Interpreter.VariableSource varSource;
}
