package com.example.translationapplication.http;

import android.content.Context;

import com.example.translationapplication.TranslationType;

public interface ServiceInterface {
    void request(Context context, TranslationType transType, final VolleyCallback callback, final String text);
}
