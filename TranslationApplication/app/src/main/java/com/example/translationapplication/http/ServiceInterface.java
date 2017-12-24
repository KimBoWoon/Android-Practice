package com.example.translationapplication.http;

import android.content.Context;

import com.example.translationapplication.util.TranslationType;

public interface ServiceInterface {
    void requestPapagoAPI(Context context, TranslationType transType, final VolleyCallback callback, final String text);
}
