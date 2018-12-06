package com.bowoon.android.android_http_practice.common;

import android.content.Context;

import com.bowoon.android.android_http_practice.okhttp.OkHttpClass;
import com.bowoon.android.android_http_practice.retrofit.RetrofitClass;
import com.bowoon.android.android_http_practice.volley.VolleyClass;

public class HttpServiceFactory {
    public static HttpService createClass(String name) {
        try {
            switch (name) {
                case "retrofit":
                    return new RetrofitClass();
                case "volley":
                    return new VolleyClass();
                case "okhttp":
                    return new OkHttpClass();
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static HttpService createClass(String name, Context context) {
        switch (name) {
            case "retrofit":
                return new RetrofitClass();
            case "volley":
                return new VolleyClass(context);
            case "okhttp":
                return new OkHttpClass();
            default:
                return null;
        }
    }
}
