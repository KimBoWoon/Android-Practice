//
// Created by Null on 2018-03-31.
//

#include "com_create_jni_module_Practice.h"

/*
 * Class:     com_create_jni_module_Practice
 * Method:    add
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_create_jni_module_Practice_add
        (JNIEnv *, jclass, jint x, jint y) {
    return x + y;
}