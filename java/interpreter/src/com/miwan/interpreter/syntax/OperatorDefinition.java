package com.miwan.interpreter.syntax;

import java.util.TreeMap;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 5/29/2019
 * <p>
 * 此类包含运算符和Built-in函数实现
 */

public class OperatorDefinition {

	public static final TreeMap<String, OperatorInfo> operators;

	static {
		operators = new TreeMap<>();

		//算术运算符↓
		operators.put("^", new OperatorInfo((byte) 1, (byte) 2, a ->//
				Math.pow(((Number) a[0]).doubleValue(), ((Number) a[1]).doubleValue())));
		operators.put("*", new OperatorInfo((byte) 2, (byte) 2, a -> {
			if (a[0] instanceof Double || a[1] instanceof Double)
				return ((Number) a[0]).doubleValue() * ((Number) a[1]).doubleValue();
			return (Integer) a[0] * (Integer) a[1];
		}));
		operators.put("/", new OperatorInfo((byte) 2, (byte) 2, a -> {
			if (a[0] instanceof Double || a[1] instanceof Double)
				return ((Number) a[0]).doubleValue() / ((Number) a[1]).doubleValue();
			return (Integer) a[0] / (Integer) a[1];
		}));
		operators.put("%", new OperatorInfo((byte) 2, (byte) 2, a -> {
			if (a[0] instanceof Double || a[1] instanceof Double)
				return ((Number) a[0]).doubleValue() % ((Number) a[1]).doubleValue();
			return (Integer) a[0] % (Integer) a[1];
		}));
		operators.put("+", new OperatorInfo((byte) 3, (byte) 2, a -> {
			if (a[0] instanceof Double || a[1] instanceof Double)
				return ((Number) a[0]).doubleValue() + ((Number) a[1]).doubleValue();
			return (Integer) a[0] + (Integer) a[1];
		}));
		operators.put("-", new OperatorInfo((byte) 3, (byte) 2, a -> {
			if (a[0] instanceof Double || a[1] instanceof Double)
				return ((Number) a[0]).doubleValue() - ((Number) a[1]).doubleValue();
			return (Integer) a[0] - (Integer) a[1];
		}));

		//布尔运算符↓
		operators.put("!", new OperatorInfo((byte) 0, (byte) 1, a -> {
			if (!(a[0] instanceof Boolean)) {
				throw new RuntimeException();
			}
			return !(Boolean) a[0];
		}));
		operators.put(">", new OperatorInfo((byte) 4, (byte) 2, a -> {
			if (a[0] instanceof Double || a[1] instanceof Double) {
				return ((Number) a[0]).doubleValue() > ((Number) a[1]).doubleValue();
			} else {
				return ((Number) a[0]).intValue() > ((Number) a[1]).intValue();
			}
		}));
		operators.put("<", new OperatorInfo((byte) 4, (byte) 2, a -> {
			if (a[0] instanceof Double || a[1] instanceof Double) {
				return ((Number) a[0]).doubleValue() < ((Number) a[1]).doubleValue();
			} else {
				return ((Number) a[0]).intValue() < ((Number) a[1]).intValue();
			}
		}));
		operators.put(">=", new OperatorInfo((byte) 4, (byte) 2, a -> {
			if (a[0] instanceof Double || a[1] instanceof Double) {
				return ((Number) a[0]).doubleValue() >= ((Number) a[1]).doubleValue();
			} else {
				return ((Number) a[0]).intValue() >= ((Number) a[1]).intValue();
			}
		}));
		operators.put("<=", new OperatorInfo((byte) 4, (byte) 2, a -> {
			if (a[0] instanceof Double || a[1] instanceof Double) {
				return ((Number) a[0]).doubleValue() <= ((Number) a[1]).doubleValue();
			} else {
				return ((Number) a[0]).intValue() <= ((Number) a[1]).intValue();
			}
		}));
		operators.put("==", new OperatorInfo((byte) 4, (byte) 2, a -> {
			if (a[0] instanceof Double || a[1] instanceof Double) {
				return ((Number) a[0]).doubleValue() == ((Number) a[1]).doubleValue();
			} else {
				return ((Number) a[0]).intValue() == ((Number) a[1]).intValue();
			}
		}));
		operators.put("!=", new OperatorInfo((byte) 4, (byte) 2, a -> {
			if (a[0] instanceof Double || a[1] instanceof Double) {
				return ((Number) a[0]).doubleValue() != ((Number) a[1]).doubleValue();
			} else {
				return ((Number) a[0]).intValue() != ((Number) a[1]).intValue();
			}
		}));
		operators.put("&&", new OperatorInfo((byte) 6, (byte) 2, a -> {
			if (!(a[0] instanceof Boolean) || !(a[1] instanceof Boolean)) {
				throw new RuntimeException();
			}
			return (Boolean) a[0] && (Boolean) a[1];
		}));
		operators.put("||", new OperatorInfo((byte) 7, (byte) 2, a -> {
			if (!(a[0] instanceof Boolean) || !(a[1] instanceof Boolean)) {
				throw new RuntimeException();
			}
			return (Boolean) a[0] || (Boolean) a[1];
		}));

		// 用于实现字面负号的特殊内部符号，优先级最高
		operators.put("$", new OperatorInfo(Byte.MIN_VALUE, (byte) 2, null));
	}

