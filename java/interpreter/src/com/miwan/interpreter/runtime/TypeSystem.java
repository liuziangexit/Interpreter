package com.miwan.interpreter.runtime;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 此类用于实现(我们语言内的)函数和运算符的调用决议
 */

public class TypeSystem {

	/*
	?->?
	bool->int
	bool->double
	int->double
	int->boolean
	double->boolean
	?->object
	转换失败则返回null
	*/
	@SuppressWarnings("unchecked")
	static public <R> R implicitConvert(Object val, Class<R> require) {
		if (val.getClass() == require || require == Object.class)
			return (R) val;
		if (val instanceof Boolean) {
			int wide = (Boolean) val ? 1 : 0;
			if (require == Integer.class) {
				return (R) (Integer) wide;
			} else {
				return implicitConvert(wide, require);
			}
		} else if (val instanceof Integer) {
			if (require == Double.class) {
				return (R) (Double) ((Integer) val).doubleValue();
			} else if (require == Boolean.class) {
				return (R) (Boolean) ((Integer) val != 0);
			}
		} else if (val instanceof Double) {
			if (require == Boolean.class) {
				return (R) (Boolean) (((Double) val).intValue() != 0);
			}
		}
		return null;
	}

	static public int typePrecedence(Class<?> t) {
		if (t.equals(Boolean.class))
			return 0;
		if (t.equals(Integer.class))
			return 1;
		if (t.equals(Double.class))
			return 2;
		throw new IllegalArgumentException();
	}
}
