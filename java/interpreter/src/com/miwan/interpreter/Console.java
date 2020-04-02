package com.miwan.interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {
	static public void main(String[] args) throws IOException {
		java.io.BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.print(">");
			String s = in.readLine();
			final long beginTime = System.currentTimeMillis();
			long finTime = 0;
			Object eval;
			try {
				eval = Interpreter.eval(s, null);
				finTime = System.currentTimeMillis();
			} catch (InterpreterException e) {
				e.printStackTrace();
				System.out.print(System.lineSeparator());
				continue;
			}
			System.out.println("=" + eval);
			System.out.println("takes " + (finTime - beginTime) + "ms");
		}
	}
}
