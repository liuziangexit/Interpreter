/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 5/29/2019
 * @description 语法树求值算法
 */

function AstEvaluator_evaluate(ast) {
	return AstEvaluator_evaluate(ast, null);
}

function AstEvaluator_evaluate(ast, source) {
	var resultStack = [];
	for (var currentElement of ast)
		switch (currentElement.type) {
			case NodeType_NUMBER_VALUE:
				resultStack.push(currentElement.value);
				break;
			case NodeType_VARIABLE_NAME:
				resultStack.push(source(currentElement.value));
				break;
			case NodeType_ARITHMETIC_OPERATOR:
				var rhs = resultStack.pop();
				var lhs = resultStack.pop();
				resultStack.push(OperatorDefinition_arithmetic.get(currentElement.value[0]).calculation([lhs, rhs]));
				break;
			case NodeType_FUNCTION_CALL:
				var operator = OperatorDefinition_functions.get(currentElement.value);
				var arguments = [];
				for (var i = operator.number - 1; i >= 0; i--)
					arguments.unshift(resultStack.pop());
				resultStack.push(operator.calculation(arguments));
		}
	return resultStack[0];
}
