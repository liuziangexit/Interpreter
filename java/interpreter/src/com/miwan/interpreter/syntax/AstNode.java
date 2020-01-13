package com.miwan.interpreter.syntax;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 5/29/2019
 * <p>
 * AST节点
 */

public class AstNode {

	public enum NodeType {
		FP_VALUE, // 64bit floating-point value
		INTEGER_VALUE, // 32bit signed integer value
		BOOLEAN_VALUE,// boolean value
		OPERATOR, //
		FUNCTION_CALL, //
		VARIABLE_NAME
	}

	AstNode(int arg0) {
		this.type = NodeType.INTEGER_VALUE;
		this.value = arg0;
	}

	AstNode(double arg0) {
		this.type = NodeType.FP_VALUE;
		this.value = arg0;
	}

	AstNode(boolean arg0) {
		this.type = NodeType.BOOLEAN_VALUE;
		this.value = arg0;
	}

	AstNode(NodeType type, String value) {
		if (type != NodeType.FUNCTION_CALL && type != NodeType.VARIABLE_NAME && type != NodeType.OPERATOR)
			throw new IllegalArgumentException();
		this.type = type;
		this.value = value;
	}

	public NodeType type;
	public Object value;

	// debug use only↓
	static private boolean displayFullDebugInfo = false;

	@Override
	public String toString() {
		if (displayFullDebugInfo)
			return value + "|" + this.type.name().split("_")[0];
		return value.toString();
	}

}
