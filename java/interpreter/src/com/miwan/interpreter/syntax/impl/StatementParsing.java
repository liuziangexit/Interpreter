package com.miwan.interpreter.syntax.impl;

import com.miwan.interpreter.lexical.LexStream;
import com.miwan.interpreter.lexical.Lexeme;
import com.miwan.interpreter.lexical.TokenKind;
import com.miwan.interpreter.syntax.BadSyntaxException;
import com.miwan.interpreter.syntax.ast.*;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatementParsing {

	static public Statement parse(final LexStream lexStream) {
		if (lexStream.current() == null)
			return null;
		switch (lexStream.current().kind) {
			case Identifier: {
				if (lexStream.current().text.equals("var")) {
					return parseVarDecl(lexStream);
				} else if (lexStream.current().text.equals("return")) {
					lexStream.eat();
					ReturnStatement returnStatement = new ReturnStatement(ExpressionParsing.parse(lexStream));
					if (!LexStream.test(lexStream.eat(), e -> e.kind == TokenKind.Sem))
						throw new BadSyntaxException("';' required", lexStream.getRawContent());
					return returnStatement;
				} else if (lexStream.current().text.equals("func")) {
					return parseFuncDef(lexStream);
				} else if (lexStream.current().text.equals("if")) {
					return parseIf(lexStream);
				} else if (LexStream.test(lexStream.peek(), p -> p.kind == TokenKind.Assign)) {
					IdExpr id = new IdExpr(lexStream.eat().text);
					lexStream.eat();//eat =
					Expression rhs = ExpressionParsing.parse(lexStream);
					if (!LexStream.test(lexStream.eat(), e -> e.kind == TokenKind.Sem))
						throw new BadSyntaxException("';' required", lexStream.getRawContent());
					return new AssignExpr(id, rhs);
				}
			}
			break;
			case LCurly: {
				//i hate case without break
				if (true)
					return parseBlock(lexStream, true);
			}
			break;
		}
		Expression expr = ExpressionParsing.parse(lexStream);
		//allow single line expression without ';'
		if (LexStream.test(lexStream.current(), c -> c.kind == TokenKind.Sem)) {
			lexStream.eat();
		}
		return new ExpressionStmt(expr);
	}

	static public Statement parseBlock(final LexStream lexStream, boolean explicitBlock) {
		if (explicitBlock)
			lexStream.eat();//eat {

		final BlockStmt rootBlock = new BlockStmt(new ArrayList<>(1));
		Lexeme pos = lexStream.current();
		//FIXME 这个地方好像不对，正常block都是以括号标志结束的，而这里的特殊情况似乎反客为主了
		//应该确保这里只会返回BlockStmt
		while (lexStream.current() != null) {
			if (explicitBlock) {
				if (LexStream.test(lexStream.current(), p -> p.kind == TokenKind.RCurly)) {
					lexStream.eat();
					break;
				}
			}
			rootBlock.statements.add(StatementParsing.parse(lexStream));
			if (pos == lexStream.current())
				throw new BadSyntaxException("could not parse tokens after " + lexStream.current(), lexStream.getRawContent());
			pos = lexStream.current();
		}

		if (!explicitBlock && rootBlock.statements.size() == 1 && rootBlock.statements.get(0) instanceof ExpressionStmt) {
			return ((ExpressionStmt) rootBlock.statements.get(0)).expr;
		} else {
			return rootBlock;
		}
	}

	static private Statement parseVarDecl(final LexStream lexStream) {
		lexStream.eat();//eat var
		List<AbstractMap.SimpleImmutableEntry<IdExpr, Expression>> initializeList = new ArrayList<>();
		while (true) {
			Lexeme id = lexStream.eat();
			if (id.kind != TokenKind.Identifier) {
				throw new BadSyntaxException("identifier required", lexStream.getRawContent());
			}
			if (LexStream.test(lexStream.current(), p -> p.kind == TokenKind.Assign)) {
				lexStream.eat();//eat =
				Expression rhs = ExpressionParsing.parse(lexStream);
				initializeList.add(new AbstractMap.SimpleImmutableEntry<>(new IdExpr(id.text), (Expression) rhs));
			} else {
				initializeList.add(new AbstractMap.SimpleImmutableEntry<>(new IdExpr(id.text), null));
			}
			if (LexStream.test(lexStream.current(), p -> p.kind == TokenKind.Comma)) {
				lexStream.eat();
			} else if (!LexStream.test(lexStream.current(), p -> p.kind != TokenKind.Sem)) {
				break;
			}
		}
		if (!LexStream.test(lexStream.eat(), e -> e.kind == TokenKind.Sem)) {
			throw new BadSyntaxException("';' required", lexStream.getRawContent());
		}
		return new VariableDeclarationStmt(initializeList);
	}

	static private FunctionDeclarationStmt parseFuncDef(final LexStream lexStream) {
		lexStream.eat();//eat func
		Lexeme name = lexStream.eat();
		if (name == null || name.kind != TokenKind.Identifier) {
			throw new BadSyntaxException("identifier required after keyword 'func'", lexStream.getRawContent());
		}
		if (!LexStream.test(lexStream.eat(), e -> e.kind == TokenKind.LParen)) {
			throw new BadSyntaxException("bad function declaration", lexStream.getRawContent());
		}
		List<IdExpr> parameters;
		if (LexStream.test(lexStream.current(), c -> c.kind == TokenKind.RParen)) {
			parameters = Collections.emptyList();
			lexStream.eat();
		} else {
			parameters = new ArrayList<>();
			do {
				Lexeme id = lexStream.eat();
				if (id == null || id.kind != TokenKind.Identifier)
					throw new BadSyntaxException("bad parameter list", lexStream.getRawContent());
				parameters.add(new IdExpr(id.text));
				Lexeme next = lexStream.current();
				if (LexStream.test(next, c -> c.kind == TokenKind.Comma)) {
					lexStream.eat();
				} else {
					break;
				}
			} while (true);
			if (!LexStream.test(lexStream.eat(), c -> c.kind == TokenKind.RParen)) {
				throw new BadSyntaxException("')' expected", lexStream.getRawContent());
			}
		}
		BlockStmt body = (BlockStmt) parseBlock(lexStream, true);
		return new FunctionDeclarationStmt(name.text, parameters, body);
	}

	static private IfStmt parseIf(final LexStream lexStream) {
		lexStream.eat();
		if (!LexStream.test(lexStream.eat(), e -> e.kind == TokenKind.LParen)) {
			throw new BadSyntaxException("bad if", lexStream.getRawContent());
		}
		Expression cond = ExpressionParsing.parse(lexStream);
		if (!LexStream.test(lexStream.eat(), e -> e.kind == TokenKind.RParen)) {
			throw new BadSyntaxException("bad if", lexStream.getRawContent());
		}
		Statement trueBranch = parse(lexStream);
		Statement falseBranch = null;
		if (LexStream.test(lexStream.current(), c -> c.kind == TokenKind.Identifier && c.text.equals("else"))) {
			lexStream.eat();
			falseBranch = parse(lexStream);
		}
		return new IfStmt(cond, trueBranch, falseBranch);
	}

}
