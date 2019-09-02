package fire.interpreter;

import java.util.Collection;
import java.util.function.Function;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import fire.script.IJavaScriptEngine;
import fire.script.ScriptEngineBindThreadManager;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 5/30/2019
 * @description 公式解析器接口。本包对外的唯一接口
 */

public class Interpreter {

	static private final String[] booleanExpressionOperator = new String[]{"!", "<", ">", "==", "<=", ">=", "?",
			":"};

	//计算战斗公式
	static public Object calculateBattle(String expression, IJavaScriptEngine env) {
		boolean isBooleanExpression = false;
		for (final String s : booleanExpressionOperator) {
			if (expression.contains(s)) {
				isBooleanExpression = true;
				break;
			}
		}

		if (!isBooleanExpression) {
			try {
				Collection<AstNode> ast = ShuntingYard.compile(expression.replaceAll("\\s", ""));
				return AstEvaluator.evaluate(ast, varName -> {
					Object object = env.get(varName);
					if (object == null)
						throw new RuntimeException("variable " + varName + "not found");
					return ((Number) object).doubleValue();
				});
			} catch (Exception ex) {
				ex.printStackTrace();
				return 0;
			}
		}

		// Boolean求值先用JS解释器
		// TODO:实现Boolean表达式解释器
		// TODO:快快快！
		Context ctx;
		try {
			ctx = Context.enter();
			Scriptable scope = ctx.initStandardObjects();
			ScriptEngineBindThreadManager.getHashMap().entrySet().forEach(//
					e -> scope.put(e.getKey(), scope, e.getValue()));
			return ctx.evaluateString(scope, expression, null, 1, null);
		} finally {
			Context.exit();
		}
	}

	static public Function<Double, String> ResultToInt = d -> String.valueOf(d.intValue());

	/**
	 * @param string
	 * @param source         表达式的参数源。此参数可以为null
	 * @param resultToString 通过此参数可以自定义公式结果向String的转换方式。若此参数为null，则将使用String.valueOf(double)
	 * @description 计算参数string中所有以{}标记的公式，并将其替换为结果
	 */
	static public String calculateConfig(String string, AstEvaluator.VariableSource source, Function<Double, String> resultToString) {
		StringBuilder result = new StringBuilder(string.length());
		StringBuilder expression = new StringBuilder();

		StringBuilder appendTo = result;
		for (int begin = 0; begin != string.length(); begin++) {
			char currentChar = string.charAt(begin);
			if (currentChar == '{') {
				appendTo = expression;
				continue;
			}
			if (currentChar == '}') {
				appendTo = result;
				//计算
				double r = AstEvaluator.evaluate(ShuntingYard.compile(expression.toString()), source).doubleValue();
				//计算结果附到输出中
				if (resultToString != null)
					result.append(resultToString.apply(r));
				else
					result.append(r);
				expression.setLength(0);
				continue;
			}
			appendTo.append(currentChar);
		}
		return result.toString();
	}

}
