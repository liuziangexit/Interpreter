package com.miwan.interpreter.runtime;

import com.miwan.interpreter.InterpreterException;

public abstract class VirtualMachineException extends InterpreterException {
	public VirtualMachineException() {
	}

	public VirtualMachineException(String message) {
		super(System.lineSeparator() + "An exception is thrown while executing AST: " + message);
	}
}
