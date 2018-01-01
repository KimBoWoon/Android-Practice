package com.example.translationapplication.http;

import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.translationapplication.home.TranslatedModel;
import com.example.translationapplication.util.DataManager;
import com.example.translationapplication.util.TranslationType;

public class HttpService implements ServiceInterface {
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

    private HttpOption httpOptionSetting(String text) {
        HttpOption option = new HttpOption();
        option.setBodyContentType("application/x-www-form-urlencoded; charset=utf-8");
        option.setHeaders("X-Naver-Client-Id", "RFK0kLnsAaftYMktF6XS");
        option.setHeaders("X-Naver-Client-Secret", "hZ85cZ8bNp");
        option.setParams("source", DataManager.getInstance().getSource());
        option.setParams("target", DataManager.getInstance().getTarget());
        option.setParams("text", text);

        return option;
    }

    public void requestPapagoAPI(final VolleyCallback callback, final String text) {
        String url = makeURL(DataManager.getInstance().getType());

        HttpOption option = httpOptionSetting(text);

        VolleyCustomRequest<TranslatedModel> request = new VolleyCustomRequest<>(
                url,
                TranslatedModel.class,
                text,
                option,
                new Response.Listener<TranslatedModel>() {
                    @Override
                    public void onResponse(TranslatedModel response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFail();
                    }
                });

        addRequestQueue(request);
    }

    private void addRequestQueue(VolleyCustomRequest<TranslatedModel> request) {
        VolleyManager.getInstance().getRequestQueue().add(request);
    }
}