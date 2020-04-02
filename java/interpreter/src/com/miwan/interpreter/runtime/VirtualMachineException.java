package com.miwan.interpreter.runtime;

import com.miwan.interpreter.InterpreterException;

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
