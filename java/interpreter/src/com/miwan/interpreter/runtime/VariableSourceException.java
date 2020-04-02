package com.miwan.interpreter.runtime;

public class VariableSourceException extends VirtualMachineException {
	public VariableSourceException(String message) {
		super(message);
	}

	public VariableSourceException(String message, String rawContent) {
		super(message, rawContent);
	}

}
