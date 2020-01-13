package fire.interpreter;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 5/29/2019
 * @description token
 */

class AstNode {

	enum NodeType {
		FP_VALUE, // 64bit floating-point value
		INTEGER_VALUE, // 32bit signed integer value
		ARITHMETIC_OPERATOR, //
		FUNCTION_CALL, //
		VARIABLE_NAME
	}

	AstNode(byte arg0) {
		this.type = NodeType.ARITHMETIC_OPERATOR;
		this.value = new byte[]{arg0};
	}

	AstNode(int arg0) {
		this.type = NodeType.INTEGER_VALUE;
		this.value = TypePunning.i32s(arg0);
	}

	AstNode(double arg0) {
		this.type = NodeType.FP_VALUE;
		this.value = TypePunning.fp64s(arg0);
	}

	AstNode(NodeType type, String value) {
		if (type != NodeType.FUNCTION_CALL && type != NodeType.VARIABLE_NAME)
			throw new IllegalArgumentException();
		this.type = type;
		this.value = TypePunning.strs(value);
	}

	NodeType type;
	byte[] value;

	// debug use only↓
	static private boolean displayFullDebugInfo = false;

	@Override
	public String toString() {
		String returnMe = null;
		switch (type) {
			case FUNCTION_CALL:
			case VARIABLE_NAME:
				returnMe = TypePunning.strd(this.value);
				break;
			case ARITHMETIC_OPERATOR:
				returnMe = String.valueOf((char) this.value[0]);
				break;
			case INTEGER_VALUE:
				returnMe = String.valueOf(TypePunning.i32d(this.value));
				break;
			case FP_VALUE:
				returnMe = String.valueOf(TypePunning.fp64d(this.value));
				break;
		}
		if (displayFullDebugInfo)
			return returnMe + "|" + this.type.name().split("_")[0];
		return returnMe;
	}

}
