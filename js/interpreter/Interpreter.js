/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 5/30/2019
 * @description 公式解析器接口。本包对外的唯一接口
 */

var booleanExpressionOperator = ["!", "<", ">", "==", "<=", ">=", "?", ":"];

//计算公式
function Interpreter_eval(expressionString, variableSourceCallback) {
	for (s of booleanExpressionOperator)
		if (expressionString.includes(s))
			throw "evaluating boolean expression are not supported yet";

	var ast = ShuntingYard_compile(expressionString.replace(/\s/g, ''));
	return AstEvaluator_evaluate(ast, variableSourceCallback);
}

/**
 * @param string
 * @param source         表达式的参数源。此参数可以为null
 * @description 计算参数string中所有以{}标记的公式，并将其替换为结果
 */
function Interpreter_calculateConfig(string, source) {
	var result = String(string);
	for (; ;) {
		var begin = result.indexOf('{');
		var end = result.indexOf('}');
		if (begin == -1)
			break;
		if (begin > end)
			throw "bad expression"
		result = result.substring(0, begin) + Interpreter_eval(result.substring(begin + 1, end), source) + result.substring(end + 1, result.length);
	}
	return result;
}
