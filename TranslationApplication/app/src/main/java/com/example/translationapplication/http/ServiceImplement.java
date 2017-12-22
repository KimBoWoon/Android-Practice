package com.example.translationapplication.http;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.translationapplication.MainModel;
import com.example.translationapplication.TranslationType;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class ServiceImplement implements ServiceInterface {
    @Override
    public void request(Context context, TranslationType transType, final VolleyCallback callback, final String text) {
        String url = makeURL(transType);

        switch (transType) {
            case NMT:
                requestNMT(context, callback, url, text);
                break;
            case SMT:
                requestSMT(context, callback, url, text);
                break;
            default:
                break;
        }
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

    private void requestSMT(Context context, final VolleyCallback callback, String url, final String text) {
        StringRequest jsonRQ = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("NetworkSuccess", response);
                        JsonParser jsonParser = new JsonParser();
                        JsonElement jsonElement = jsonParser.parse(response);
                        String data = jsonElement.getAsJsonObject().get("message").getAsJsonObject().get("result").toString();
                        MainModel obj = new Gson().fromJson(data, MainModel.class);
                        callback.onSuccess(obj);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("NetworkFailed", error.toString());
                        callback.onFail();
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("X-Naver-Client-Id", "RFK0kLnsAaftYMktF6XS");
                params.put("X-Naver-Client-Secret", "hZ85cZ8bNp");

                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("source", "en");
                params.put("target", "ko");
                params.put("text", text);

                return params;
            }
        };

        addRequestQueue(context, jsonRQ);
    }

    private void requestNMT(Context context, final VolleyCallback callback, String url, final String text) {
        StringRequest jsonRQ = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("NetworkSuccess", response);
                        JsonParser jsonParser = new JsonParser();
                        JsonElement jsonElement = jsonParser.parse(response);
                        String data = jsonElement.getAsJsonObject().get("message").getAsJsonObject().get("result").toString();
                        MainModel obj = new Gson().fromJson(data, MainModel.class);
                        callback.onSuccess(obj);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("NetworkFailed", error.toString());
                        callback.onFail();
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("X-Naver-Client-Id", "RFK0kLnsAaftYMktF6XS");
                params.put("X-Naver-Client-Secret", "hZ85cZ8bNp");

                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("source", "en");
                params.put("target", "ko");
                params.put("text", text);

                return params;
            }
        };

        addRequestQueue(context, jsonRQ);
    }

    private void addRequestQueue(Context context, StringRequest jsonRQ) {
        final RequestQueue rq = VolleyPractice.getInstance().getRequestQueue(context);

        rq.add(jsonRQ);
    }
}
