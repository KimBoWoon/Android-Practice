package com.bowoon.android.android_http_spi.volley;

import android.net.Uri;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bowoon.android.android_http_spi.common.HttpCallback;
import com.bowoon.android.android_http_spi.common.HttpOption;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpService implements HttpServiceList {
    private String makeURL() {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("https");
        uri.path("randomuser.me");
        uri.appendPath("api");
        uri.appendQueryParameter("results", "10");
        Log.i("makeURL", uri.build().toString());
        return uri.build().toString();
    }

    private HttpOption httpOptionSetting() {
        HttpOption option = new HttpOption();

        option.setBodyContentType("application/json");
        option.setContentType("application/json");

        return option;
    }

    private JSONObject makeJSON() {
        JSONObject object = new JSONObject();

        try {
            object.put("data", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public void requestUser(final HttpCallback callback) {
        String url = makeURL();
        HttpOption option = httpOptionSetting();
        JSONObject jsonObject = makeJSON();

        JsonCustomRequest request = new JsonCustomRequest(
                Request.Method.GET,
                "https://randomuser.me/api/?results=10",
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("Response", response.get("results").toString());
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", "Error");
                    }
                });

//        JsonCustomRequest request = new JsonCustomRequest(
//                Request.Method.GET,
//                url,
//                option,
//                jsonObject,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            Log.i("Response", response.get("results").toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("VolleyError", "Error");
//                    }
//                });

        request.setRetryPolicy(new DefaultRetryPolicy(2000, 5, 1));

        addRequestQueue(request);
    }

    private void addRequestQueue(JsonCustomRequest request) {
        try {
            VolleyManager.getInstance().getRequestQueue().add(request);
        } catch (IllegalAccessException e) {
            Log.i("IllegalAccessException", e.getMessage());
        }
    }
}