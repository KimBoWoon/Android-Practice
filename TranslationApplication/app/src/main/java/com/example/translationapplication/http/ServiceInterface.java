package com.example.translationapplication.http;

import android.content.Context;

public interface ServiceInterface {
    void request(Context context, final VolleyCallback callback, final String text);
}
