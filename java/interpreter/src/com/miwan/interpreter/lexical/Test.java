package com.miwan.interpreter.lexical;

import java.util.List;

public class Test {

	public static void main(String[] args) {
		//Number
		List<Token> scan = Scanner.scan("123");
		scan = Scanner.scan("123.1");
		scan = Scanner.scan("1.1");
		scan = Scanner.scan("1");
		try {
			scan = Scanner.scan("1.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		scan = Scanner.scan("1.1");
		try {
			scan = Scanner.scan("12.");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			scan = Scanner.scan("1.1.1");
		} catch (Exception e) {
			e.printStackTrace();
		}

		scan = Scanner.scan("12.2 123 1.1");

		//true false
		scan = Scanner.scan("true false");
		scan = Scanner.scan("true true");
		scan = Scanner.scan("true");
		scan = Scanner.scan("true");

		//id
		scan = Scanner.scan("truefalse a b _value");
		scan = Scanner.scan("_value");
		scan = Scanner.scan("www_wsss xxx");
		scan = Scanner.scan("www");

		//boolean
		scan = Scanner.scan("true&&true&&");
		scan = Scanner.scan("func()&&func2()");
		scan = Scanner.scan("www");
	}

}
