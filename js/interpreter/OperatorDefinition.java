package fire.interpreter;

import java.util.TreeMap;
import java.util.function.Function;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 5/29/2019
 * @description 运算符效果定义，下列注释充当语法文档
 * @数字类型 解释器内部以强类型进行表示和计算
 * @负数的表示 支持直接负号，如“-1”这样的形式
 * @优先级 以圆括号包裹子表达式以声明更高优先级
 * @四则运算 加减乘除号，无需多言
 * @指数和对数 通过函数来计算指数:pow(base, exponent)、通过运算符来计算指数:base^exponent、自然对数:log(value)
 * @关于右结合的指数符号 指数符号^在iPhoneSpotlight中为左结合性，而在WolfrmAlpha则为右结合性...它的结合性并没有一个明确定义或业界共识
 * 在本解释器中，连续的^符号将被视为迭代幂次运算，为右结合性(与WolfrmAlpha相同)。
 * 通俗地说，若有以下一式：2^3^4^5，其运算顺序将为2^(3^(4^5))而非((2^3)^4)^5
 * 在书写表达式时请尽量使用pow函数，这可以使求值顺序更清晰
 * @绝对值 获得一个数的绝对值:abs(value)
 * @比较 接受两个参数，返回更大的那个:max(value1, value2)、接受两个参数，返回更小的那个:min(value1, value2)
 * @取整 以四舍五入的规则获得一个整数:round(value)、获得小于等于参数的最大整数:floor(value)
 * @随机 获得一个伪随机浮点数，范围是[0,1):random()
 */

class OperatorDefinition {

	static final TreeMap<Byte, OperatorInfo> arithmetic;

	static {
		arithmetic = new TreeMap<>();
		arithmetic.put((byte) '+', new OperatorInfo((byte) 0, createSpecializedCalculation(a -> a[0] + a[1], a -> a[0] + a[1])));
		arithmetic.put((byte) '-', new OperatorInfo((byte) 0, createSpecializedCalculation(a -> a[0] - a[1], a -> a[0] - a[1])));
		arithmetic.put((byte) '*', new OperatorInfo((byte) 1, createSpecializedCalculation(a -> a[0] * a[1], a -> a[0] * a[1])));
		arithmetic.put((byte) '/', new OperatorInfo((byte) 1, createSpecializedCalculation(a -> a[0] / a[1], a -> a[0] / a[1])));
		arithmetic.put((byte) '%', new OperatorInfo((byte) 1, createSpecializedCalculation(a -> a[0] % a[1], a -> a[0] % a[1])));
		arithmetic.put((byte) '^', new OperatorInfo((byte) 2, createSpecializedCalculation(a -> Math.pow(a[0], a[1]), a -> Math.pow(a[0], a[1]))));

		// 用于实现字面负号的特殊内部符号
		arithmetic.put((byte) '$', new OperatorInfo(Byte.MAX_VALUE, null));
	}

	static final TreeMap<String, OperatorInfo> functions;

	static {
		functions = new TreeMap<>();
		functions.put("pow", new OperatorInfo((byte) 2, createFpCalculation(a -> Math.pow(a[0], a[1]))));
		functions.put("log", new OperatorInfo((byte) 1, createFpCalculation(a -> Math.log(a[0]))));
		functions.put("abs", new OperatorInfo((byte) 1, createFpCalculation(a -> Math.abs(a[0]))));
		functions.put("max", new OperatorInfo((byte) 2, createFpCalculation(a -> Math.max(a[0], a[1]))));
		functions.put("min", new OperatorInfo((byte) 2, createFpCalculation(a -> Math.min(a[0], a[1]))));
		functions.put("round", new OperatorInfo((byte) 1, createFpCalculation(a -> Math.round(a[0]))));
		functions.put("floor", new OperatorInfo((byte) 1, createFpCalculation(a -> Math.floor(a[0]))));
		functions.put("random", new OperatorInfo((byte) 0, createFpCalculation(a -> Math.random())));
	}

	static final class OperatorInfo {

		OperatorInfo(byte arg0, Calculation arg1) {
			this.number = arg0;
			this.calculation = arg1;
		}

		final byte number;// 优先级(在arithmetic中时)或函数参数数量(在functions中时)
		final Calculation calculation;

	}

	@FunctionalInterface
	interface Calculation {
		Number calculate(Number[] arguments);
	}

	static Calculation createFpCalculation(Function<double[], Number> fpf) {
		return a -> fpf.apply(fp64Array(a));
	}

	static Calculation createSpecializedCalculation(Function<int[], Number> intf, Function<double[], Number> fpf) {
		return a -> {
			for (Number p : a)
				if (p instanceof Double)
					return fpf.apply(fp64Array(a));
			return intf.apply(i32Array(a));
		};
	}

	static double[] fp64Array(Number[] a) {
		double[] doubles = new double[a.length];
		for (int i = 0; i < doubles.length; i++)
			doubles[i] = a[i].doubleValue();
		return doubles;
	}

	static int[] i32Array(Number[] a) {
		int[] ints = new int[a.length];
		for (int i = 0; i < ints.length; i++)
			ints[i] = a[i].intValue();
		return ints;
	}

}
