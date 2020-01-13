package fire.interpreter;

import java.util.Collection;
import java.util.Stack;

import fire.interpreter.OperatorDefinition.OperatorInfo;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 5/29/2019
 * @description 语法树求值算法
 */

public class AstEvaluator {

	@FunctionalInterface
	public interface VariableSource {
		double get(String varName);
	}

	static Number evaluate(final Collection<AstNode> ast) {
		return evaluate(ast, null);
	}

	// 对语法树求值
	// 可调用对象source将被用于获取变量值
	static Number evaluate(final Collection<AstNode> ast, VariableSource source) {
		Stack<Number> resultStack = new Stack<>();
		for (AstNode currentElement : ast)
			switch (currentElement.type) {
			case INTEGER_VALUE:
				resultStack.push(TypePunning.i32d(currentElement.value));
				break;
			case FP_VALUE:
				resultStack.push(TypePunning.fp64d(currentElement.value));
				break;
			case VARIABLE_NAME:
				resultStack.push(source.get(TypePunning.strd(currentElement.value)));
				break;
			case ARITHMETIC_OPERATOR:
				Number rhs = resultStack.pop();
				Number lhs = resultStack.pop();
				resultStack.push(OperatorDefinition.arithmetic.get(currentElement.value[0]).calculation
						.calculate(new Number[] { lhs, rhs }));
				break;
			case FUNCTION_CALL:
				OperatorInfo operator = OperatorDefinition.functions.get(TypePunning.strd(currentElement.value));
				Number[] arguments = new Number[operator.number];
				for (int i = operator.number - 1; i >= 0; i--)
					arguments[i] = resultStack.pop();
				resultStack.push(operator.calculation.calculate(arguments));
			}
		return resultStack.peek();
	}

}
