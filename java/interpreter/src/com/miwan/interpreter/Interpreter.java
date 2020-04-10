package com.miwan.interpreter;

import com.miwan.interpreter.lexical.LexStream;
import com.miwan.interpreter.lexical.Lexeme;
import com.miwan.interpreter.lexical.Scanner;
import com.miwan.interpreter.runtime.Environment;
import com.miwan.interpreter.runtime.VirtualMachine;
import com.miwan.interpreter.syntax.ast.Node;
import com.miwan.interpreter.syntax.Parser;

import java.util.List;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 5/30/2019
 * <p>
 * Interpreter对外接口
 */

public class Interpreter {

	/**
	 * 计算在Interpreter/README.txt中给出语言定义的表达式的值
	 *
	 * @param input  表达式
	 * @param source 变量源
	 * @return 计算该字符串的结果
	 * @throws InterpreterException 如果输入不符合语言定义，则某些异常可能被抛出
	 */
	static public Object eval(final String input, VariableSource source) throws InterpreterException {
		//词法分析阶段，产出词串
		List<Lexeme> scannerResult = Scanner.scan(input);
		//语法分析阶段，产出AST
		Node ast = Parser.parse(new LexStream(scannerResult, input));
		//解释执行阶段，产出结果
		return VirtualMachine.eval(ast, new Environment(source));
	}

	static public Object eval(final String input) {
		return eval(input, null);
	}

	@FunctionalInterface
	public interface VariableSource {
		Object get(String varName);
	}

}
