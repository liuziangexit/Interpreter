package com.miwan.interpreter.runtime;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 4/2/2020
 * <p>
 * 以一个或多个null为参数来调用函数时抛出
 */

public class NullArgumentException extends VirtualMachineException {
	public NullArgumentException(String message) {
		super(message);
	}
}
