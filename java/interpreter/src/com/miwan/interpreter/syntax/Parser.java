package com.miwan.interpreter.syntax;

import com.miwan.interpreter.lexical.LexStream;
import com.miwan.interpreter.syntax.ast.*;
import com.miwan.interpreter.lexical.Lexeme;
import com.miwan.interpreter.syntax.impl.StatementParsing;

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
		public State(boolean shouldParseBinaryOp, boolean shouldParseCondExpr) {
			this.shouldParseBinaryOp = shouldParseBinaryOp;
			this.shouldParseCondExpr = shouldParseCondExpr;
		}

		final public boolean shouldParseBinaryOp;
		final public boolean shouldParseCondExpr;
	}

	//return Root Node
	static public Node parse(final LexStream lexStream) {
		return StatementParsing.parseMainFunc(lexStream);
	}

}
