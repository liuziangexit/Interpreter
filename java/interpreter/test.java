package fire.interpreter;

import java.util.ArrayList;

public class test {

	static long slowPow(long a, long b) {
		for (int i = 0; i < b; i++)
			a *= a;
		return a;
	}

	static int slowPow(int a, int b) {
		for (int i = 0; i < b; i++)
			a *= a;
		return a;
	}

	public static void main(String[] args) {
		long time = System.currentTimeMillis();

		for (int i = 0; i < 1; i++) {
			// 序列化测试
			if (TypePunning.i32d(TypePunning.i32s(9710)) != 9710)
				throw new RuntimeException();
			if (TypePunning.i32d(TypePunning.i32s(-9710)) != -9710)
				throw new RuntimeException();
			if (TypePunning.i32d(TypePunning.i32s(slowPow(2, 31) - 1)) != slowPow(2, 31) - 1)
				throw new RuntimeException();
			if (TypePunning.i32d(TypePunning.i32s(-1 * (slowPow(2, 31) - 1))) != -1 * (slowPow(2, 31) - 1))
				throw new RuntimeException();
			if (TypePunning.i64d(TypePunning.i64s(77777777777L)) != 77777777777L)
				throw new RuntimeException();
			if (TypePunning.i64d(TypePunning.i64s(-77777777777L)) != -77777777777L)
				throw new RuntimeException();
			if (TypePunning.i64d(TypePunning.i64s(slowPow(2, 63) - 1)) != slowPow(2, 63) - 1)
				throw new RuntimeException();
			if (TypePunning.i64d(TypePunning.i64s(-1 * (slowPow(2, 63) - 1))) != -1 * (slowPow(2, 63) - 1))
				throw new RuntimeException();
			if (TypePunning.fp64d(TypePunning.fp64s(97.1)) != 97.1)
				throw new RuntimeException();
			if (TypePunning.fp64d(TypePunning.fp64s(97.44)) != 97.44)
				throw new RuntimeException();
			if (TypePunning.fp64d(TypePunning.fp64s(slowPow(2, 53) - 1)) != slowPow(2, 53) - 1)
				throw new RuntimeException();
			if (TypePunning.fp64d(TypePunning.fp64s(-1 * (slowPow(2, 53) - 1))) != -1 * (slowPow(2, 53) - 1))
				throw new RuntimeException();

			if (TypePunning.fp64d(TypePunning.fp64s(Double.NEGATIVE_INFINITY)) != Double.NEGATIVE_INFINITY)
				throw new RuntimeException();
			if (TypePunning.fp64d(TypePunning.fp64s(Double.POSITIVE_INFINITY)) != Double.POSITIVE_INFINITY)
				throw new RuntimeException();

			// 是否阻止了NAN
			boolean cool = false;
			try {
				TypePunning.fp64s(Double.NaN);
				// SOMETHING WENT WRONG
			} catch (RuntimeException e) {
				// OK
				cool = true;
			}
			if (!cool)
				throw new RuntimeException("NOT COOL!");

			// 基本的表达式解析测试
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("3+4*2/(1-5)^2^3+(7-9)^2")))
					.equals("7.0001220703125"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("3+4*2/(1-5)^2^3")))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("3+4*2/pow(1-5,pow(2,3))")))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("3+4*2/pow(1-5,2^3)")))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("3+4*2/(1-5)^pow(2,3)")))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!"6.0".equals(String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("0.6*10")))))
				throw new RuntimeException();
			if (!"-189.2".equals(
					String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("-(0.6*137-(1-0)*0+7*1+100)*1")))))
				throw new RuntimeException();
			// 测试负号
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("-log(5)"))).substring(0, 7)
					.equals("-1.6094"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("-1*-log(5)"))).substring(0, 6)
					.equals("1.6094"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("3+4*2/(1-5)^pow(2,3)")))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			// 测试数字类型
			ArrayList<AstNode> cc = (ArrayList<AstNode>) ShuntingYard.compile("1+1.2");
			if (cc.get(0).type != AstNode.NodeType.INTEGER_VALUE)
				throw new RuntimeException();
			if (cc.get(1).type != AstNode.NodeType.FP_VALUE)
				throw new RuntimeException();
			// 测试^符号的右结合性
			ArrayList<AstNode> a = (ArrayList<AstNode>) ShuntingYard.compile("2^2^3^4^5");
			if (TypePunning.i32d(a.get(0).value) != 2)
				throw new RuntimeException();
			if (TypePunning.i32d(a.get(1).value) != 2)
				throw new RuntimeException();
			if (TypePunning.i32d(a.get(2).value) != 3)
				throw new RuntimeException();
			if (TypePunning.i32d(a.get(3).value) != 4)
				throw new RuntimeException();
			if (TypePunning.i32d(a.get(4).value) != 5)
				throw new RuntimeException();
			for (int z = 5; z < 9; z++)
				if (a.get(z).value[0] != (byte) '^')
					throw new RuntimeException();
			// 测试变量标识符
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("3+four*two/(1-5)^pow(2,3)"), varName -> {
				if (varName.equalsIgnoreCase("four"))
					return 4;
				if (varName.equalsIgnoreCase("two"))
					return 2;
				return 0;
			})).equals("3.0001220703125"))
				throw new RuntimeException();
			// 关于负号优先级的测试
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("3+4*2/-4^pow(2,3)")))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("3+4*2/pow(-4,pow(2,3))")))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("3+-2^2"))).equals("7.0"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("-3+-2^2"))).equals("1.0"))
				throw new RuntimeException();
			// 函数测试
			// 都是转发JavaMath库的实现，所以试一下简单的成功情形就行了，不用写那么高的覆盖率
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("log(5)"))).substring(0, 6).equals("1.6094"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("abs(-9710)"))).substring(0, 6)
					.equals("9710.0"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("abs(9710)"))).equals("9710.0"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("max(9710,9711)"))).equals("9711.0"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("min(9710,9711)"))).equals("9710.0"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("min(-9,1)"))).equals("-9.0"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("round(10.5)"))).equals("11"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("floor(10.5)"))).equals("10.0"))
				throw new RuntimeException();
			if (String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("random()")))
					.equals(String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("random()")))))
				throw new RuntimeException();// 若两次random结果一样则不通过，这里不通过的概率几乎没有，如果有...就再跑一遍试试
			// 测试隐式转型
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile("round(10.5)+2.8")))
					.equals(String.valueOf(13.8)))
				throw new RuntimeException();
			if (!Interpreter.calculateConfig("abcd{1+2},{2^2}{4+8/a}", name -> {
				if (name.equals("a"))
					return 2;
				throw new RuntimeException();
			}, Interpreter.ResultToInt).equals("abcd3,48"))
				throw new RuntimeException();
		}
		time = System.currentTimeMillis() - time;
		System.out.println("正确性测试用例通过，耗时:" + String.valueOf(time) + "毫秒");

		time = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			@SuppressWarnings("unused")
			Number optimizationFence = AstEvaluator.evaluate(ShuntingYard.compile("3+4*2/(1-5)^2^3+(7-9)^2"));
			optimizationFence = AstEvaluator.evaluate(ShuntingYard.compile("3+4*2/(1-5)^2^3"));
			optimizationFence = AstEvaluator.evaluate(ShuntingYard.compile("3+4*2/-4^pow(2,3)"));
			optimizationFence = AstEvaluator.evaluate(ShuntingYard.compile("-1*-log(5)"));
			optimizationFence = AstEvaluator.evaluate(ShuntingYard.compile("3+4*2/(1-5)^pow(2,3)"));
			optimizationFence = AstEvaluator.evaluate(ShuntingYard.compile("3+4*2/pow(1-5,2^3)"));
			optimizationFence = AstEvaluator.evaluate(ShuntingYard.compile("3+4*2/pow(1-5,pow(2,3))"));
			optimizationFence = AstEvaluator.evaluate(ShuntingYard.compile("3+-2^2"));
		}

		time = System.currentTimeMillis() - time;
		System.out.println("性能测试完成，对8个表达式重复求值100万次，总耗时:" + String.valueOf(time) + "毫秒，解释每条表达式平均耗时："
				+ String.valueOf(time / 8000000) + "毫秒");
	}

}
