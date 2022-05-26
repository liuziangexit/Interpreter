package com.miwan.interpreter.lexical;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 12/18/2019
 * <p>
 * 词法单元
 */

public class Lexeme {
public Lexeme(String text, TokenKind kind, Position begin, Position end) {
    this.text = text;
    this.kind = kind;
    this.begin = begin.copy();
    this.end = end.copy();
  }

  @Override
      public String toString() {
    if (text.length() == 1) {
      return this.kind + " '" + this.text + "' at " + this.begin;
    } else {
      return this.kind + " \"" + this.text + "\" at " + this.begin;
    }
  }

public final String text;
public final TokenKind kind;
public final Position begin, end;
}
