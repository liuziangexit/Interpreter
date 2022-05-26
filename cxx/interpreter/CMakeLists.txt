cmake_minimum_required(VERSION 3.3)
project(interpreter)
enable_language(CXX)
set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED True)

if ("${CMAKE_CXX_COMPILER_ID}" STREQUAL "MSVC")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /Za")
endif ()

option(BUILD_SHARED_LIBS "Build using shared libraries" ON)

include_directories(interpreter ./include)

file(GLOB SRCS "src/*.cpp" "src/*.c")
add_library(libinterpreter ${SRCS})
set_target_properties(libinterpreter PROPERTIES OUTPUT_NAME "interpreter")
set_target_properties(libinterpreter PROPERTIES PREFIX "")

add_executable(console console.cpp)
target_link_libraries(console libinterpreter)
set_target_properties(console PROPERTIES OUTPUT_NAME "console")
set_target_properties(console PROPERTIES PREFIX "interpreter-")

