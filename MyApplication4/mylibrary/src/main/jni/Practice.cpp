//
// Created by Null on 2018-03-31.
//

#include "com_bowoon_android_mylibrary_Practice.h"

JNIEXPORT jint JNICALL Java_com_bowoon_android_mylibrary_Practice_add
        (JNIEnv *, jclass, jint x, jint y) {
    return x + y;
}