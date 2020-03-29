package com.miwan.interpreter.runtime;

import com.miwan.interpreter.syntax.*;

import java.util.function.Function;

public class VirtualMachine {

	static public Object eval(Node node) {
		if (node instanceof BinaryExpr) {
			return evalBinaryExpr((BinaryExpr) node);
		} else if (node instanceof CallExpr) {
			return evalCallExpr((CallExpr) node);
		} else if (node instanceof IdExpr) {
			return evalIdExpr((IdExpr) node, null);
		} else if (node instanceof NumberExpr) {
			return evalNumberExpr((NumberExpr) node);
		} else if (node instanceof ParenExpr) {
			return evalParenExpr((ParenExpr) node);
		}
		throw new IllegalArgumentException();
	}

	static private Object evalBinaryExpr(BinaryExpr node) {
		OperatorDefinition.OperatorInfo impl = OperatorDefinition.operators.get(node.op.text);
		return impl.calculation.calculate(new Object[]{eval(node.lhs), eval(node.rhs)});
	}

	static private Object evalCallExpr(CallExpr node) {
		OperatorDefinition.FunctionInfo impl = OperatorDefinition.functions.get(node.func.id);
		Object[] args = new Object[node.args.size()];
		for (int i = 0; i < node.args.size(); i++) {
			args[i] = eval(node.args.get(i));
		}
		return impl.calculation.calculate(args);
	}

	static private Object evalIdExpr(IdExpr node, Function<String, Object> source) {
		return source.apply(node.id);
	}

	static private Number evalNumberExpr(NumberExpr node) {
		return node.value;
	}

	static private Object evalParenExpr(ParenExpr node) {
		if (node.inner == null)
			return null;
		return eval(node.inner);
	}
}
