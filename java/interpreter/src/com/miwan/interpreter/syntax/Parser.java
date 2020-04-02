package com.miwan.interpreter.syntax;

import com.miwan.interpreter.lexical.LexStream;
import com.miwan.interpreter.runtime.Builtin;
import com.miwan.interpreter.syntax.ast.*;
import com.miwan.interpreter.lexical.Lexeme;
import com.miwan.interpreter.lexical.TokenKind;

import java.util.*;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 12/18/2019
 * <p>
 * 自顶向下的LL(k)
 */

public class Parser {
	//return Root Node
	static public Node parse(final LexStream lexStream) {
		Node ast = parseImpl(lexStream, true);
		if (lexStream.hasNext()) {
			throw new BadSyntaxException("could not parse tokens after " + lexStream.current());
		}
		return ast;
	}

	static private Node parseImpl(final LexStream lexStream, boolean shouldParseBinaryOp) {
		if (lexStream.current() == null)
			return null;
		Node lhs;
		switch (lexStream.current().kind) {
			case LParen: {
				Lexeme lp = lexStream.eat();//eat (
				ParenExpr parenExpr = new ParenExpr(parseImpl(lexStream, true));
				if (!LexStream.test(lexStream.eat(), eat -> eat.kind == TokenKind.RParen))
					throw new BadSyntaxException(lp + " missing corresponds ')'");
				lhs = parenExpr;
			}
			break;
			case Identifier: {
				if (LexStream.test(lexStream.peek(), peek -> peek.kind == TokenKind.LParen)) {
					lhs = parseCall(lexStream);
				} else {
					lhs = new IdExpr(lexStream.eat().text);
				}
			}
			break;
			case Minus: {
				if (!lexStream.hasNext()) {
					throw new BadSyntaxException("expression expected after " + lexStream.current());
				}
				if (lexStream.peek().kind == TokenKind.Number) {
					lhs = parseNumber(lexStream);
				} else {
					Lexeme minusLex = lexStream.eat();//eat -
					lhs = new BinaryExpr("*", new NumberExpr(-1), //
							requireNonNull(//
									parseImpl(lexStream, false), //
									"unknown error occurs while parsing tokens after " + minusLex));
				}
			}
			break;
			case Number: {
				lhs = parseNumber(lexStream);
			}
			break;
			case True:
			case False: {
				Lexeme lexeme = lexStream.eat();
				if (lexeme.kind == TokenKind.True) {
					lhs = new BooleanLiteralExpr(true);
				} else {
					lhs = new BooleanLiteralExpr(false);
				}
			}
			break;
			case Not: {
				if (!lexStream.hasNext())
					throw new BadSyntaxException("expression expected after " + lexStream.current());
				Lexeme notLex = lexStream.eat();//eat !
				lhs = new LogicNotExpr(//
						requireNonNull(//
								parseImpl(lexStream, false),//
								"unknown error occurs while parsing tokens after " + notLex));
			}
			break;
			/*case QMark: {
				cursor.v++;//eat ?
				Node node = parseImpl(lexemes, cursor, parseBinaryOp);
			}
			break;*/
			default:
				return null;
		}
		while (shouldParseBinaryOp && lexStream.hasNext()) {
			Node expr = parseBinaryExpr(lexStream, lhs, Integer.MIN_VALUE);
			if (expr == lhs)
				break;
			lhs = expr;
		}
		return lhs;
	}

	static private Node parseCall(final LexStream lexStream) {
		Lexeme func = lexStream.eat();
		List<Node> argList;
		if (!lexStream.hasNext())
			throw new BadSyntaxException("bad call expression " + func);
		if (lexStream.peek().kind != TokenKind.RParen) {
			//parse argument list
			argList = new ArrayList<>();
			do {
				Lexeme eat = lexStream.eat();//eat ( or ,
				argList.add(//
						requireNonNull(//
								parseImpl(lexStream, true), //
								"unknown error occurs while parsing tokens after " + eat));
			} while (lexStream.hasNext() && lexStream.current().kind != TokenKind.RParen);
		} else {
			//no argument
			argList = Collections.emptyList();
			lexStream.eat();//eat (
		}
		if (!LexStream.test(lexStream.eat(), eat -> eat.kind == TokenKind.RParen))
			throw new BadSyntaxException("expect ')' for " + func);
		return new CallExpr(func.text, argList);
	}

	static private Node parseNumber(final LexStream lexStream) {
		String text;
		Lexeme lexeme = lexStream.eat();
		if (lexeme.kind == TokenKind.Minus) {
			text = "-" + lexStream.eat().text;
		} else {
			text = lexeme.text;
		}
		if (text.contains(".")) {
			double v;
			try {
				v = Double.parseDouble(text);
			} catch (NumberFormatException e) {
				throw new BadSyntaxException("invalid number format at " + lexeme);
			}
			return new NumberExpr(v);
		} else {
			int v;
			try {
				v = Integer.parseInt(text);
			} catch (NumberFormatException e) {
				throw new BadSyntaxException("invalid number format at " + lexeme);
			}
			return new NumberExpr(v);
		}
	}

	static private Node parseBinaryExpr(final LexStream lexStream, Node lhs, int prevPrecedence) {
		if (!lexStream.hasNext())
			return lhs;
		Lexeme opLex = lexStream.current();
		Integer opPrd = Builtin.precedence(opLex.text);
		if (opPrd == null || opPrd < prevPrecedence) {
			return lhs;
		}
		lexStream.eat();//eat operator
		Node rhs = requireNonNull(//
				parseImpl(lexStream, false),//
				"unknown error occurs while parsing tokens after " + opLex);
		if (lexStream.hasNext()) {
			Integer nextOpPrd = Builtin.precedence(lexStream.current().text);
			//如果下一个token是运算符
			if (nextOpPrd != null) {
				if (nextOpPrd > opPrd) {
					//下一个运算符优先级比当前运算符的高
					rhs = parseBinaryExpr(lexStream, rhs, nextOpPrd);
				}
			}
		}
		//FIXME 为什么要递归？
		return parseBinaryExpr(lexStream, new BinaryExpr(opLex.text, lhs, rhs), opPrd);
	}

	static private <T extends Node> T requireNonNull(T node, String exceptionMessage) {
		if (node == null)
			throw new BadSyntaxException(exceptionMessage);
		return node;
	}

}
