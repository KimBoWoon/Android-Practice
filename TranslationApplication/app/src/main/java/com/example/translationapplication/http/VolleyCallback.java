package com.example.translationapplication.http;

import com.example.translationapplication.home.TranslatedModel;

/**
 * Created by 보운 on 2017-12-20.
 */

public interface VolleyCallback {
    void onSuccess(TranslatedModel result);
    void onFail();
}
