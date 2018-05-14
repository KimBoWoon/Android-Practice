package com.bowoon.android.android_http_spi.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bowoon.android.android_http_spi.common.FileDataPart;
import com.bowoon.android.android_http_spi.common.HttpCallback;
import com.bowoon.android.android_http_spi.common.HttpOption;
import com.bowoon.android.android_http_spi.model.BlogCategory;
import com.bowoon.android.android_http_spi.util.UrlType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class HttpService implements HttpServiceList {
    private String makeURL(UrlType type) {
        if (UrlType.CATEGORY == type) {
            return "https://openapi.naver.com/blog/listCategory.json";
        } else if (UrlType.POST == type) {
            return "https://openapi.naver.com/blog/writePost.json";
        }
        return null;
    }

    private HttpOption httpOptionSetting(String token, byte[] bytes, int categoryNo) {
        try {
            HttpOption option = new HttpOption();
            option.setAuthorization("Bearer " + token);
            option.setTitle("네이버 multi-part 이미지 첨부 테스트");
            option.setContent("<font color='red'>multi-part</font>로 첨부한 글입니다. <br>  이미지 첨부 <br> <img src='#0' />");
            option.setOpen("all");
            option.setCategoryNo(categoryNo);
            option.setImage(new FileDataPart("practice.gif", bytes, "image/gif"));

            return option;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    @Override
    public void naverBlogCategory(String token, HttpCallback callback) {
        String url = makeURL(UrlType.CATEGORY);
//        HttpOption option = httpOptionSetting(token, bytes);
//        JSONObject jsonObject = makeJSON();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("VolleySuccess", String.valueOf(response.get("message")));
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            BlogCategory category = gson.fromJson(response.getString("message"), BlogCategory.class);
                            callback.onSuccess(category);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.onSuccess(null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("VolleyFail", error.getMessage());
                        callback.onFail();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(2000, 5, 1));

        addRequestQueue(request);
    }

    public void naverBlogPost(String token, int categoryNo, byte[] bytes, final HttpCallback callback) {
        String url = makeURL(UrlType.POST);
        HttpOption option = httpOptionSetting(token, bytes, categoryNo);
//        JSONObject jsonObject = makeJSON();

        VolleyMultipartRequest request = new VolleyMultipartRequest(
                Request.Method.POST,
                url,
                option,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            Log.i("VolleySuccess", new String(response.data, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        callback.onSuccess(null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("VolleyFail", error.getMessage());
                        callback.onFail();
                    }
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(2000, 5, 1));

        addRequestQueue(request);
    }

    private void addRequestQueue(VolleyMultipartRequest request) {
        try {
            VolleyManager.getInstance().getRequestQueue().add(request);
        } catch (IllegalAccessException e) {
            Log.i("IllegalAccessException", e.getMessage());
        }
    }

    private void addRequestQueue(JsonObjectRequest request) {
        try {
            VolleyManager.getInstance().getRequestQueue().add(request);
        } catch (IllegalAccessException e) {
            Log.i("IllegalAccessException", e.getMessage());
        }
    }
}