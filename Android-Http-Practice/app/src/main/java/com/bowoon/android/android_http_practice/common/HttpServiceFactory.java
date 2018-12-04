package com.bowoon.android.android_http_practice.common;

import android.content.Context;

import com.bowoon.android.android_http_practice.retrofit.UseRetrofit;
import com.bowoon.android.android_http_practice.volley.UseVolley;

public class HttpServiceFactory {
    public static HttpService createClass(String name) throws IllegalAccessException {
        switch (name) {
            case "retrofit":
                return new UseRetrofit();
            case "volley":
                return new UseVolley();
            default:
                return null;
        }
    }

    public static HttpService createClass(String name, Context context) {
        switch (name) {
            case "retrofit":
                return new UseRetrofit();
            case "volley":
                return new UseVolley(context);
            default:
                return null;
        }
    }
}
