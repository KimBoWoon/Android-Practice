package com.example.translationapplication.http;

//public class VolleyPractice {
//    public static void main(String[] args) {
//        ServiceProvider.registerDefaultProvider(new ProviderImplement());
//        ServiceProvider.registerProvider("BoWoon", new ProviderImplement());
//
//        System.out.println(ServiceProvider.newInstance().add(1, 4));
//        System.out.println(ServiceProvider.newInstance().mul(2, 2));
//        System.out.println(ServiceProvider.newInstance("BoWoon").add(5, 5));
//    }
//}

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyPractice {
    private static VolleyPractice INSTANCE;
    private RequestQueue rq;

    private VolleyPractice() {

    }

    private static class Singleton {
        private static final VolleyPractice INSTANCE = new VolleyPractice();
    }

    public static VolleyPractice getInstance() {
        return Singleton.INSTANCE;
    }

    public RequestQueue getRequestQueue(Context context) {
        if (rq == null) {
            rq = Volley.newRequestQueue(context);
        }
        return rq;
    }
}