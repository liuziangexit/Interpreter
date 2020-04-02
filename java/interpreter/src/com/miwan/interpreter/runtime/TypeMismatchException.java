package com.miwan.interpreter.runtime;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 4/2/2020
 * <p>
 * 遇到意料之外的表达式类型时抛出
 */

public class TypeMismatchException extends VirtualMachineException {
	public TypeMismatchException(String message) {
		super(message);
	}
}
