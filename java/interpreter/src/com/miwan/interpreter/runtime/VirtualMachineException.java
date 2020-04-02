package com.miwan.interpreter.runtime;

import com.miwan.interpreter.InterpreterException;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 4/2/2020
 * <p>
 * 虚拟机异常基类
 */

public abstract class VirtualMachineException extends InterpreterException {
	public VirtualMachineException() {
	}

	public VirtualMachineException(String message) {
		super(message);
	}

	public VirtualMachineException(String message, String rawContent) {
		super("An exception is thrown while executing AST: " + message, rawContent);
	}
}
