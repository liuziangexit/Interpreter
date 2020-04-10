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

	//TODO 把这里挨个match改掉，改成在map里预先定义好token，然后试map

	static private class TokenTemplate {
	}

	static private HashMap<String, TokenKind> tokenMap = new HashMap<>();

	static {
		tokenMap.put("true", TokenKind.True);
		tokenMap.put("false", TokenKind.False);
		tokenMap.put("&&", TokenKind.AndAnd);
		tokenMap.put("||", TokenKind.OrOr);
		tokenMap.put("!", TokenKind.Not);
		tokenMap.put("!=", TokenKind.NotEquals);
		tokenMap.put("==", TokenKind.EqualEqual);
		tokenMap.put(">", TokenKind.Greater);
		tokenMap.put(">=", TokenKind.GreaterEquals);
		tokenMap.put("<", TokenKind.Less);
		tokenMap.put("<=", TokenKind.LessEquals);
		tokenMap.put("+", TokenKind.Plus);
		tokenMap.put("-", TokenKind.Minus);
		tokenMap.put("*", TokenKind.Multiply);
		tokenMap.put("/", TokenKind.Divide);
		tokenMap.put("%", TokenKind.Rem);
		tokenMap.put("^", TokenKind.Pow);
		tokenMap.put("?", TokenKind.QMark);
		tokenMap.put(":", TokenKind.Colon);
		tokenMap.put(",", TokenKind.Comma);
		tokenMap.put(";", TokenKind.Sem);
		tokenMap.put("(", TokenKind.LParen);
		tokenMap.put(")", TokenKind.RParen);
		tokenMap.put("[", TokenKind.LBracket);
		tokenMap.put("]", TokenKind.RBracket);
		tokenMap.put("{", TokenKind.LCurly);
		tokenMap.put("}", TokenKind.RCurly);
	}

	//人肉自动机
	static public List<Lexeme> scan(final String src) {
		final List<Lexeme> result = new ArrayList<>(Math.max(src.length() / 3, 5));//假定平均每3个字一个词
		final StringBuilder currentLex = new StringBuilder(30);//很少有词长度大于30
		Position pos = new Position(0, 0, 0);
		final Pointer<Position> tokenBegin = new Pointer<>(new Position(0, 0, 0));
		final Consumer<TokenKind> lexemeCreator = kind -> {
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
				lexemeCreator.accept(TokenKind.Number);
				continue;
			}

			//match ID, true, false
			if (Character.isLetter(src.charAt(pos.count())) || src.charAt(pos.count()) == '_') {
				//move i to the end(last char +1) of the current token
				while (pos.nextColumn() < src.length()//
						&& (Character.isLetterOrDigit(src.charAt(pos.count())) || src.charAt(pos.count()) == '_')) ;
				currentLex.append(src, tokenBegin.v.count(), pos.count());

				TokenKind tokenKind = tokenMap.get(currentLex.toString());
				lexemeCreator.accept(tokenKind != null ? tokenKind : TokenKind.Identifier);
				continue;
			}

			//match other predefined token
			currentLex.append(src.charAt(pos.count()));
			if (pos.count() + 1 < src.length()) {
				currentLex.append(src.charAt(pos.count() + 1));
				TokenKind tokenKind = tokenMap.get(currentLex.toString());
				if (tokenKind != null) {
					pos.nextColumn(2);
					lexemeCreator.accept(tokenKind);
					continue;
				}
				currentLex.deleteCharAt(1);
			}
			TokenKind tokenKind = tokenMap.get(currentLex.toString());
			if (tokenKind != null) {
				pos.nextColumn();
				lexemeCreator.accept(tokenKind);
				continue;
			}

			throw new BadSyntaxException("can not resolve token at " + tokenBegin, src);
		}
		return result;
	}

}
