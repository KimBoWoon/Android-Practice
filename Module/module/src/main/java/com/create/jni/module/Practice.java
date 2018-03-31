package com.create.jni.module;

public class Practice {
    static {
        System.loadLibrary("module");
    }

    public native static int add(int x, int y);
}
