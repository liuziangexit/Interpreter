package com.miwan.interpreter.syntax;

import com.miwan.interpreter.runtime.OperatorDefinition;
import com.miwan.interpreter.syntax.ast.*;
import com.miwan.interpreter.util.Pointer;
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
	static public Node parse(final List<Lexeme> lexemes) {
		return parseImpl(lexemes, new Pointer<>(0), true);
	}

	static private Node parseImpl(final List<Lexeme> lexemes, Pointer<Integer> cursor, boolean parseBinaryOp) {
		if (cursor.v >= lexemes.size())
			return null;

		Node lhs;
		switch (lexemes.get(cursor.v).kind) {
			case LParen: {
				cursor.v++;//eat (
				ParenExpr parenExpr = new ParenExpr(parseImpl(lexemes, cursor, true));
				if (lexemes.get(cursor.v).kind != TokenKind.RParen)
					throw new RuntimeException("expected ')'");
				cursor.v++;  // eat )
				lhs = parenExpr;
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
			case True:
			case False: {
				Lexeme lexeme = lexemes.get(cursor.v++);
				if (lexeme.kind == TokenKind.True) {
					lhs = new BooleanLiteralExpr(true);
				} else {
					lhs = new BooleanLiteralExpr(false);
				}
			}
			break;
			case Not: {
				cursor.v++;//eat !
				lhs = new LogicNotExpr(parseImpl(lexemes, cursor, true));
			}
			break;
			/*case QMark: {
			}
			break;*/
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
			List<Node> argList;
			if (lexemes.get(idx.v + 1).kind != TokenKind.RParen) {
				//parse argument list
				argList = new ArrayList<>();
				while (idx.v < lexemes.size() && lexemes.get(idx.v).kind != TokenKind.RParen) {
					idx.v++;
					argList.add(parseImpl(lexemes, idx, true));
				}
			} else {
				//no argument
				argList = Collections.emptyList();
				idx.v++;//eat (
			}
			if (idx.v >= lexemes.size() || lexemes.get(idx.v).kind != TokenKind.RParen)
				throw new RuntimeException("expect a ')'");
			idx.v++;//eat )
			return new CallExpr(func.text, argList);
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
