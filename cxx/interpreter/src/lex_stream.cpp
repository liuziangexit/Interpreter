#include <exception>
#include <lexical/lex_stream.h>
#include <utility>

interpreter::lex_stream::lex_stream(auto &&_content)
    : content(std::forward<decltype(_content)>(_content)), current_index(0) {}
interpreter::lex_stream::lex_stream(interpreter::lex_stream &&) = default;
interpreter::lex_stream::lex_stream(const interpreter::lex_stream &) = default;

interpreter::lexeme interpreter::lex_stream::eat() {
  if (this->current_index >= this->content.size()) {
    throw std::runtime_error("out of range");
  }
  return content[current_index++];
}

interpreter::lexeme interpreter::lex_stream::current() {
  if (this->current_index >= this->content.size()) {
    throw std::runtime_error("out of range");
  }
  return content[current_index];
}

interpreter::lexeme interpreter::lex_stream::peek() { this->peek(1); }

interpreter::lexeme interpreter::lex_stream::peek(std::size_t c) {
  if (this->current_index + c >= content.size()) {
    throw std::runtime_error("out of range");
  }
  return content[current_index + c];
}

bool interpreter::lex_stream::has_next() {
  return this->current_index < content.size() - 1;
}
