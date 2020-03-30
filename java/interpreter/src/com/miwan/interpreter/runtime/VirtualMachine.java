package com.miwan.interpreter.runtime;

import com.miwan.interpreter.Interpreter;
import com.miwan.interpreter.syntax.*;

public class VirtualMachine {

	static public Object eval(Node node) {
		return eval(node, null);
	}

	static public Object eval(Node node, Interpreter.VariableSource source) {
		if (node instanceof BinaryExpr) {
			return evalBinaryExpr((BinaryExpr) node, source);
		} else if (node instanceof CallExpr) {
			return evalCallExpr((CallExpr) node, source);
		} else if (node instanceof IdExpr) {
			return evalIdExpr((IdExpr) node, source);
		} else if (node instanceof NumberExpr) {
			return evalNumberExpr((NumberExpr) node, source);
		} else if (node instanceof ParenExpr) {
			return evalParenExpr((ParenExpr) node, source);
		} else if (node instanceof BooleanLiteralExpr) {
			return evalBooleanLiteralExpr((BooleanLiteralExpr) node, source);
		} else if (node instanceof LogicNotExpr) {
			return evalNotExpr((LogicNotExpr) node, source);
		}
		throw new IllegalArgumentException();
	}

	static private Object evalNotExpr(LogicNotExpr node, Interpreter.VariableSource source) {
		OperatorDefinition.OperatorInfo impl = OperatorDefinition.operators.get("!");
		return impl.calculation.calculate(new Object[]{eval(node.inner, source)});
	}

	static private Object evalBinaryExpr(BinaryExpr node, Interpreter.VariableSource source) {
		OperatorDefinition.OperatorInfo impl = OperatorDefinition.operators.get(node.op);
		return impl.calculation.calculate(new Object[]{eval(node.lhs, source), eval(node.rhs, source)});
	}

	static private Object evalCallExpr(CallExpr node, Interpreter.VariableSource source) {
		OperatorDefinition.FunctionInfo impl = OperatorDefinition.functions.get(node.func.id);
		Object[] args = new Object[node.args.size()];
		for (int i = 0; i < node.args.size(); i++) {
			args[i] = eval(node.args.get(i), source);
		}
		return impl.calculation.calculate(args);
	}

	static private Object evalIdExpr(IdExpr node, Interpreter.VariableSource source) {
		return source.get(node.id);
	}

	static private Number evalNumberExpr(NumberExpr node, Interpreter.VariableSource source) {
		return node.value;
	}

	static private Object evalParenExpr(ParenExpr node, Interpreter.VariableSource source) {
		if (node.inner == null)
			return null;
		return eval(node.inner, source);
	}

	static private boolean evalBooleanLiteralExpr(BooleanLiteralExpr node, Interpreter.VariableSource source) {
		return node.value;
	}
}
