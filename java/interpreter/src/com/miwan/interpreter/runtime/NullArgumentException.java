package com.miwan.interpreter.runtime;

public class NullArgumentException extends VirtualMachineException {
	public NullArgumentException(String message) {
		super(message);
	}
}
