package com.example.translationapplication.http;

import android.content.Context;

public interface ServiceInterface {
    int add(int x, int y);
    int mul(int x, int y);

    void request(Context context, String text);
}
