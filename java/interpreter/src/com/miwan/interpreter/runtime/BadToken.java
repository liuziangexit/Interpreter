package com.miwan.interpreter.runtime;

public class BadToken extends VirtualMachineException {
	public BadToken(String message, String rawContent) {
		super(message, rawContent);
	}
}