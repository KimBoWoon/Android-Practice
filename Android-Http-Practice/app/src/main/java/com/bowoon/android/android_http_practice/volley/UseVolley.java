package com.bowoon.android.android_http_practice.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bowoon.android.android_http_practice.common.HttpCallback;
import com.bowoon.android.android_http_practice.common.HttpService;
import com.bowoon.android.android_http_practice.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

public class UseVolley implements HttpService {
    private final String BASE_URL = "https://randomuser.me";

    public UseVolley() throws IllegalAccessException {
        throw new IllegalAccessException("Need Application Context");
    }

    public UseVolley(Context context) {
        VolleyManager.getInstance().setRequestQueue(context);
    }

    @Override
    public void getPerson(final HttpCallback callback) {
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
