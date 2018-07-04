cmake_minimum_required(VERSION 3.4.1)


add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
              src/main/cpp/ffmpeg/cmdutils.c
              src/main/cpp/ffmpeg/ffmpeg.c
              src/main/cpp/ffmpeg/ffmpeg_filter.c
              src/main/cpp/ffmpeg/ffmpeg_opt.c
              src/main/cpp/native-lib.c )


add_library( ffmpeg #切记不要用libffmpeg
             SHARED
             IMPORTED )
set_target_properties( ffmpeg
                       PROPERTIES IMPORTED_LOCATION
                       ../../../../libs/${ANDROID_ABI}/libffmpeg.so)

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )


set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
include_directories(src/main/cpp)
include_directories(src/main/cpp/include)

target_link_libraries( # Specifies the target library.
                       native-lib
                       ffmpeg

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )