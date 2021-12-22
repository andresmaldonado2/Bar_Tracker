cmake_minimum_required(VERSION 3.10)
include_directories(${PROJECT_SOURCE_DIR}/src/cpp/include)
add_library(performanceMetricsLib
        SHARED
        ${PROJECT_SOURCE_DIR}/src/cpp/matrixLib.c)

target_compile_options(performanceMetricsLib
        PRIVATE
        -Ofast
        -fPIC)
