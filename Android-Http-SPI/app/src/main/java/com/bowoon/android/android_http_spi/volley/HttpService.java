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
import com.bowoon.android.android_http_spi.util.UploadType;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpService implements HttpServiceList {
    private String makeURL(UploadType type) {
        if (type == UploadType.NAVER) {
            return "https://openapi.naver.com/blog/writePost.json";
        } else if (type == UploadType.TWITTER) {
            return "https://upload.twitter.com/1.1/media/upload.json";
        }
        return null;
    }

    private HttpOption httpOptionSetting(UploadType type, String token, byte[] bytes) {
        try {
            HttpOption option = new HttpOption();

            if (type == UploadType.NAVER) {
                option.setAuthorization("Bearer " + token);
                option.setTitle("네이버 multi-part 이미지 첨부 테스트");
                option.setContent("<font color='red'>multi-part</font>로 첨부한 글입니다. <br>  이미지 첨부 <br> <img src='#0' />");
                option.setImage(new FileDataPart("practice.gif", bytes, "image/gif"));
            } else if (type == UploadType.TWITTER) {

            }
            return option;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpOption httpOptionSetting(UploadType type, byte[] bytes) {
        try {
            HttpOption option = new HttpOption();

            if (type == UploadType.NAVER) {
//                option.setAuthorization("Bearer " + token);
                option.setTitle("네이버 multi-part 이미지 첨부 테스트");
                option.setContent("<font color='red'>multi-part</font>로 첨부한 글입니다. <br>  이미지 첨부 <br> <img src='#0' />");
                option.setImage(new FileDataPart("practice.gif", bytes, "image/gif"));
            } else if (type == UploadType.TWITTER) {

            }
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
        String url = makeURL(UploadType.NAVER);
        HttpOption option = httpOptionSetting(UploadType.NAVER, token, bytes);
//        JSONObject jsonObject = makeJSON();

        VolleyMultipartRequest request = new VolleyMultipartRequest(
                Request.Method.POST,
                url,
                option,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.i("VolleySuccess", response.toString() + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("VolleyFail", error.getMessage());
                    }
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(2000, 5, 1));

        addRequestQueue(request);
    }

    @Override
    public void twitterPost(byte[] bytes, HttpCallback callback) {
        String url = makeURL(UploadType.TWITTER);
        HttpOption option = httpOptionSetting(UploadType.TWITTER, bytes);
//        JSONObject jsonObject = makeJSON();

        VolleyMultipartRequest request = new VolleyMultipartRequest(
                Request.Method.POST,
                url,
                option,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.i("VolleySuccess", response.toString() + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("VolleyFail", error.getMessage());
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