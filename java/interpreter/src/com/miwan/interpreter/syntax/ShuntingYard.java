package com.miwan.interpreter.syntax;

import com.miwan.interpreter.lexical.Lexeme;
import com.miwan.interpreter.lexical.Token;
import com.miwan.interpreter.lexical.TokenFlag;
import com.miwan.interpreter.lexical.TokenKind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 5/28/2019
 * <p>
 * 词法分析和语法分析的临时实现
 * TODO 准备重写
 */

public class ShuntingYard {

	// 接口↓

	//从词序列建立AST
	public static Collection<AstNode> compile(final List<Lexeme> lexemes) {
		final ArrayList<AstNode> reversedPolishExpression = new ArrayList<>();

		final Stack<Lexeme> operatorStack = new Stack<>();

		for (int i = 0; i < lexemes.size(); i++) {
			Lexeme currentLex = lexemes.get(i);

			//对于标识符(Identifier)
			if (currentLex.kind == TokenKind.Identifier) {
				if (OperatorDefinition.functions.containsKey(currentLex.text)) {
					//函数调用
					operatorStack.push(currentLex);
				} else {
					//变量
					reversedPolishExpression.add(//
							new AstNode(AstNode.NodeType.VARIABLE_NAME, currentLex.text));
				}
				continue;
			}

			//对于数字
			if (currentLex.kind == TokenKind.Number) {
				if (currentLex.text.contains(".")) {
					reversedPolishExpression.add(//
							new AstNode(Double.parseDouble(currentLex.text)));
				} else {
					reversedPolishExpression.add(//
							new AstNode(Integer.parseInt(currentLex.text)));
				}
				continue;
			}

			//对于boolean字面量
			if (currentLex.kind.is(TokenFlag.BooleanLiteral)) {
				if (currentLex.kind == TokenKind.True) {
					reversedPolishExpression.add(new AstNode(true));
				} else {
					reversedPolishExpression.add(new AstNode(false));
				}
				continue;
			}

			// 处理负号（而不解释为减号）
			if (currentLex.kind == TokenKind.Minus) {
				boolean isNegativeSymbol = true;
				if (i != 0) {
					// 只有“-”符号的前一个字符是“)”或数字或变量的时候，“-”符号才被视作减号
					Lexeme previousLex = lexemes.get(i - 1);
					if (previousLex.kind == TokenKind.RParen || previousLex.kind == TokenKind.Number || previousLex.kind == TokenKind.Identifier)
						isNegativeSymbol = false;// 是减号
				}
				if (isNegativeSymbol) {
					// 若为负号...则处理为-1*n
					// 使用美元符作为乘号是为了让这个乘法具有最高的优先级(见美元符在OperatorDefinition.operators中的定义)
					reversedPolishExpression.add(new AstNode(-1));
					operatorStack.push(Token.createLexeme("$", TokenKind.USD));
					continue;
				}
			}

			//处理运算符
			//包括运算符优先级的处理
			if (currentLex.kind.is(TokenFlag.Operator)//
					&& OperatorDefinition.operators.containsKey(currentLex.text)) {
				//运算符优先级决议
				//只要存在另一个记为previousOp的运算符位于栈的顶端，并且...
				while (!operatorStack.empty()//
						&& (operatorStack.peek().kind.is(TokenFlag.Operator) || OperatorDefinition.operators.containsKey(currentLex.text))) {
					OperatorDefinition.OperatorInfo previousOpInfo = OperatorDefinition.operators
							.get(operatorStack.peek().text);
					if (previousOpInfo == null
							//...currentChar所代表之运算符的优先级要小于previousOp的优先级，或者...
							|| OperatorDefinition.operators.get(currentLex.text).precedence < previousOpInfo.precedence
							//...currentChar和previousOp不全是^(语法中将^符号视作迭代幂次，因此是右结合的，不参与此优先级决议)...
							|| (currentLex.text.equals("^") && operatorStack.peek().text.equals("^")))
						break;
					//...那么，将previousOp从栈的顶端弹出并且放入输出队列中(循环直到上述条件不满足为止)
					reversedPolishExpression.add(new AstNode(AstNode.NodeType.OPERATOR, convertUsdOperator(operatorStack.pop())));
				}
				operatorStack.push(currentLex);
				continue;
			}

			// 处理函数参数表中的逗号
			if (currentLex.kind == TokenKind.Comma) {
				popStack(reversedPolishExpression, operatorStack, "(", false);
				continue;
			}

			// 处理括号(优先级相关)
			if (currentLex.kind == TokenKind.LParen) {
				operatorStack.push(currentLex);
				continue;
			}
			if (currentLex.kind == TokenKind.RParen) {
				popStack(reversedPolishExpression, operatorStack, "(", true);
				if (!operatorStack.isEmpty() && operatorStack.peek().kind == TokenKind.Identifier)
					reversedPolishExpression.add(new AstNode(AstNode.NodeType.FUNCTION_CALL, operatorStack.pop().text));
				continue;
			}
		}
		popStack(reversedPolishExpression, operatorStack, null, true);

		return reversedPolishExpression;
	}

	// 实现相关↓

	static private String convertUsdOperator(Lexeme op) {
		if (op.kind == TokenKind.USD)
			return "*";
		return op.text;
	}

	static private void popStack(List<AstNode> resultSequence, Stack<Lexeme> operatorStack, String stopFlag,
								 boolean shouldPopFlag) {
		while (!operatorStack.empty()) {
			if (stopFlag != null && operatorStack.peek().text.equals(stopFlag)) {
				if (shouldPopFlag)
					operatorStack.pop();
				break;
			}
			Lexeme t = operatorStack.pop();
			if (t.kind.is(TokenFlag.Operator) || t.kind == TokenKind.USD) {
				resultSequence.add(new AstNode(AstNode.NodeType.OPERATOR, convertUsdOperator(t)));
			} else {
				throw new IllegalStateException();
			}
		}
	}

}
