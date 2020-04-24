package com.miwan.interpreter.lexical;

import com.miwan.interpreter.syntax.BadSyntaxException;
import com.miwan.interpreter.util.Pointer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 12/18/2019
 * <p>
 * Tokenize Phase
 */

public class Scanner {

	//人肉自动机
	static public List<Lexeme> scan(final String src) {
		final List<Lexeme> result = new ArrayList<>(Math.max(src.length() / 3, 5));//假定平均每3个字一个词
		final StringBuilder currentLex = new StringBuilder(30);//很少有词长度大于30
		Position pos = new Position(0, 0, 0);
		final Pointer<Position> tokenBegin = new Pointer<>(new Position(0, 0, 0));
		final Consumer<TokenKind> newToken = kind -> {
			result.add(new Lexeme(currentLex.toString(), kind, tokenBegin.v, pos));
			currentLex.setLength(0);
		};

		for (; pos.count() < src.length(); ) {
			tokenBegin.v.assign(pos);

			//skip white space
			if (src.charAt(pos.count()) == ' ' || src.charAt(pos.count()) == '\t') {
				pos.nextColumn();
				continue;
			}

			//skip end of line
			if (src.charAt(pos.count()) == '\n') {
				pos.nextLine(1);
				continue;
			}
			if (src.charAt(pos.count()) == '\r') {
				if (src.length() > pos.count() + 1 && src.charAt(pos.count() + 1) == '\n') {
					pos.nextLine(2);
					continue;
				}
				pos.nextLine(1);
				continue;
			}

			if (src.charAt(pos.count()) == '`') {
				do {
					pos.nextColumn();
				}
				while (src.length() > pos.count() && src.charAt(pos.count()) != '`');
				pos.nextColumn();
				continue;
			}

			//match Number
			if (Character.isDigit(src.charAt(pos.count()))) {
				boolean dot = false;
				currentLex.append(src.charAt(pos.count()));
				pos.nextColumn();
				while (pos.count() < src.length()) {
					if (Character.isDigit(src.charAt(pos.count()))) {
						currentLex.append(src.charAt(pos.count()));
						pos.nextColumn();
					} else if (src.charAt(pos.count()) == '.') {
						if (dot) {
							throw new InvalidNumberFormatException("invalid number at " + tokenBegin, src);
						}
						dot = true;
						currentLex.append(src.charAt(pos.count()));
						pos.nextColumn();
					} else {
						break;
					}
				}
				if (currentLex.charAt(currentLex.length() - 1) == '.') {
					throw new InvalidNumberFormatException("invalid number at " + tokenBegin, src);
				}
				newToken.accept(TokenKind.Number);
				continue;
			}

			if (Character.isLetter(src.charAt(pos.count())) || src.charAt(pos.count()) == '_') {
				//move i to the end(last char +1) of the current token
				while (pos.nextColumn() < src.length()//
						&& (Character.isLetterOrDigit(src.charAt(pos.count())) || src.charAt(pos.count()) == '_')) ;
				currentLex.append(src, tokenBegin.v.count(), pos.count());

				//match "true"
				if (currentLex.length() == 4) {
					if (match(currentLex, 't', 'r', 'u', 'e')) {
						newToken.accept(TokenKind.True);
						continue;
					}
				}
				//match "false"
				if (currentLex.length() == 5) {
					if (match(currentLex, 'f', 'a', 'l', 's', 'e')) {
						newToken.accept(TokenKind.False);
						continue;
					}
				}
				//is an Id
				newToken.accept(TokenKind.Identifier);
				continue;
			}

			//match boolean operators
			if (match(src, pos.count(), '&', '&')) {
				currentLex.append(src, pos.count(), pos.count() + 2);
				pos.nextColumn(2);
				newToken.accept(TokenKind.AndAnd);
				continue;
			}
			if (match(src, pos.count(), '|', '|')) {
				currentLex.append(src, pos.count(), pos.count() + 2);
				pos.nextColumn(2);
				newToken.accept(TokenKind.OrOr);
				continue;
			}
			if (src.charAt(pos.count()) == '!') {
				if (match(src, pos.count() + 1, '=')) {
					currentLex.append(src, pos.count(), pos.count() + 2);
					pos.nextColumn(2);
					newToken.accept(TokenKind.NotEquals);
					continue;
				}
				currentLex.append('!');
				pos.nextColumn();
				newToken.accept(TokenKind.Not);
				continue;
			}
			if (src.charAt(pos.count()) == '=') {
				//==
				if (match(src, pos.count() + 1, '=')) {
					currentLex.append(src, pos.count(), pos.count() + 2);
					pos.nextColumn(2);
					newToken.accept(TokenKind.EqualEqual);
					continue;
				}
				//=
				currentLex.append(src, pos.count(), pos.count() + 1);
				pos.nextColumn(1);
				newToken.accept(TokenKind.Assign);
				continue;
			}
			if (src.charAt(pos.count()) == '>') {
				if (match(src, pos.count() + 1, '=')) {
					currentLex.append(src, pos.count(), pos.count() + 2);
					pos.nextColumn(2);
					newToken.accept(TokenKind.GreaterEquals);
					continue;
				}
				currentLex.append('>');
				pos.nextColumn();
				newToken.accept(TokenKind.Greater);
				continue;
			}
			if (src.charAt(pos.count()) == '<') {
				if (match(src, pos.count() + 1, '=')) {
					currentLex.append(src, pos.count(), pos.count() + 2);
					pos.nextColumn(2);
					newToken.accept(TokenKind.LessEquals);
					continue;
				}
				currentLex.append('<');
				pos.nextColumn();
				newToken.accept(TokenKind.Less);
				continue;
			}

			//match arithmetic operators
			if (src.charAt(pos.count()) == '+') {
				currentLex.append('+');
				pos.nextColumn();
				newToken.accept(TokenKind.Plus);
				continue;
			}
			if (src.charAt(pos.count()) == '-') {
				currentLex.append('-');
				pos.nextColumn();
				newToken.accept(TokenKind.Minus);
				continue;
			}
			if (src.charAt(pos.count()) == '*') {
				currentLex.append('*');
				pos.nextColumn();
				newToken.accept(TokenKind.Multiply);
				continue;
			}
			if (src.charAt(pos.count()) == '/') {
				currentLex.append('/');
				pos.nextColumn();
				newToken.accept(TokenKind.Divide);
				continue;
			}
			if (src.charAt(pos.count()) == '%') {
				currentLex.append('%');
				pos.nextColumn();
				newToken.accept(TokenKind.Rem);
				continue;
			}
			if (src.charAt(pos.count()) == '^') {
				currentLex.append('^');
				pos.nextColumn();
				newToken.accept(TokenKind.Pow);
				continue;
			}

			//match others
			if (src.charAt(pos.count()) == '?') {
				currentLex.append('?');
				pos.nextColumn();
				newToken.accept(TokenKind.QMark);
				continue;
			}
			if (src.charAt(pos.count()) == ':') {
				currentLex.append(':');
				pos.nextColumn();
				newToken.accept(TokenKind.Colon);
				continue;
			}
			if (src.charAt(pos.count()) == ',') {
				currentLex.append(',');
				pos.nextColumn();
				newToken.accept(TokenKind.Comma);
				continue;
			}
			if (src.charAt(pos.count()) == ';') {
				currentLex.append(';');
				pos.nextColumn();
				newToken.accept(TokenKind.Sem);
				continue;
			}
			if (src.charAt(pos.count()) == '(') {
				currentLex.append('(');
				pos.nextColumn();
				newToken.accept(TokenKind.LParen);
				continue;
			}
			if (src.charAt(pos.count()) == ')') {
				currentLex.append(')');
				pos.nextColumn();
				newToken.accept(TokenKind.RParen);
				continue;
			}
			if (src.charAt(pos.count()) == '[') {
				currentLex.append('[');
				pos.nextColumn();
				newToken.accept(TokenKind.LBracket);
				continue;
			}
			if (src.charAt(pos.count()) == ']') {
				currentLex.append(']');
				pos.nextColumn();
				newToken.accept(TokenKind.RBracket);
				continue;
			}
			if (src.charAt(pos.count()) == '{') {
				currentLex.append('{');
				pos.nextColumn();
				newToken.accept(TokenKind.LCurly);
				continue;
			}
			if (src.charAt(pos.count()) == '}') {
				currentLex.append('}');
				pos.nextColumn();
				newToken.accept(TokenKind.RCurly);
				continue;
			}

			throw new BadSyntaxException("can not resolve token at " + tokenBegin, src);
		}
		return result;
	}

	static private boolean match(final CharSequence seq, final char... expect) {
		return match(seq, 0, expect);
	}

	static private boolean match(final CharSequence seq, final int begin, final char... expect) {
		if (begin >= seq.length())
			return false;
		if (seq.length() - begin < expect.length)
			return false;
		for (int i = 0; i < expect.length; i++)
			if (seq.charAt(i + begin) != expect[i])
				return false;
		return true;
	}

}
