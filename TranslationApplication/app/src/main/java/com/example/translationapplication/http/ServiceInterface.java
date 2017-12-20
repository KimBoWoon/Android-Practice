package com.example.translationapplication.http;

import android.content.Context;

import com.example.translationapplication.VolleyCallback;

public interface ServiceInterface {
    void request(Context context, final String text);
    void request(Context context, final VolleyCallback callback, final String text);
}
