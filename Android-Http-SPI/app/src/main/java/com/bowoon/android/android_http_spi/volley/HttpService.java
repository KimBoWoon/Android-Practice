package com.bowoon.android.android_http_spi.volley;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bowoon.android.android_http_spi.common.FileDataPart;
import com.bowoon.android.android_http_spi.common.HttpCallback;
import com.bowoon.android.android_http_spi.common.HttpOption;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class HttpService implements HttpServiceList {
    private String makeURL() {
        return "https://openapi.naver.com/blog/writePost.json";
    }

    private HttpOption httpOptionSetting(String token, byte[] bytes) {
        try {
            HttpOption option = new HttpOption();
            option.setAuthorization("Bearer " + token);
            option.setTitle("네이버 multi-part 이미지 첨부 테스트");
            option.setContent("<font color='red'>multi-part</font>로 첨부한 글입니다. <br>  이미지 첨부 <br> <img src='#0' />");
            option.setOpen("all");
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

    public void naverBlogPost(String token, byte[] bytes, final HttpCallback callback) {
        String url = makeURL();
        HttpOption option = httpOptionSetting(token, bytes);
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
                        callback.onSuccess();
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
}