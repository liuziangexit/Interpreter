#ifndef __INTERPRETER_LEXICAL_LEX_STREAM_H__
#define __INTERPRETER_LEXICAL_LEX_STREAM_H__
#include "lexeme.h"
#include <cstdint>
#include <string>
#include <vector>

namespace interpreter {

class lex_stream {
  std::vector<lexeme> content;
  std::size_t current_index;

public:
  lex_stream(auto &&_content);
  lex_stream(const lex_stream &);
  lex_stream(lex_stream &&);

  lexeme eat();
  lexeme current();
  lexeme peek();
  lexeme peek(std::size_t c);
  bool has_next();

  // static boolean test(Lexeme lex, Function<Lexeme, Boolean> tester)
};

} // namespace interpreter

#endif