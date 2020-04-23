package com.miwan.interpreter.syntax.impl;

import com.miwan.interpreter.lexical.LexStream;
import com.miwan.interpreter.lexical.Lexeme;
import com.miwan.interpreter.lexical.TokenKind;
import com.miwan.interpreter.runtime.Builtin;
import com.miwan.interpreter.syntax.BadSyntaxException;
import com.miwan.interpreter.syntax.Parser;
import com.miwan.interpreter.syntax.ast.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpressionParsing {

	static public Expression parse(final LexStream lexStream, Parser.State state) {
		if (lexStream.current() == null)
			return null;
		Expression parsed;
		switch (lexStream.current().kind) {
			case LParen: {
				Lexeme lp = lexStream.eat();//eat (
				ParenExpr parenExpr = new ParenExpr(parse(lexStream, new Parser.State(true, true, false)));
				if (!LexStream.test(lexStream.eat(), eat -> eat.kind == TokenKind.RParen))
					throw new BadSyntaxException(lp + " missing corresponds ')'", lexStream.getRawContent());
				parsed = parenExpr;
			}
			break;
			case Identifier: {
				if (LexStream.test(lexStream.peek(), peek -> peek.kind == TokenKind.LParen)) {
					parsed = parseCall(lexStream);
				} else {
					parsed = new IdExpr(lexStream.eat().text);
				}
			}
			break;
			case Minus: {
				if (!lexStream.hasNext()) {
					throw new BadSyntaxException("expression expected after " + lexStream.current(), lexStream.getRawContent());
				}
				if (lexStream.peek().kind == TokenKind.Number) {
					parsed = parseNumber(lexStream);
				} else {
					Lexeme minusLex = lexStream.eat();//eat -
					parsed = new BinaryExpr("*", new NumberExpr(-1), //
							requireNonNull(//
									parse(lexStream, new Parser.State(false, true, false)), //
									"unknown error occurs while parsing tokens after " + minusLex, lexStream.getRawContent()));
				}
			}
			break;
			case Number: {
				parsed = parseNumber(lexStream);
			}
			break;
			case True:
			case False: {
				Lexeme lexeme = lexStream.eat();
				if (lexeme.kind == TokenKind.True) {
					parsed = new BooleanLiteralExpr(true);
				} else {
					parsed = new BooleanLiteralExpr(false);
				}
			}
			break;
			case Not: {
				if (!lexStream.hasNext())
					throw new BadSyntaxException("expression expected after " + lexStream.current(), lexStream.getRawContent());
				Lexeme notLex = lexStream.eat();//eat !
				parsed = new LogicNotExpr(//
						requireNonNull(//
								parse(lexStream, new Parser.State(false, false, false)),//
								"unknown error occurs while parsing tokens after " + notLex, lexStream.getRawContent()));
			}
			break;
			default:
				return null;
		}
		while (state.shouldParseBinaryOp && lexStream.hasNext()) {
			Expression expr = parseBinaryExpr(lexStream, parsed, Integer.MIN_VALUE);
			if (expr == parsed)
				break;
			parsed = expr;
		}
		if (state.shouldParseCondExpr && LexStream.test(lexStream.current(), current -> current.kind == TokenKind.QMark)) {
			return parseCondExpr(lexStream, parsed);
		}
		return parsed;
	}


	static private Expression parseCall(final LexStream lexStream) {
		Lexeme func = lexStream.eat();
		List<Expression> argList;
		if (!lexStream.hasNext())
			throw new BadSyntaxException("bad call expression " + func, lexStream.getRawContent());
		if (lexStream.peek().kind != TokenKind.RParen) {
			//parse argument list
			argList = new ArrayList<>();
			do {
				Lexeme eat = lexStream.eat();//eat ( or ,
				argList.add(//
						requireNonNull(//
								parse(lexStream, new Parser.State(true, true, false)), //
								"unknown error occurs while parsing tokens after " + eat, lexStream.getRawContent())
				);
			} while (lexStream.hasNext() && lexStream.current().kind != TokenKind.RParen);
		} else {
			//no argument
			argList = Collections.emptyList();
			lexStream.eat();//eat (
		}
		if (!LexStream.test(lexStream.eat(), eat -> eat.kind == TokenKind.RParen))
			throw new BadSyntaxException("expect ')' for " + func, lexStream.getRawContent());
		return new CallExpr(func.text, argList);
	}

	static private Expression parseNumber(final LexStream lexStream) {
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
				throw new BadSyntaxException("invalid number format at " + lexeme, lexStream.getRawContent());
			}
			return new NumberExpr(v);
		} else {
			int v;
			try {
				v = Integer.parseInt(text);
			} catch (NumberFormatException e) {
				throw new BadSyntaxException("invalid number format at " + lexeme, lexStream.getRawContent());
			}
			return new NumberExpr(v);
		}
	}

	static private Expression parseBinaryExpr(final LexStream lexStream, Expression lhs, int prevPrecedence) {
		if (!lexStream.hasNext())
			return lhs;
		Lexeme opLex = lexStream.current();
		Integer opPrd = Builtin.precedence(opLex.text);
		if (opPrd == null || opPrd < prevPrecedence) {
			return lhs;
		}
		lexStream.eat();//eat operator
		Expression rhs = requireNonNull(//
				parse(lexStream, new Parser.State(false, false, false)),//
				"unknown error occurs while parsing tokens after " + opLex, lexStream.getRawContent());
		while (lexStream.current() != null) {
			Integer nextOpPrd = Builtin.precedence(lexStream.current().text);
			//如果下一个token是运算符
			if (nextOpPrd != null) {
				if (nextOpPrd > opPrd) {
					//下一个运算符优先级比当前运算符的高
					//就把当前运算符的rhs当作下一个运算符的lhs
					rhs = parseBinaryExpr(lexStream, rhs, nextOpPrd);
					//并且重复这个流程
					continue;
				}
			}
			break;
		}
		return parseBinaryExpr(lexStream, new BinaryExpr(opLex.text, lhs, rhs), opPrd);
	}

	static private Expression parseCondExpr(final LexStream lexStream, Expression cond) {
		Lexeme qMarkPlace = lexStream.eat();
		if (qMarkPlace.kind != TokenKind.QMark)
			throw new BadSyntaxException("unexpected token " + qMarkPlace, lexStream.getRawContent());
		Expression trueBranch = parse(lexStream, new Parser.State(true, true, false));
		if (trueBranch == null) {
			throw new BadSyntaxException("could not parse tokens after " + qMarkPlace, lexStream.getRawContent());
		}
		Lexeme colonPlace = lexStream.eat();
		if (colonPlace == null) {
			throw new BadSyntaxException("expected : for condition expression starts with " + cond, lexStream.getRawContent());
		}
		if (colonPlace.kind != TokenKind.Colon) {
			throw new BadSyntaxException("expected : at " + colonPlace + " for condition expression starts with " + cond, lexStream.getRawContent());
		}
		Expression falseBranch = parse(lexStream, new Parser.State(true, true, false));
		if (falseBranch == null) {
			throw new BadSyntaxException("could not parse tokens after " + colonPlace, lexStream.getRawContent());
		}
		return new CondExpr(cond, trueBranch, falseBranch);
	}

	static private <T extends Node> T requireNonNull(T node, String exceptionMessage, String rawContent) {
		if (node == null)
			throw new BadSyntaxException(exceptionMessage, rawContent);
		return node;
	}

}
