package com.miwan.interpreter.syntax.ast;

import com.miwan.interpreter.runtime.VirtualMachineException;

public class TypeMismatchException extends VirtualMachineException {
	public TypeMismatchException(String message) {
		super(message);
	}
}
