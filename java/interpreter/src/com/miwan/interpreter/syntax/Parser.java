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

		Node lhs = null;
		switch (lexemes.get(cursor.v).kind) {
			case LParen: {
				lhs = parseParenExpr(lexemes, cursor);
			}
			break;
			case Identifier: {
				lhs = parseId(lexemes, cursor);
			}
			break;
			case Number: {
				lhs = parseNumber(lexemes, cursor);
			}
			break;
			default:
				return null;
		}
		if (parseBinaryOp && cursor.v < lexemes.size())
			return parseBinaryExpr(lexemes, cursor, lhs);
		return lhs;
	}

	static private Node parseId(final List<Lexeme> lexemes, Pointer<Integer> idx) {
		if (lexemes.get(idx.v + 1).kind != TokenKind.LParen) {
			return new IdExpr(lexemes.get(idx.v++).text);
		} else {
			Lexeme func = lexemes.get(idx.v);
			idx.v += 2;//eat function name and (
			ArrayList<Node> args = new ArrayList<>();
			for (; idx.v < lexemes.size() && lexemes.get(idx.v).kind != TokenKind.RParen; idx.v++) {
				args.add(parseImpl(lexemes, idx, true));
			}
			idx.v++;//eat)
			return new CallExpr(func.text, args);
		}
	}

	static private Node parseNumber(final List<Lexeme> lexemes, Pointer<Integer> idx) {
		Lexeme lexeme = lexemes.get(idx.v++);
		if (lexeme.text.contains(".")) {
			return new NumberExpr(Double.parseDouble(lexeme.text));
		} else {
			return new NumberExpr(Integer.parseInt(lexeme.text));
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

	//1+1+1
	static private Node parseBinaryExpr(final List<Lexeme> lexemes, Pointer<Integer> idx, Node lhs) {
		Lexeme opLex = lexemes.get(idx.v);
		OperatorDefinition.OperatorInfo opInfo = OperatorDefinition.operators.get(opLex.text);
		if (opInfo == null) {
			return lhs;
		}
		idx.v++;//eat op
		Node rhs = parseImpl(lexemes, idx, false);
		if (idx.v < lexemes.size()) {
			OperatorDefinition.OperatorInfo nextOpInfo = OperatorDefinition.operators.get(lexemes.get(idx.v).text);
			if (nextOpInfo != null) {
				if (nextOpInfo.precedence < opInfo.precedence) {
					rhs = parseBinaryExpr(lexemes, idx, rhs);
				} else {
					return parseBinaryExpr(lexemes, idx, new BinaryExpr(opLex, lhs, rhs));
				}
			}
		}
		return new BinaryExpr(opLex, lhs, rhs);
	}

}
