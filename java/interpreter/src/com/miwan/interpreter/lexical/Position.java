package com.miwan.interpreter.lexical;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 4/2/2020
 * <p>
 * 表示代码中的一个位置
 */

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

	private int line;//行
	private int col;//该行的第几位
	private int count;//总计第几位
}
