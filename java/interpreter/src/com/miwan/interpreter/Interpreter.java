package com.miwan.interpreter;

import com.miwan.interpreter.lexical.Scanner;
import com.miwan.interpreter.runtime.VirtualMachine;
import com.miwan.interpreter.syntax.Node;
import com.miwan.interpreter.syntax.Parser;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 5/30/2019
 * <p>
 * Interpreter对外接口
 */

public class Interpreter {

	/**
	 * 文法定义：
	 * NUM(数字字面量) -> 可带小数的数字
	 * BOOL(布尔字面量) -> "true" | "false"
	 * VAR(变量标识符) -> [a-z] | [A-Z] | VARVAR
	 * OP(运算符) -> + | - | * | / | ^ | C语言里的那些逻辑运算符和比较运算符
	 * STMT -> NUM | BOOL  | VAR | FUNC | (STMT) | STMT OP STMT
	 * STMTS -> ε | STMT | STMTS, STMTS
	 * FUNC(函数调用) -> 函数名(STMTS)
	 * LANG -> STMT
	 *
	 * @param input  符合上述定义的STMT的字符串
	 * @param source 用于获得变量值的functor
	 * @return 对该字符串计算后的值。如果input不符合STMT的定义，则返回值未定义
	 * @throws Exception 如果input不符合STMT的定义，则某些异常可能被抛出
	 */
	static public Object eval(final String input, VariableSource source) {
		Node ast = Parser.parse(Scanner.scan(input));
		return VirtualMachine.eval(ast, source);
	}

	@FunctionalInterface
	public interface VariableSource {
		Object get(String varName);
	}

}
