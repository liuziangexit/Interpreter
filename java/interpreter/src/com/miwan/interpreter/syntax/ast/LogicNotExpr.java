package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;
import com.miwan.interpreter.runtime.TypeMismatchException;
import com.miwan.interpreter.runtime.TypeSystem;

import java.util.Collection;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 表示一个逻辑非表达式
 */

public class LogicNotExpr extends Node {
	public LogicNotExpr(Node inner) {
		this.inner = inner;
	}

	public final Node inner;

	@Override
	public Collection<Node> children() {
		return null;
	}

	@Override
	public Object eval(Environment env) {
		Object innerValue = this.inner.eval(env);
		Boolean o = TypeSystem.builtinConvert(innerValue, Boolean.class);
		if (o == null) {
			throw new TypeMismatchException(innerValue + " can not be convert to boolean");
		}
		return !o;
	}

	@Override
	public String toString() {
		return "(!" + this.inner + ")";
	}
}
