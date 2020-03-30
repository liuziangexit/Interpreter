package com.miwan.interpreter;

import com.miwan.interpreter.lexical.Lexeme;
import com.miwan.interpreter.lexical.Scanner;
import com.miwan.interpreter.runtime.AstEvaluator;
import com.miwan.interpreter.runtime.VirtualMachine;
import com.miwan.interpreter.syntax.AstNode;
import com.miwan.interpreter.syntax.Node;
import com.miwan.interpreter.syntax.Parser;
import com.miwan.interpreter.syntax.ShuntingYard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Test {

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

		Node parseParen = Parser.parse(Scanner.scan("(())"));
		Node parseCall = Parser.parse(Scanner.scan("func(a,func2(),c)"));
		Node parse = Parser.parse(Scanner.scan("1+(2+3)"));
		Node parse2 = Parser.parse(Scanner.scan("1*(2+3)"));
		Node parse3 = Parser.parse(Scanner.scan("1+2*3"));
		Node parse4 = Parser.parse(Scanner.scan("1+2+3"));
		//1.多个(实际上是2个以上)op优先级从大到小的时候还是会出问题2.运算符^要特殊处理成右结合性
		Node parse5 = Parser.parse(Scanner.scan("1^2*3+4"));

		//TODO 3+4*2/(1-5)^2^3+(7-9)^2
		//TODO 3+4*2/pow(1-5,pow(2,3))
		Node parse6 = Parser.parse(Scanner.scan("1-(-1)*0+1"));
		Collection<AstNode> parse62 = ShuntingYard.compile(Scanner.scan("1-(-1)*0+1"));
		Object eval = VirtualMachine.eval(parse6);
		Object eval2 = AstEvaluator.evaluate(parse62);
		System.out.println("");


		//TODO
		//1-(-1)*0+1加这个测试用例
		//测试三元运算符
		//普通情形
		/*if (0 != ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("0?1:0")))).intValue())
			throw new RuntimeException();
		if (1 != ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("1?1:0")))).intValue())
			throw new RuntimeException();
		if (1 != ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("-1?1:0")))).intValue())
			throw new RuntimeException();
		if (4 != ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("2221?pow(2,2):0")))).intValue())
			throw new RuntimeException();
		//问号运算参数中的嵌套情形
		if (0 != ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("true ? 0 : 1 ? 2 : 3")))).intValue())
			throw new RuntimeException();
		if (1 != ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("true?1:0?true:false?1:0")))).intValue())
			throw new RuntimeException();
		//冒号运算参数中的嵌套情形
		if (4 != ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("2221?pow(2,2):0")))).intValue())
			throw new RuntimeException();
		//和括号一起测一下
		if (4 != ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("2221?pow(2,2):0")))).intValue())
			throw new RuntimeException();*/


		for (int i = 0; i < 1; i++) {
			// 基本的表达式解析测试
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+4*2/(1-5)^2^3+(7-9)^2"))))
					.equals("7.0001220703125"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+4*2/(1-5)^2^3"))))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+4*2/pow(1-5,pow(2,3))"))))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+4*2/pow(1-5,2^3)"))))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+4*2/(1-5)^pow(2,3)"))))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!"6.0".equals(String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("0.6*10"))))))
				throw new RuntimeException();
			if (!"-189.2".equals(
					String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("-(0.6*137-(1-0)*0+7*1+100)*1"))))))
				throw new RuntimeException();
			// 测试负号
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("-log(5)")))).substring(0, 7)
					.equals("-1.6094"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("-1*-log(5)")))).substring(0, 6)
					.equals("1.6094"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+4*2/(1-5)^pow(2,3)"))))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			// 测试数字类型
			ArrayList<AstNode> cc = (ArrayList<AstNode>) ShuntingYard.compile(Scanner.scan("1+1.2"));
			if (cc.get(0).type != AstNode.NodeType.INTEGER_VALUE)
				throw new RuntimeException();
			if (cc.get(1).type != AstNode.NodeType.FP_VALUE)
				throw new RuntimeException();
			// 测试^符号的右结合性
			ArrayList<AstNode> a = (ArrayList<AstNode>) ShuntingYard.compile(Scanner.scan("2^2^3^4^5"));
			if ((int) a.get(0).value != 2)
				throw new RuntimeException();
			if ((int) a.get(1).value != 2)
				throw new RuntimeException();
			if ((int) a.get(2).value != 3)
				throw new RuntimeException();
			if ((int) a.get(3).value != 4)
				throw new RuntimeException();
			if ((int) a.get(4).value != 5)
				throw new RuntimeException();
			for (int z = 5; z < 9; z++)
				if (!a.get(z).value.equals("^"))
					throw new RuntimeException();
			// 测试变量标识符
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+four*two/(1-5)^pow(2,3)")), varName -> {
				if (varName.equalsIgnoreCase("four"))
					return 4;
				if (varName.equalsIgnoreCase("two"))
					return 2;
				return 0;
			})).equals("3.0001220703125"))
				throw new RuntimeException();
			// 关于负号优先级的测试
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+4*2/-4^pow(2,3)"))))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+4*2/pow(-4,pow(2,3))"))))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+-2^2")))).equals("7.0"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("-3+-2^2")))).equals("1.0"))
				throw new RuntimeException();
			// 函数测试
			// 都是转发JavaMath库的实现，所以试一下简单的成功情形就行了，不用写那么高的覆盖率
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("log(5)")))).substring(0, 6).equals("1.6094"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("abs(-9710)")))).equals("9710"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("abs(9710)")))).equals("9710"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("max(9710,9711)")))).equals("9711"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("min(9710,9711)")))).equals("9710"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("min(-9,1)")))).equals("-9"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("round(10.5)")))).equals("11"))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("floor(10.5)")))).equals("10.0"))
				throw new RuntimeException();
			if (String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("random()"))))
					.equals(String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("random()"))))))
				throw new RuntimeException();// 若两次random结果一样则不通过，这里不通过的概率几乎没有，如果有...就再跑一遍试试
			//测试if函数（看OperatorDefinition.java的注释，这是一个函数，不是面向过程语言里的分支关键字）
			if (0 != ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("if(0,1,0)")))).intValue())
				throw new RuntimeException();
			if (1 != ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("if(1,1,0)")))).intValue())
				throw new RuntimeException();
			if (1 != ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("if(-1,1,0)")))).intValue())
				throw new RuntimeException();
			if (4 != ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("if(2221,pow(2,2),0)")))).intValue())
				throw new RuntimeException();

			// 测试隐式转型
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("round(10.5)+2.8"))))
					.equals(String.valueOf(13.8)))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("1+2.8"))))
					.equals(String.valueOf(3.8d)))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("1+2"))))
					.equals(String.valueOf(3)))
				throw new RuntimeException();
			//测试boolean表达式计算
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("true"))))
					.equals(String.valueOf(true)))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("false"))))
					.equals(String.valueOf(false)))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("false||true"))))
					.equals(String.valueOf(true)))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("false||false"))))
					.equals(String.valueOf(false)))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("true||true"))))
					.equals(String.valueOf(true)))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("true&&true"))))
					.equals(String.valueOf(true)))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("true&&false"))))
					.equals(String.valueOf(false)))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("false&&false"))))
					.equals(String.valueOf(false)))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("!false&&!false"))))
					.equals(String.valueOf(true)))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("1>2"))))
					.equals(String.valueOf(false)))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("2.0>1"))))
					.equals(String.valueOf(true)))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("2.0<1"))))
					.equals(String.valueOf(false)))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("2.0==2.0"))))
					.equals(String.valueOf(true)))
				throw new RuntimeException();
			if (!String.valueOf(AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("abs(2.0)==abs(-2)"))))
					.equals(String.valueOf(true)))
				throw new RuntimeException();
		}
		time = System.currentTimeMillis() - time;
		System.out.println("正确性测试用例通过，耗时:" + String.valueOf(time) + "毫秒");

		time = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			@SuppressWarnings("unused")
			Number optimizationFence = ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+4*2/(1-5)^2^3+(7-9)^2"))));
			optimizationFence = ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+4*2/(1-5)^2^3"))));
			optimizationFence = ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+4*2/-4^pow(2,3)"))));
			optimizationFence = ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("-1*-log(5)"))));
			optimizationFence = ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+4*2/(1-5)^pow(2,3)"))));
			optimizationFence = ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+4*2/pow(1-5,2^3)"))));
			optimizationFence = ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+4*2/pow(1-5,pow(2,3))"))));
			optimizationFence = ((Number) AstEvaluator.evaluate(ShuntingYard.compile(Scanner.scan("3+-2^2"))));
		}

		time = System.currentTimeMillis() - time;
		System.out.println("性能测试完成，对8个表达式重复求值100万次，总耗时:" + String.valueOf(time) + "毫秒，解释每条表达式平均耗时："
				+ String.valueOf(time / 8000000) + "毫秒");
	}

}
