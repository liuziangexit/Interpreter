package com.miwan.interpreter.runtime;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/31/2020
 * <p>
 * 此类用于实现(我们语言内的)类型系统
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
	static public <R> R builtinConvert(Object val, Class<R> require) {
		if (val.getClass() == require || require == Object.class)
			return (R) val;
		if (val instanceof Boolean) {
			int wide = (Boolean) val ? 1 : 0;
			if (require == Integer.class) {
				return (R) (Integer) wide;
			} else {
				return builtinConvert(wide, require);
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

	/*
	比较两个参数列表，返回它们之间的匹配顺序
	1.参数少者优先
	2.顺序比较各个参数，参数类型小者优先(Boolean<Integer<Double)

	若返回true，则表明a应早于b匹配；若返回false，则表明a应晚于b匹配；
	 */
	static public boolean compareArgumentList(Class<?>[] a, Class<?>[] b) {
		if (a.length < b.length)
			return true;
		for (int i = 0; i < a.length; i++) {
			if (TypeSystem.typePrecedence(a[i]) < TypeSystem.typePrecedence(b[i]))
				return true;
		}
		throw new IllegalArgumentException("they are identical...");
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
