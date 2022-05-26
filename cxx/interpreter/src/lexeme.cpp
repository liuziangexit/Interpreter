#include <lexical/lexeme.h>

interpreter::lexeme::lexeme(const std::string &text,
                            interpreter::token_kind kind,
                            const interpreter::text_position &position)
    : text(text), kind(kind), position(position) {}

interpreter::text_position::text_position(const interpreter::text_position &) =
    default;

interpreter::text_position::text_position(uint32_t row, uint32_t col)
    : row(row), col(col) {}