	public static final TreeMap<String, FunctionInfo> functions;

	static {
		functions = new TreeMap<>();
		functions.put("pow", new FunctionInfo((byte) 2, a ->//
				Math.pow(((Number) a[0]).doubleValue(), ((Number) a[1]).doubleValue())));
		functions.put("log", new FunctionInfo((byte) 1, a ->//
				Math.log(((Number) a[0]).doubleValue())));
		functions.put("abs", new FunctionInfo((byte) 1, a -> {
			if (a[0] instanceof Integer) {
				return Math.abs((Integer) a[0]);
			} else {
				return Math.abs((Double) a[0]);
			}
		}));
		functions.put("max", new FunctionInfo((byte) 2, a -> {
			if (a[0] instanceof Double || a[1] instanceof Double) {
				return Math.max(((Number) a[0]).doubleValue(), ((Number) a[1]).doubleValue());
			} else {
				return Math.max((Integer) a[0], (Integer) a[1]);
			}
		}));
		functions.put("min", new FunctionInfo((byte) 2, a -> {
			if (a[0] instanceof Double || a[1] instanceof Double) {
				return Math.min(((Number) a[0]).doubleValue(), ((Number) a[1]).doubleValue());
			} else {
				return Math.min((Integer) a[0], (Integer) a[1]);
			}
		}));
		functions.put("round", new FunctionInfo((byte) 1, a -> (int) Math.round(((Number) a[0]).doubleValue())));
		functions.put("floor", new FunctionInfo((byte) 1, a -> Math.floor(((Number) a[0]).doubleValue())));
		functions.put("random", new FunctionInfo((byte) 0, a -> Math.random()));
		functions.put("if", new FunctionInfo((byte) 3, a -> {
			if (a[0] instanceof Boolean) {
				return ((boolean) a[0]) ? a[1] : a[2];
			} else if (a[0] instanceof Integer) {
				return ((int) a[0]) != 0 ? a[1] : a[2];
			} else {
				throw new IllegalArgumentException();
			}
		}));
	}

	public static class FunctionInfo {
		FunctionInfo(byte arg0, Calculation arg1) {
			this.arg_count = arg0;
			this.calculation = arg1;
		}

		public final byte arg_count;//函数参数数量
		public final Calculation calculation;
	}

	public static final class OperatorInfo extends FunctionInfo {
		OperatorInfo(byte arg0, byte arg1, Calculation arg2) {
			super(arg1, arg2);
			this.precedence = arg0;
		}

		public final byte precedence;//优先级，0级最高，127最低
	}

	@FunctionalInterface
	public interface Calculation {
		Object calculate(Object[] arguments);
	}

}
