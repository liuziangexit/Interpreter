package com.miwan.interpreter.lexical;

import java.util.ArrayList;
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

	static public List<Token> scan(final String src) {
		final List<Token> result = new ArrayList<>();
		final StringBuilder currentLex = new StringBuilder();
		final Consumer<TokenKind> newToken = kind -> {
			result.add(Token.createToken(currentLex.toString(), kind));
			currentLex.setLength(0);
		};

		for (int i = 0; i < src.length(); ) {
			final int tokenBegin = i;

			//skip white space
			if (src.charAt(i) == ' ' || src.charAt(i) == '\t') {
				i++;
				continue;
			}

			//match Number
			if (Character.isDigit(src.charAt(i))) {
				boolean dot = false;
				currentLex.append(src.charAt(i++));
				while (i < src.length()) {
					if (Character.isDigit(src.charAt(i))) {
						currentLex.append(src.charAt(i++));
					} else if (src.charAt(i) == '.') {
						if (dot) {
							throw new RuntimeException("invalid number at col " + tokenBegin);
						}
						dot = true;
						currentLex.append(src.charAt(i++));
					} else {
						break;
					}
				}
				if (currentLex.charAt(currentLex.length() - 1) == '.') {
					throw new RuntimeException("invalid number at col " + tokenBegin);
				}
				newToken.accept(TokenKind.Number);
				continue;
			}

			if (Character.isLetter(src.charAt(i)) || src.charAt(i) == '_') {
				//move i to the end(last char +1) of the current token
				while (++i < src.length()//
						&& (Character.isLetterOrDigit(src.charAt(i)) || src.charAt(i) == '_')) ;
				currentLex.append(src, tokenBegin, i);

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
			if (match(src, i, '&', '&')) {
				currentLex.append(src, i, i + 2);
				newToken.accept(TokenKind.AndAnd);
				i += 2;
				continue;
			}
			if (match(src, i, '|', '|')) {
				currentLex.append(src, i, i + 2);
				newToken.accept(TokenKind.OrOr);
				i += 2;
				continue;
			}
			if (src.charAt(i) == '!') {
				if (match(src, i + 1, '=')) {
					currentLex.append(src, i, i + 2);
					newToken.accept(TokenKind.NotEquals);
					i += 2;
					continue;
				}
				currentLex.append('!');
				newToken.accept(TokenKind.Not);
				i++;
				continue;
			}
			if (match(src, i, '=', '=')) {
				currentLex.append(src, i, i + 2);
				newToken.accept(TokenKind.EqualEqual);
				i += 2;
				continue;
			}
			if (src.charAt(i) == '>') {
				if (match(src, i + 1, '=')) {
					currentLex.append(src, i, i + 2);
					newToken.accept(TokenKind.GreaterEquals);
					i += 2;
					continue;
				}
				currentLex.append('>');
				newToken.accept(TokenKind.Greater);
				i++;
				continue;
			}
			if (src.charAt(i) == '<') {
				if (match(src, i + 1, '=')) {
					currentLex.append(src, i, i + 2);
					newToken.accept(TokenKind.LessEquals);
					i += 2;
					continue;
				}
				currentLex.append('<');
				newToken.accept(TokenKind.Less);
				i++;
				continue;
			}

			//match arithmetic operators
			if (src.charAt(i) == '+') {
				currentLex.append('+');
				newToken.accept(TokenKind.Plus);
				i++;
				continue;
			}
			if (src.charAt(i) == '-') {
				currentLex.append('-');
				newToken.accept(TokenKind.Minus);
				i++;
				continue;
			}
			if (src.charAt(i) == '*') {
				currentLex.append('*');
				newToken.accept(TokenKind.Multiply);
				i++;
				continue;
			}
			if (src.charAt(i) == '/') {
				currentLex.append('/');
				newToken.accept(TokenKind.Divide);
				i++;
				continue;
			}
			if (src.charAt(i) == '%') {
				currentLex.append('%');
				newToken.accept(TokenKind.Rem);
				i++;
				continue;
			}
			if (src.charAt(i) == '^') {
				currentLex.append('^');
				newToken.accept(TokenKind.Pow);
				i++;
				continue;
			}

			//match others
			if (src.charAt(i) == '?') {
				currentLex.append('?');
				newToken.accept(TokenKind.QMark);
				i++;
				continue;
			}
			if (src.charAt(i) == ':') {
				currentLex.append(':');
				newToken.accept(TokenKind.Colon);
				i++;
				continue;
			}
			if (src.charAt(i) == ',') {
				currentLex.append(',');
				newToken.accept(TokenKind.Comma);
				i++;
				continue;
			}
			if (src.charAt(i) == ';') {
				currentLex.append(';');
				newToken.accept(TokenKind.Sem);
				i++;
				continue;
			}
			if (src.charAt(i) == '(') {
				currentLex.append('(');
				newToken.accept(TokenKind.LParen);
				i++;
				continue;
			}
			if (src.charAt(i) == ')') {
				currentLex.append(')');
				newToken.accept(TokenKind.RParen);
				i++;
				continue;
			}
			if (src.charAt(i) == '[') {
				currentLex.append('[');
				newToken.accept(TokenKind.LBracket);
				i++;
				continue;
			}
			if (src.charAt(i) == ']') {
				currentLex.append(']');
				newToken.accept(TokenKind.RBracket);
				i++;
				continue;
			}
			if (src.charAt(i) == '{') {
				currentLex.append('{');
				newToken.accept(TokenKind.LCurly);
				i++;
				continue;
			}
			if (src.charAt(i) == '}') {
				currentLex.append('}');
				newToken.accept(TokenKind.RCurly);
				i++;
				continue;
			}
			if (src.charAt(i) == '$') {
				throw new RuntimeException("symbol $ is reserved");
			}

			throw new RuntimeException("can not resolve token at " + tokenBegin);
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
