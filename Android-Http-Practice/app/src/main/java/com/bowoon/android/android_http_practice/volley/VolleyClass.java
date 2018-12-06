package com.bowoon.android.android_http_practice.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bowoon.android.android_http_practice.common.HttpCallback;
import com.bowoon.android.android_http_practice.common.HttpService;
import com.bowoon.android.android_http_practice.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

public class VolleyClass implements HttpService {
    private final String BASE_URL = "https://randomuser.me";

    public VolleyClass() throws IllegalAccessException {
        throw new IllegalAccessException("Need Application Context");
    }

    public VolleyClass(Context context) {
        // volley는 context가 필요함
        // request queue를 초기화하기 위해 필요
        VolleyManager.getInstance().setRequestQueue(context);
    }

    @Override
    public void getPerson(final HttpCallback callback) {
        // request 생성
        // JsonObjectRequest, StringRequest, JsonArrayRequest가 존재
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                BASE_URL + "/api/?results=10",
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("volley", response.toString());
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        Person person = gson.fromJson(response.toString(), Person.class);
                        callback.onSuccess(person);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFail();
                    }
                }
        );

        // request를 만들었으면 RequestQueue에 추가하여 데이터를 요청
        addRequestQueue(request);
    }

    private void addRequestQueue(JsonObjectRequest request) {
        try {
            VolleyManager.getInstance().getRequestQueue().add(request);
        } catch (IllegalAccessException e) {
            Log.i("IllegalAccessException", e.getMessage());
        }
    }
}
