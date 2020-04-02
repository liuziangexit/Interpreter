package com.miwan.interpreter.lexical;

import java.util.List;

//词法阶段的单元测试
public class Test {

	public static void main(String[] args) {
		//Number
		List<Lexeme> scan = Scanner.scan("123");
		if (scan.get(0).kind != TokenKind.Number)
			throw new RuntimeException();
		scan = Scanner.scan("123.1");
		if (scan.get(0).kind != TokenKind.Number)
			throw new RuntimeException();
		scan = Scanner.scan("1.1");
		if (scan.get(0).kind != TokenKind.Number)
			throw new RuntimeException();
		scan = Scanner.scan("1");
		if (scan.get(0).kind != TokenKind.Number)
			throw new RuntimeException();
		boolean fucked = false;
		try {
			scan = Scanner.scan("1.");
			fucked = true;
		} catch (Exception e) {
		}
		if (fucked)
			throw new RuntimeException();
		try {
			scan = Scanner.scan("12.");
			fucked = true;
		} catch (Exception e) {
		}
		if (fucked)
			throw new RuntimeException();

		try {
			scan = Scanner.scan("1.1.1");
			fucked = true;
		} catch (Exception e) {
		}
		if (fucked)
			throw new RuntimeException();

		scan = Scanner.scan("12.2 123 1.1");
		if (scan.get(0).kind != TokenKind.Number
				|| scan.get(1).kind != TokenKind.Number
				|| scan.get(2).kind != TokenKind.Number)
			throw new RuntimeException();

		//true false
		scan = Scanner.scan("true false");
		if (scan.get(0).kind != TokenKind.True
				|| scan.get(1).kind != TokenKind.False)
			throw new RuntimeException();
		scan = Scanner.scan("true true");
		if (scan.get(0).kind != TokenKind.True
				|| scan.get(1).kind != TokenKind.True)
			throw new RuntimeException();
		scan = Scanner.scan("true");
		if (scan.get(0).kind != TokenKind.True)
			throw new RuntimeException();

		//id
		scan = Scanner.scan("truefalse a b _value");
		if (scan.get(0).kind != TokenKind.Identifier
				|| scan.get(1).kind != TokenKind.Identifier
				|| scan.get(2).kind != TokenKind.Identifier
				|| scan.get(3).kind != TokenKind.Identifier)
			throw new RuntimeException();
		scan = Scanner.scan("_value");
		if (scan.get(0).kind != TokenKind.Identifier)
			throw new RuntimeException();
		scan = Scanner.scan("www_wsss xxx");
		if (scan.get(0).kind != TokenKind.Identifier
				|| scan.get(1).kind != TokenKind.Identifier)
			throw new RuntimeException();
		scan = Scanner.scan("www");
		if (scan.get(0).kind != TokenKind.Identifier)
			throw new RuntimeException();

		//boolean
		scan = Scanner.scan("true&&true&&");
		if (scan.get(0).kind != TokenKind.True
				|| scan.get(1).kind != TokenKind.AndAnd
				|| scan.get(2).kind != TokenKind.True
				|| scan.get(3).kind != TokenKind.AndAnd)
			throw new RuntimeException();
		scan = Scanner.scan("func()&&func2()");
		if (scan.get(0).kind != TokenKind.Identifier
				|| scan.get(1).kind != TokenKind.LParen
				|| scan.get(2).kind != TokenKind.RParen
				|| scan.get(3).kind != TokenKind.AndAnd
				|| scan.get(4).kind != TokenKind.Identifier
				|| scan.get(5).kind != TokenKind.LParen
				|| scan.get(6).kind != TokenKind.RParen)
			throw new RuntimeException();

		System.out.println("Great Success!!!");
	}

}
