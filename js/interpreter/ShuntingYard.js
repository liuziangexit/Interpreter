/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 5/28/2019
 * @description 数学公式解析前端
 * @see https://zh.wikipedia.org/zh-hans/%E8%B0%83%E5%BA%A6%E5%9C%BA%E7%AE%97%E6%B3%95
 */

// 接口↓

// 将表达式编译为语法树
// 参数infixExpression不能包含空格
function ShuntingYard_compile(infixExpression) {
	var reversedPolishExpression = [];
	var operatorStack = [];

	var currentLiteral = "";
	for (var i = 0; i < infixExpression.length; i++) {
		var currentChar = infixExpression.charAt(i);

		// 组建当前词
		if (/[A-Za-z0-9]/.test(currentChar) || currentChar == '.') {
			currentLiteral += currentChar;
			continue;
		}

		// 当前词结束
		if (OperatorDefinition_functions.has(currentLiteral)) {
			// 函数
			operatorStack.push(currentLiteral);
			currentLiteral = "";
		} else {
			// 字面量和变量
			generateNode(currentLiteral, reversedPolishExpression);
			currentLiteral = "";
		}

		// 处理负号（而不解释为减号）
		if (currentChar == '-') {
			var isNegative = true;
			if (i != 0) {
				// 只有“-”符号的前一个字符是“)”或数字或字母的时候，“-”符号才被视作减号
				var previousChar = infixExpression.charAt(i - 1);
				if (previousChar == ')' || /[A-Za-z0-9]/.test(previousChar))
					isNegative = false;// 是减号
			}
			if (isNegative) {
				// 若为负号...则处理为-1*n
				// 使用美元符作为乘号是为了让这个乘法具有最高的优先级(见美元符在OperatorDefinition.arithmetic中的定义)
				reversedPolishExpression.push(AstNodenew_AstNodeNumber(-1));
				operatorStack.push('$');
				continue;
			}
		}

		// 处理四则运算符
		if (OperatorDefinition_arithmetic.has(currentChar)) {
			// 只要存在另一个记为previousOp的操作符位于栈的顶端，并且...
			// ...如果currentChar所代表之运算符的优先级要小于或者等于previousOp的优先级，或者...
			// ...currentChar和previousOp都是^(语法中将^符号视作迭代幂次，因此是右结合的。参见OperatorDefinition.java注释中的“关于右结合的指数符号”一节)...
			// ...那么，将o2从栈的顶端弹出并且放入输出队列中(循环直到上述条件不满足为止)
			while (!operatorStack.length == 0 && OperatorDefinition_arithmetic.has(operatorStack[operatorStack.length - 1])) {
				var stackTopElementAsOperatorInfo = OperatorDefinition_arithmetic
					.get(operatorStack[operatorStack.length - 1]);
				if (stackTopElementAsOperatorInfo == null
					|| OperatorDefinition_arithmetic
						.get(currentChar).number > stackTopElementAsOperatorInfo.number
					|| (currentChar == '^' && operatorStack[operatorStack.length - 1] == '^'))
					break;
				reversedPolishExpression.push(AstNode_newAstNodeOperator(convertUsdOperator(operatorStack.pop())));
			}
			operatorStack.push(currentChar);
			continue;
		}

		// 处理逗号表达式
		if (currentChar == ',') {
			popStack(reversedPolishExpression, operatorStack, '(', false);
			continue;
		}

		// 处理括号(优先级相关)
		if (currentChar == '(') {
			operatorStack.push(currentChar);
			continue;
		}
		if (currentChar == ')') {
			popStack(reversedPolishExpression, operatorStack, '(', true);
			if (!operatorStack.length == 0 && OperatorDefinition_functions.has(operatorStack[operatorStack.length - 1]))
				reversedPolishExpression.push(AstNodenew_newAstNodeFunctionCall(operatorStack.pop()));
			continue;
		}
	}
	generateNode(currentLiteral, reversedPolishExpression);
	popStack(reversedPolishExpression, operatorStack, 0, true);

	return reversedPolishExpression;
}

// 实现相关↓

function convertUsdOperator(op) {
	if (op == '$')
		return '*';
	return op;
}

function popStack(resultSequence, operatorStack, stopFlag, shouldPopFlag) {
	while (!operatorStack.length == 0) {
		if (operatorStack[operatorStack.length - 1] == stopFlag) {
			if (shouldPopFlag)
				operatorStack.pop();
			break;
		}
		resultSequence.push(AstNode_newAstNodeOperator(convertUsdOperator(operatorStack.pop())));
	}
}

function generateNode(currentLiteral, resultSequence) {
	if (currentLiteral.length != 0) {
		var type = 2;
		//1. variable
		//2. number
		for (var i = 0; i < currentLiteral.length; i++) {
			if (currentLiteral.charAt(i).match("[A-Za-z]")) {
				type = 1;
				break;
			}
		}
		if (type == 2)
			resultSequence.push(AstNodenew_AstNodeNumber(Number(currentLiteral)));
		else if (type == 1)
			resultSequence.push(AstNodenew_newAstNodeVariableName(currentLiteral));
	}
}

