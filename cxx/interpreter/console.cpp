#include <iostream>
#include <lexical/lexeme.h>

using namespace interpreter;

int main() {
  lexeme lex("ha", token_kind::Identifier, text_position(1, 1));
  std::cout << lex.text;
  std::cout << "test\n";
}