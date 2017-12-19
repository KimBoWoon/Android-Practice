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
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyPractice {
    private static VolleyPractice instance;
    private RequestQueue rq;
    private ImageLoader il;
    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

    private VolleyPractice(Context context) {
        rq = Volley.newRequestQueue(context);
        il = new ImageLoader(rq, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static VolleyPractice getInstance(Context context) {
        if (instance == null) {
            return new VolleyPractice(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return rq;
    }

    public ImageLoader getImageLoader() {
        return il;
    }
}