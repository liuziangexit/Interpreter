package fire.interpreter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import fire.interpreter.AstNode.NodeType;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 5/28/2019
 * @description 数学公式解析前端
 * @see https://zh.wikipedia.org/zh-hans/%E8%B0%83%E5%BA%A6%E5%9C%BA%E7%AE%97%E6%B3%95
 */

class ShuntingYard {

	// 接口↓

	// 将表达式编译为语法树
	// 参数infixExpression不能包含空格
	static Collection<AstNode> compile(final String infixExpression) {
		ArrayList<AstNode> reversedPolishExpression = new ArrayList<>();
		Stack<Object> operatorStack = new Stack<>();

		StringBuilder currentLiteral = new StringBuilder();
		for (int i = 0; i < infixExpression.length(); i++) {
			char currentChar = infixExpression.charAt(i);

			// 组建当前词
			if (Character.isLetterOrDigit(currentChar) || currentChar == '.') {
				currentLiteral.append(currentChar);
				continue;
			}

			// 当前词结束
			if (OperatorDefinition.functions.containsKey(currentLiteral.toString())) {
				// 函数
				operatorStack.push(currentLiteral.toString());
				currentLiteral.setLength(0);
			} else {
				// 字面量和变量
				generateNode(currentLiteral, reversedPolishExpression);
			}

			// 处理负号（而不解释为减号）
			if (currentChar == '-') {
				boolean isNegative = true;
				if (i != 0) {
					// 只有“-”符号的前一个字符是“)”或数字或字母的时候，“-”符号才被视作减号
					char previousChar = infixExpression.charAt(i - 1);
					if (previousChar == ')' || Character.isLetterOrDigit(previousChar))
						isNegative = false;// 是减号
				}
				if (isNegative) {
					// 若为负号...则处理为-1*n
					// 使用美元符作为乘号是为了让这个乘法具有最高的优先级(见美元符在OperatorDefinition.arithmetic中的定义)
					reversedPolishExpression.add(new AstNode(-1));
					operatorStack.push((byte) '$');
					continue;
				}
			}

			// 处理四则运算符
			if (OperatorDefinition.arithmetic.containsKey((byte) currentChar)) {
				// 只要存在另一个记为previousOp的操作符位于栈的顶端，并且...
				// ...如果currentChar所代表之运算符的优先级要小于或者等于previousOp的优先级，或者...
				// ...currentChar和previousOp都是^(语法中将^符号视作迭代幂次，因此是右结合的。参见OperatorDefinition.java注释中的“关于右结合的指数符号”一节)...
				// ...那么，将o2从栈的顶端弹出并且放入输出队列中(循环直到上述条件不满足为止)
				while (!operatorStack.empty() && operatorStack.peek() instanceof Byte) {
					OperatorDefinition.OperatorInfo stackTopElementAsOperatorInfo = OperatorDefinition.arithmetic
							.get(operatorStack.peek());
					if (stackTopElementAsOperatorInfo == null
							|| OperatorDefinition.arithmetic
							.get((byte) currentChar).number > stackTopElementAsOperatorInfo.number
							|| (currentChar == '^' && (byte) operatorStack.peek() == '^'))
						break;
					reversedPolishExpression.add(new AstNode(convertUsdOperator((byte) operatorStack.pop())));
				}
				operatorStack.push((byte) currentChar);
				continue;
			}

			// 处理逗号表达式
			if (currentChar == ',') {
				popStack(reversedPolishExpression, operatorStack, (byte) '(', false);
				continue;
			}

			// 处理括号(优先级相关)
			if (currentChar == '(') {
				operatorStack.push((byte) currentChar);
				continue;
			}
			if (currentChar == ')') {
				popStack(reversedPolishExpression, operatorStack, (byte) '(', true);
				if (!operatorStack.isEmpty() && operatorStack.peek() instanceof String)
					reversedPolishExpression.add(new AstNode(NodeType.FUNCTION_CALL, (String) operatorStack.pop()));
				continue;
			}
		}
		generateNode(currentLiteral, reversedPolishExpression);
		popStack(reversedPolishExpression, operatorStack, (byte) 0, true);

		return reversedPolishExpression;
	}

	// 实现相关↓

	static private byte convertUsdOperator(byte op) {
		if (op == '$')
			return '*';
		return op;
	}

	static private void popStack(List<AstNode> resultSequence, Stack<Object> operatorStack, byte stopFlag,
								 boolean shouldPopFlag) {
		while (!operatorStack.empty()) {
			if ((byte) operatorStack.peek() == stopFlag) {
				if (shouldPopFlag)
					operatorStack.pop();
				break;
			}
			resultSequence.add(new AstNode(convertUsdOperator((byte) operatorStack.pop())));
		}
	}

	static private void generateNode(StringBuilder currentLiteral, List<AstNode> resultSequence) {
		if (currentLiteral.length() != 0) {
			int type = 2;
			//1. variable
			//2. integer
			//3. double
			for (int i = 0; i < currentLiteral.length(); i++) {
				if (Character.isLetter(currentLiteral.charAt(i))) {
					type = 1;
					break;
				}
				if (currentLiteral.charAt(i) == '.') {
					type = 3;
					break;
				}
			}
			if (type == 2)
				resultSequence.add(new AstNode(Integer.parseInt(currentLiteral.toString())));
			else if (type == 3)
				resultSequence.add(new AstNode(Double.parseDouble(currentLiteral.toString())));
			else if (type == 1)
				resultSequence.add(new AstNode(NodeType.VARIABLE_NAME, currentLiteral.toString()));
			currentLiteral.setLength(0);
		}
	}

}
