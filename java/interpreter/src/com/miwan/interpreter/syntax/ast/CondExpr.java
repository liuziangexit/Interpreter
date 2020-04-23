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

public class CondExpr extends Expression {

	public Expression cond, yes, no;

	public CondExpr(Expression cond, Expression yes, Expression no) {
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
	public Object execute(Environment env) {
		Object cond = this.cond.execute(env);
		Boolean condAsBool = TypeSystem.builtinConvert(cond, Boolean.class);
		if (condAsBool == null) {
			throw new TypeMismatchException(cond + " can not be converted to boolean");
		}
		if (condAsBool) {
			return this.yes.execute(env);
		} else {
			return this.no.execute(env);
		}
	}

	@Override
	public String toString() {
		return cond + "?" + yes + no;
	}
}
