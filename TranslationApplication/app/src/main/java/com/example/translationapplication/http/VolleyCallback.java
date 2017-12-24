package com.example.translationapplication.http;

import com.example.translationapplication.home.MainModel;

/**
 * Created by 보운 on 2017-12-20.
 */

public interface VolleyCallback {
    void onSuccess(MainModel result);
    void onFail();
}
