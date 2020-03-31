package com.miwan.interpreter.util;

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
