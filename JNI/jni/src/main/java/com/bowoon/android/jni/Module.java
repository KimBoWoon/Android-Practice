package com.bowoon.android.jni;

public class Module {
    static {
        System.loadLibrary("module");
    }

    public static native int add(int x, int y);
}
