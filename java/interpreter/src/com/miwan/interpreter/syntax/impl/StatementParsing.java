package com.miwan.interpreter.syntax.impl;

import com.miwan.interpreter.lexical.LexStream;
import com.miwan.interpreter.lexical.Lexeme;
import com.miwan.interpreter.lexical.TokenKind;
import com.miwan.interpreter.syntax.BadSyntaxException;
import com.miwan.interpreter.syntax.Parser;
import com.miwan.interpreter.syntax.ast.*;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class StatementParsing {

	static public Statement parse() {
		return null;
	}

	/*static private Node parseVarDecl(final LexStream lexStream, final Parser.State state) {
		lexStream.eat();//eat var
		List<AbstractMap.SimpleImmutableEntry<IdExpr, Expression>> initializeList = new ArrayList<>();
		while (true) {
			Lexeme id = lexStream.eat();
			if (id.kind != TokenKind.Identifier) {
				throw new BadSyntaxException("identifier required", lexStream.getRawContent());
			}
			if (LexStream.test(lexStream.current(), p -> p.kind == TokenKind.Assign)) {
				lexStream.eat();//eat =
				Node rhs = parse(lexStream, new Parser.State(state.shouldParseBinaryOp, state.shouldParseCondExpr, false));
				if (!(rhs instanceof Expression)) {
					throw new BadSyntaxException("expression expected", lexStream.getRawContent());
				}
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
	}*/
}
