package com.miwan.interpreter.util;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 4/2/2020
 * <p>
 * 我爱指针
 */

public class Pointer<T> {
	public Pointer(T v) {
		this.v = v;
	}

	@Override
	public String toString() {
		return v.toString();
	}

	public T v;
}
