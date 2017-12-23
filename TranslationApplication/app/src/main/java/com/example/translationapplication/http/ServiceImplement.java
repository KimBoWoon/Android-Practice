package com.example.translationapplication.http;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.translationapplication.MainModel;
import com.example.translationapplication.TranslationType;

public class ServiceImplement implements ServiceInterface {
    @Override
    public void request(Context context, TranslationType transType, final VolleyCallback callback, final String text) {
        String url = makeURL(transType);

        requestPapagoAPI(context, callback, url, text);
    }

    private String makeURL(TranslationType transType) {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("https");
        uri.authority("openapi.naver.com");

        switch (transType) {
            case SMT:
                uri.path("v1/language/translate");
                break;
            case NMT:
                uri.path("v1/papago/n2mt");
                break;
            default:
                break;
        }

        return uri.build().toString();
    }

    private void requestPapagoAPI(Context context, final VolleyCallback callback, String url, final String text) {
        VolleyCustomRequest<MainModel> request = new VolleyCustomRequest<>(
                url,
                MainModel.class,
                text,
                new Response.Listener<MainModel>() {
                    @Override
                    public void onResponse(MainModel response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFail();
                    }
                });

        addRequestQueue(context, request);
    }

    private void addRequestQueue(Context context, VolleyCustomRequest<MainModel> request) {
        VolleyManager.getInstance().getRequestQueue(context).add(request);
    }
}