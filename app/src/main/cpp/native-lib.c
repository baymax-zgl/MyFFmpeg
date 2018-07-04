#include <jni.h>
#include <ffmpeg/ffmpeg.h>

JNIEXPORT jint JNICALL
Java_com_example_myapplication_MainActivity_ffmpegRun(JNIEnv *env, jobject instance,
                                                      jobjectArray cmd) {
    int argc = (*env)->GetArrayLength(env, cmd);
    char *argv[argc];

    int i;
    for (i = 0; i < argc; i++) {
        jstring js = (jstring) (*env)->GetObjectArrayElement(env, cmd, i);
        argv[i] = (char*) (*env)->GetStringUTFChars(env, js, 0);
    }
    return run(argc, argv);

}