package com.miwan.interpreter.lexical;

import java.util.List;
import java.util.function.Function;

public class LexStream {
	public LexStream(List<Lexeme> content) {
		this.content = content;
	}

	public Lexeme eat() {
		if (pos >= this.content.size())
			return null;
		return content.get(pos++);
	}

	public Lexeme current() {
		if (pos >= this.content.size())
			return null;
		return content.get(pos);
	}

	public Lexeme peek(int c) {
		if (pos + c >= content.size())
			return null;
		return content.get(pos + c);
	}

	public Lexeme peek() {
		return peek(1);
	}

	public boolean hasNext() {
		return peek() != null;
	}

	public static boolean test(Lexeme lex, Function<Lexeme, Boolean> tester) {
		if (lex != null)
			return tester.apply(lex);
		return false;
	}

	@Override
	public String toString() {
		return current().text;
	}

	private int pos = 0;
	final private List<Lexeme> content;
}
