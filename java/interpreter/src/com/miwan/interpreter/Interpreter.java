package com.miwan.interpreter;

import com.miwan.interpreter.lexical.LexStream;
import com.miwan.interpreter.lexical.Lexeme;
import com.miwan.interpreter.lexical.Scanner;
import com.miwan.interpreter.runtime.Environment;
import com.miwan.interpreter.runtime.VirtualMachine;
import com.miwan.interpreter.syntax.ast.Expression;
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
	 * 表达式求值
	 *
	 * @param input  表达式
	 * @param source 变量源
	 * @return 计算该字符串的结果
	 * @throws InterpreterException 如果输入不符合语言定义，则某些异常可能被抛出
	 */
	static public Object eval(final String input, VariableSource source) throws InterpreterException {
		List<Lexeme> scannerResult = Scanner.scan(input);
		Node ast = Parser.parse(new LexStream(scannerResult, input));
		return VirtualMachine.eval((Expression) ast, new Environment(source));
	}

	static public Object eval(final String input) {
		return eval(input, null);
	}

	/**
	 * @return
	 */
	static public Object execute(String src) {
		List<Lexeme> scan = Scanner.scan(src);
		Node ast = Parser.parse(new LexStream(scan, src));
		Environment environment = new Environment(id -> null);
		ast.execute(environment);
		return environment.retrieveReturned();
	}

	@FunctionalInterface
	public interface VariableSource {
		Object get(String varName);
	}

}
