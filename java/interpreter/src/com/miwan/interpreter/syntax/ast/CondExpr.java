package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;
import com.miwan.interpreter.runtime.TypeMismatchException;
import com.miwan.interpreter.runtime.TypeSystem;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 表示一个条件运算符表达式
 */

public class CondExpr extends Node {

	public Node cond, yes, no;

	public CondExpr(Node cond, Node yes, Node no) {
		this.cond = cond;
		this.yes = yes;
		this.no = no;
		cond.parent = this;
		yes.parent = this;
		no.parent = this;
	}

	@Override
	public Collection<Node> children() {
		return Arrays.asList(cond, yes, no);
	}

	@Override
	public Object eval(Environment env) {
		Object cond = this.cond.eval(env);
		Boolean condAsBool = TypeSystem.builtinConvert(cond, Boolean.class);
		if (condAsBool == null) {
			throw new TypeMismatchException(cond + " can not be converted to boolean");
		}
		if (condAsBool) {
			return this.yes.eval(env);
		} else {
			return this.no.eval(env);
		}
	}

	@Override
	public String toString() {
		return cond + "?" + yes + no;
	}
}
