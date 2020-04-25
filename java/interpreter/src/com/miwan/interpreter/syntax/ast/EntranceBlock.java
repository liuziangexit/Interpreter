package com.miwan.interpreter.syntax.ast;

import java.util.List;

public class EntranceBlock extends BlockStmt {
	public EntranceBlock(List<Statement> subStmts) {
		super(subStmts);
	}
}
