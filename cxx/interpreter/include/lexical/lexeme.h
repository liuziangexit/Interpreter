#ifndef __INTERPRETER_LEXICAL_LEXEME_H__
#define __INTERPRETER_LEXICAL_LEXEME_H__
#include <cstdint>
#include <string>

namespace interpreter {

enum token_kind {
  NumberLiteral,  // int, float or double
  BooleanLiteral, // true or false
  Identifier,
  UnaryOperator,
  BinaryOperator,
  QMark,    // ?
  Colon,    // :
  Comma,    // ,
  Sem,      // ;
  LParen,   // (
  RParen,   // )
  LBracket, // [
  RBracket, // ]
  LCurly,   // {
  RCurly    // }
};

class text_position {
  uint32_t row;
  uint32_t col;

public:
  text_position(uint32_t row, uint32_t col);
  text_position(const text_position &);
};

struct lexeme {
  std::string text;
  token_kind kind;
  text_position position;

  lexeme(const std::string &text, token_kind kind,
         const text_position &position);
  lexeme(const lexeme &);
  lexeme(lexeme &&);
};

} // namespace interpreter

#endif