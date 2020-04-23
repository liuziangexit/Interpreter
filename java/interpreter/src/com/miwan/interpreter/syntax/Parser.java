package com.miwan.interpreter.syntax;

import com.miwan.interpreter.lexical.LexStream;
import com.miwan.interpreter.runtime.Builtin;
import com.miwan.interpreter.syntax.ast.*;
import com.miwan.interpreter.lexical.Lexeme;
import com.miwan.interpreter.lexical.TokenKind;
import com.miwan.interpreter.syntax.impl.ExpressionParsing;

import java.util.*;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 12/18/2019
 * <p>
 * 自顶向下的LL(k)文法解析器，俗称递归下降
 */

public class Parser {

	static public class State {
		public State(boolean shouldParseBinaryOp, boolean shouldParseCondExpr, boolean shouldParseSem) {
			this.shouldParseBinaryOp = shouldParseBinaryOp;
			this.shouldParseCondExpr = shouldParseCondExpr;
			this.shouldParseSem = shouldParseSem;
		}

		final public boolean shouldParseBinaryOp;
		final public boolean shouldParseCondExpr;
		final public boolean shouldParseSem;
	}

	//return Root Node
	static public Node parse(final LexStream lexStream) {
		Node ast = ExpressionParsing.parse(lexStream, new State(true, true, true));
		if (lexStream.current() != null) {
			throw new BadSyntaxException("could not parse tokens " + lexStream.current(), lexStream.getRawContent());
		}
		return ast;
	}

}
