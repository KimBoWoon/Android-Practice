package com.bowoon.android.mylibrary;

public class Practice {
    static {
        System.loadLibrary("module");
    }

    public static native int add(int x, int y);
}
