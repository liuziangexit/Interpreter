package com.miwan.interpreter.syntax;

import com.miwan.interpreter.Pointer;
import com.miwan.interpreter.lexical.Lexeme;
import com.miwan.interpreter.lexical.TokenKind;

import java.util.*;
import java.util.function.Function;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 12/18/2019
 * <p>
 * TODO ⚠️ON PROGRESS
 * Syntax Analysis Phase
 */

public class Parser {
	//return Root Node
	static public Node parse(final List<Lexeme> lexemes) {
		return parseImpl(lexemes, new Pointer<>(0), true);
	}

	static private Node parseImpl(final List<Lexeme> lexemes, Pointer<Integer> cursor, boolean parseBinaryOp) {
		if (cursor.v >= lexemes.size())
			return null;

		Node lhs;
		switch (lexemes.get(cursor.v).kind) {
			case LParen: {
				lhs = parseParenExpr(lexemes, cursor);
			}
			break;
			case Identifier: {
				lhs = parseId(lexemes, cursor);
			}
			break;
			case Minus: {
				if (lexemes.get(cursor.v + 1).kind == TokenKind.Number) {
					lhs = parseNumber(lexemes, cursor);
				} else {
					cursor.v++;
					lhs = new BinaryExpr("*", new NumberExpr(-1), parseImpl(lexemes, cursor, false));
				}
			}
			break;
			case Number: {
				lhs = parseNumber(lexemes, cursor);
			}
			break;
			default:
				return null;
		}
		while (parseBinaryOp && cursor.v < lexemes.size()) {
			Node expr = parseBinaryExpr(lexemes, cursor, lhs, Integer.MAX_VALUE);
			if (expr == lhs)
				break;
			lhs = expr;
		}
		return lhs;
	}

	static private Node parseId(final List<Lexeme> lexemes, Pointer<Integer> idx) {
		if (lexemes.get(idx.v + 1).kind != TokenKind.LParen) {
			return new IdExpr(lexemes.get(idx.v++).text);
		} else {
			Lexeme func = lexemes.get(idx.v);
			idx.v++;//eat function name
			ArrayList<Node> args = new ArrayList<>();
			while (idx.v < lexemes.size() && lexemes.get(idx.v).kind != TokenKind.RParen) {
				idx.v++;
				args.add(parseImpl(lexemes, idx, true));
			}
			if (idx.v >= lexemes.size() || lexemes.get(idx.v).kind != TokenKind.RParen)
				throw new RuntimeException("expect a ')'");
			idx.v++;//eat (
			return new CallExpr(func.text, args);
		}
	}

	static private Node parseNumber(final List<Lexeme> lexemes, Pointer<Integer> idx) {
		String text;
		Lexeme lexeme = lexemes.get(idx.v++);
		if (lexeme.kind == TokenKind.Minus) {
			text = lexeme.text + lexemes.get(idx.v++).text;
		} else {
			text = lexeme.text;
		}
		if (text.contains(".")) {
			return new NumberExpr(Double.parseDouble(text));
		} else {
			return new NumberExpr(Integer.parseInt(text));
		}
	}

	static private Node parseParenExpr(final List<Lexeme> lexemes, Pointer<Integer> idx) {
		idx.v++;//eat (
		ParenExpr parenExpr = new ParenExpr(parseImpl(lexemes, idx, true));
		if (lexemes.get(idx.v).kind != TokenKind.RParen)
			throw new RuntimeException("expected ')'");
		idx.v++;  // eat )
		return parenExpr;
	}

	static private Node parseBinaryExpr(final List<Lexeme> lexemes, Pointer<Integer> idx, Node lhs, int prevPrecedence) {
		if (idx.v == lexemes.size())
			return lhs;
		Lexeme opLex = lexemes.get(idx.v);
		OperatorDefinition.OperatorInfo opInfo = OperatorDefinition.operators.get(opLex.text);
		if (opInfo == null || opInfo.precedence > prevPrecedence) {
			return lhs;
		}
		idx.v++;//eat op
		Node rhs = parseImpl(lexemes, idx, false);
		if (idx.v < lexemes.size()) {
			OperatorDefinition.OperatorInfo nextOpInfo = OperatorDefinition.operators.get(lexemes.get(idx.v).text);
			//如果下一个token是运算符
			if (nextOpInfo != null) {
				if (nextOpInfo.precedence < opInfo.precedence) {
					//下一个运算符优先级比当前运算符的高
					rhs = parseBinaryExpr(lexemes, idx, rhs, nextOpInfo.precedence);
				}
			}
		}
		return parseBinaryExpr(lexemes, idx, new BinaryExpr(opLex.text, lhs, rhs), opInfo.precedence);
	}

}
