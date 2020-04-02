package com.miwan.interpreter.runtime;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 4/2/2020
 * <p>
 * VariableSource相关的异常
 */

public class VariableSourceException extends VirtualMachineException {
	public VariableSourceException(String message) {
		super(message);
	}

	public VariableSourceException(String message, String rawContent) {
		super(message, rawContent);
	}

}
