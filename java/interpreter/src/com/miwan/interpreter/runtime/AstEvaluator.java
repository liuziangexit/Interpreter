package com.miwan.interpreter.runtime;

import com.miwan.interpreter.Interpreter;
import com.miwan.interpreter.syntax.AstNode;
import com.miwan.interpreter.syntax.OperatorDefinition;

import java.util.Collection;
import java.util.Stack;

import static com.miwan.interpreter.syntax.AstNode.NodeType.*;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 5/29/2019
 * <p>
 * 语法树求值
 */

public class AstEvaluator {

	public static Object evaluate(final Collection<AstNode> ast) {
		return evaluate(ast, null);
	}

	// 对语法树求值
	// 可调用对象source将被用于获取变量值
	public static Object evaluate(final Collection<AstNode> ast, Interpreter.VariableSource source) {
		//目前内部的数值表示只有三种：bool, int, double
		//因此如果有用户提供变量是其他类型，我们需要用这个wrapper来把那些类型转为上述类型
		Interpreter.VariableSource wrapper = (varName) -> {
			Object val = source.get(varName);
			if (val == null)
				return null;
			if (val instanceof Integer || val instanceof Double || val instanceof Boolean)
				return val;
			if (val instanceof Byte)
				return ((Number) val).intValue();
			if (val instanceof Short)
				return ((Number) val).intValue();
			if (val instanceof Long) {
				if ((Long) val > (long) Integer.MAX_VALUE || (Long) val < (long) Integer.MIN_VALUE)
					throw new RuntimeException(varName + " is not a valid 32 bit integer");
				return ((Number) val).intValue();
			}
			if (val instanceof Float)
				return ((Number) val).doubleValue();
			throw new RuntimeException(varName + " is neither a number nor a boolean");
		};

		//对后序表达式进行求值
		Stack<Object> resultStack = new Stack<>();
		for (AstNode currentElement : ast)
			switch (currentElement.type) {
				//遇到数值时
				case INTEGER_VALUE:
				case FP_VALUE:
				case BOOLEAN_VALUE:
					resultStack.push(currentElement.value);
					break;
				//遇到变量标识符时
				case VARIABLE_NAME:
					resultStack.push(//
							wrapper.get((String) currentElement.value));
					break;
				//遇到函数调用或运算符时
				case FUNCTION_CALL:
				case OPERATOR:
					OperatorDefinition.FunctionInfo operatorInfo;
					if (currentElement.type == OPERATOR)
						operatorInfo = OperatorDefinition.operators.get(currentElement.value);
					else
						operatorInfo = OperatorDefinition.functions.get(currentElement.value);
					Object[] arguments = new Object[operatorInfo.arg_count];
					for (int i = operatorInfo.arg_count - 1; i >= 0; i--)
						arguments[i] = resultStack.pop();
					resultStack.push(operatorInfo.calculation.calculate(arguments));
					break;
			}
		return resultStack.peek();
	}

}
