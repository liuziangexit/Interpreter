#include <iostream>
#include <exception>

void test(bool b) {
  if (!b)
    throw std::exception();
}

int main() {  }