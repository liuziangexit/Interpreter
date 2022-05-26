package com.miwan.interpreter;

import com.miwan.interpreter.lexical.LexStream;
import com.miwan.interpreter.lexical.Scanner;
import com.miwan.interpreter.syntax.ast.*;
import com.miwan.interpreter.syntax.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 4/2/2020
 * <p>
 * Parser和VM的测试用例
 */

public class Test {

	public static void main(String[] args) {

		//TODO 规范一下exception，要表达这几个信息1.出错的具体描述（括号不匹配还是多了个逗号啊）2.出错的位置3.出错位置附近的代码段
		//测试三元运算符
		//普通情形
		if (0 != ((Number) Interpreter.eval("0?1:0")).intValue())
			throw new RuntimeException();
		if (1 != ((Number) Interpreter.eval("1?1:0")).intValue())
			throw new RuntimeException();
		if (1 != ((Number) Interpreter.eval("-1?1:0")).intValue())
			throw new RuntimeException();
		if (4 != ((Number) Interpreter.eval("2221?pow(2,2):0")).intValue())
			throw new RuntimeException();
		//检查右结合性
		CondExpr ss = (CondExpr) Parser.parse(new LexStream(Scanner.scan("true ? 1 : 4 ? 2 : 3"), "true ? 1 : 0 ? 2 : 3"));
		if (!(ss.cond instanceof BooleanLiteralExpr)) {
			throw new RuntimeException();
		}
		if ((Double) ss.yes.execute(null) != 1) {
			throw new RuntimeException();
		}
		if ((Double) ss.no.execute(null) != 2) {
			throw new RuntimeException();
		}
		//嵌套情形
		if (1 != ((Number) Interpreter.eval("true ? 1 : 0 ? 2 : 3")).intValue())
			throw new RuntimeException();
		if (7 != ((Number) Interpreter.eval("true ? false?1:7 : 0?2:3")).intValue())
			throw new RuntimeException();
		if (3 != ((Number) Interpreter.eval("false ? false?1:1 : 0?2:3")).intValue())
			throw new RuntimeException();
		if (999 != ((Number) Interpreter.eval("false?1:0?true:false?1:999")).intValue())
			throw new RuntimeException();
		if (9788 != ((Number) Interpreter.eval("!false?!false?!false?9788:0:0:0")).intValue())
			throw new RuntimeException();
		if (9710 != ((Number) Interpreter.eval("false?true?true?9788:0:0:9710")).intValue())
			throw new RuntimeException();
		if (1 != ((Number) Interpreter.eval("false?8:true?1:false?2:3")).intValue())
			throw new RuntimeException();
		if (2 != ((Number) Interpreter.eval("(false?8:false)?1:true?2:3")).intValue())
			throw new RuntimeException();

		for (int i = 0; i < 1; i++) {
			// 基本的表达式解析测试
			if (!String.valueOf(Interpreter.eval("-abs(5)!=(-2^2-1)-3-5?-abs(5)==(-2^2-1)-3-5:-abs(5)==(-2^2-1)-3-5?9710:9711", null))
					.equals("9710.0"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("2+2^3*2", null))
					.equals("18.0"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("3+4*2/(1-5)^2^3+(7-9)^2", null))
					.equals("7.001953125"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("3+4*2/(1-5)^2^3", null))
					.equals("3.001953125"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("3+4*2/pow(1-5,pow(2,3))", null))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("3+4*2/pow(1-5,2^3)", null))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("3+4*2/(1-5)^pow(2,3)", null))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!"6.0".equals(String.valueOf(Interpreter.eval("0.6*10", null))))
				throw new RuntimeException();
			if (!"-189.2".equals(
					String.valueOf(Interpreter.eval("-(0.6*137-(1-0)*0+7*1+100)*1", null))))
				throw new RuntimeException();
			// 测试负号
			if (!String.valueOf(Interpreter.eval("-log(5)", null)).substring(0, 7)
					.equals("-1.6094"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("-1*-log(5)", null)).substring(0, 6)
					.equals("1.6094"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("3+4*2/(1-5)^pow(2,3)", null))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			// 测试数字类型
//			BinaryExpr cc = (BinaryExpr) Parser.parse(new LexStream(Scanner.scan("1+1.2"), "1+1.2"));
//			if (!(((NumberExpr) cc.lhs).value instanceof Integer))
//				throw new RuntimeException();
//			if (!(((NumberExpr) cc.rhs).value instanceof Double))
//				throw new RuntimeException();
			// 测试变量标识符
			if (!String.valueOf(Interpreter.eval("3+four*two/(1-5)^pow(2,3)", varName -> {
				if (varName.equalsIgnoreCase("four"))
					return 4;
				if (varName.equalsIgnoreCase("two"))
					return 2;
				return 0;
			})).equals("3.0001220703125"))
				throw new RuntimeException();
			// 关于负号优先级的测试
			if (!String.valueOf(Interpreter.eval("3+4*2/-4^pow(2,3)", null))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("3+4*2/pow(-4,pow(2,3))", null))
					.equals("3.0001220703125"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("3+-2^2", null)).equals("7.0"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("-3+-2^2", null)).equals("1.0"))
				throw new RuntimeException();
			// 函数测试
			// 都是转发JavaMath库的实现，所以试一下简单的成功情形就行了，不用写那么高的覆盖率
			if (!String.valueOf(Interpreter.eval("log(5)", null)).substring(0, 6).equals("1.6094"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("abs(-9710)", null)).equals("9710.0"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("abs(9710)", null)).equals("9710.0"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("max(9710,9711)", null)).equals("9711.0"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("min(9710,9711)", null)).equals("9710.0"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("min(-9,1)", null)).equals("-9.0"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("round(10.5)", null)).equals("11.0"))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("floor(10.5)", null)).equals("10.0"))
				throw new RuntimeException();
			if (String.valueOf(Interpreter.eval("random()", null))
					.equals(String.valueOf(Interpreter.eval("random()", null))))
				throw new RuntimeException();// 若两次random结果一样则不通过，这里不通过的概率几乎没有，如果有...就再跑一遍试试

			// 测试隐式转型
			if (!String.valueOf(Interpreter.eval("round(10.5)+2.8", null))
					.equals(String.valueOf(13.8)))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("1+2.8", null))
					.equals(String.valueOf(3.8d)))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("1+2", null))
					.equals(String.valueOf(3.0)))
				throw new RuntimeException();
			//测试boolean表达式计算
			if (!String.valueOf(Interpreter.eval("true", null))
					.equals(String.valueOf(true)))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("false", null))
					.equals(String.valueOf(false)))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("false||true", null))
					.equals(String.valueOf(true)))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("false||false", null))
					.equals(String.valueOf(false)))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("true||true", null))
					.equals(String.valueOf(true)))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("true&&true"))
					.equals(String.valueOf(true)))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("true&&false"))
					.equals(String.valueOf(false)))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("false&&false"))
					.equals(String.valueOf(false)))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("!false&&!false"))
					.equals(String.valueOf(true)))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("1>2"))
					.equals(String.valueOf(false)))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("2.0>1"))
					.equals(String.valueOf(true)))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("2.0<1"))
					.equals(String.valueOf(false)))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("2.0==2.0"))
					.equals(String.valueOf(true)))
				throw new RuntimeException();
			if (!String.valueOf(Interpreter.eval("abs(2.0)==abs(-2)"))
					.equals(String.valueOf(true)))
				throw new RuntimeException();
		}

		String src = null;
		try {
			byte[] bytes = Files.readAllBytes(Paths.get("src/test.script"));
			src = new String(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Object execute = Interpreter.execute(src);
		System.out.println("Great Success!!!");
	}

}
