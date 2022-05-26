cmake_minimum_required(VERSION 3.3)
project(Intepreter CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_FLAGS "-O3")

if ("${CMAKE_CXX_COMPILER_ID}" STREQUAL "MSVC")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /Za")
endif()

add_executable(Intepreter console.cpp)
set_target_properties(Intepreter PROPERTIES OUTPUT_NAME "intepreter")
set_target_properties(Intepreter PROPERTIES PREFIX "")
