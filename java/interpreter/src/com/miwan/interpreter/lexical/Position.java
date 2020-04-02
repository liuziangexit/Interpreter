package com.miwan.interpreter.lexical;

public class Position {
	public Position(int line, int col, int count) {
		this.line = line;
		this.col = col;
		this.count = count;
	}

	public Position copy() {
		return new Position(this.line, this.col, this.count);
	}

	public void nextLine(int c) {
		col = 0;
		line++;
		count += c;
	}

	public int nextColumn() {
		col++;
		count++;
		return col;
	}

	public int nextColumn(int c) {
		col += c;
		count += c;
		return col;
	}

	public int line() {
		return line;
	}

	public int col() {
		return col;
	}

	public int count() {
		return count;
	}

	@Override
	public String toString() {
		return "[line=" + line + ", col=" + col + "]";
	}

	private int line;
	private int col;
	private int count;
}
