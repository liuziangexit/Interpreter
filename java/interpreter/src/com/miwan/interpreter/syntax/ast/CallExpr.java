package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.Environment;
import com.miwan.interpreter.runtime.FunctionCall;
import com.miwan.interpreter.util.CollectionCombinator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 表示一个函数调用
 */

public class CallExpr extends Expression {
	public final IdExpr func;
	public final List<Expression> args;

	public CallExpr(String func, List<Expression> args) {
		this.func = new IdExpr(func);
		this.args = args;
		this.func.parent = this;
		args.forEach(a -> a.parent = this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Node> children() {
		return CollectionCombinator.createFrom(Collections.singletonList(func), (Collection<Node>) (Object) args);
	}

	@Override
	public Object execute(Environment env) {
		Object[] args = new Object[this.args.size()];
		for (int i = 0; i < this.args.size(); i++) {
			args[i] = ((Expression) this.args.get(i)).execute(env);
		}
		return FunctionCall.makeCall(this.func.id, args, false);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.func).append('(');
		for (int i = 0; i < this.args.size(); i++) {
			sb.append(this.args.get(i).toString());
			if (i != this.args.size() - 1)
				sb.append(", ");
		}
		sb.append(')');
		return sb.toString();
	}
}
