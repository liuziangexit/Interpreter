cmake_minimum_required(VERSION 3.3)
project(interpreter CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_FLAGS "-O3")

if ("${CMAKE_CXX_COMPILER_ID}" STREQUAL "MSVC")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /Za")
endif()

add_executable(interpreter console.cpp)
set_target_properties(interpreter PROPERTIES OUTPUT_NAME "interpreter")
set_target_properties(interpreter PROPERTIES PREFIX "")
