/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 5/29/2019
 * @description token
 */

//AST node types↓
NodeType_NUMBER_VALUE = 2; // 64bit floating-point value
NodeType_ARITHMETIC_OPERATOR = 3;
NodeType_FUNCTION_CALL = 4;
NodeType_VARIABLE_NAME = 5;

//constructors↓

function AstNode_newAstNodeOperator(value) {
	return newAstNode(NodeType_ARITHMETIC_OPERATOR, value);
}

function AstNodenew_AstNodeNumber(value) {
	return newAstNode(NodeType_NUMBER_VALUE, value);
}

function AstNodenew_newAstNodeFunctionCall(value) {
	return newAstNode(NodeType_FUNCTION_CALL, value);
}

function AstNodenew_newAstNodeVariableName(value) {
	return newAstNode(NodeType_VARIABLE_NAME, value);
}

function newAstNode(t, v) {
	return { type: t, value: v };
}
