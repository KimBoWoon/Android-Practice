package com.example.translationapplication.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyManager {
    private RequestQueue rq;

    private VolleyManager() {

    }

    private static class Singleton {
        private static final VolleyManager INSTANCE = new VolleyManager();
    }

    public static VolleyManager getInstance() {
        return Singleton.INSTANCE;
    }

    public RequestQueue getRequestQueue(Context context) {
        if (rq == null) {
            rq = Volley.newRequestQueue(context);
        }
        return rq;
    }
}