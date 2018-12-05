package com.bowoon.android.android_http_practice.common;

import android.content.Context;

import com.bowoon.android.android_http_practice.okhttp.UseOkHttp;
import com.bowoon.android.android_http_practice.retrofit.UseRetrofit;
import com.bowoon.android.android_http_practice.volley.UseVolley;

public class HttpServiceFactory {
    public static HttpService createClass(String name) {
        try {
            switch (name) {
                case "retrofit":
                    return new UseRetrofit();
                case "volley":
                    return new UseVolley();
                case "okhttp":
                    return new UseOkHttp();
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
                return new UseRetrofit();
            case "volley":
                return new UseVolley(context);
            case "okhttp":
                return new UseOkHttp();
            default:
                return null;
        }
    }
}
