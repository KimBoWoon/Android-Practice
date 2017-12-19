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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class ServiceImplement implements ServiceInterface {
    @Override
    public int add(int x, int y) {
        return x + y;
    }

    @Override
    public int mul(int x, int y) {
        return x * y;
    }

    @Override
    public void request(Context context, String text) {
        final RequestQueue rq = VolleyPractice.getInstance(context).getRequestQueue();

        Uri.Builder uri = new Uri.Builder();
        uri.scheme("https");
        uri.authority("openapi.naver.com");
        uri.path("v1/language/translate");
        String url = uri.build().toString();
        Log.i("URL", url);

        StringRequest jsonRQ = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("NetworkSuccess", response);
                        JsonParser jsonParser = new JsonParser();
                        JsonElement jsonElement = jsonParser.parse(response);
                        Log.i("NetworkSuccess", jsonElement.getAsJsonObject().get("message").getAsJsonObject().get("result").toString());
                        String data = jsonElement.getAsJsonObject().get("message").getAsJsonObject().get("result").toString();
                        MainModel obj = new Gson().fromJson(data, MainModel.class);
                        Log.i("obj", obj.getTranslatedText());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("NetworkFailed", error.toString());
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
                params.put("text", "Apple");

                return params;
            }
        };

//        JsonObjectRequest jsonRQ = new JsonObjectRequest(Request.Method.POST,
//                url,
//                new JSONObject(),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            Log.i("NetworkSuccess", String.valueOf(response.get("message")));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.i("NetworkFailed", "error");
//                    }
//                }
//        ) {
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=utf-8";
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("X-Naver-Client-Id", "RFK0kLnsAaftYMktF6XS");
//                params.put("X-Naver-Client-Secret", "hZ85cZ8bNp");
//
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("source", "en");
//                params.put("target", "ko");
//                params.put("text", "Apple");
//
//                return params;
//            }
//        };
        rq.add(jsonRQ);
    }
}
