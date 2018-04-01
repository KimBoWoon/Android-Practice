//
// Created by Null on 2018-04-01.
//

#include "com_bowoon_android_jni_Module.h"

/*
 * Class:     com_bowoon_android_jni_Module
 * Method:    add
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_bowoon_android_jni_Module_add
        (JNIEnv *, jclass, jint x, jint y) {
    return x + y;
}