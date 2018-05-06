package com.bowoon.android.android_http_spi.volley;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bowoon.android.android_http_spi.common.FileDataPart;
import com.bowoon.android.android_http_spi.common.HttpCallback;
import com.bowoon.android.android_http_spi.common.HttpOption;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.model.File;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private HttpOption httpOptionSetting(String token, byte[] bytes) {
        try {
            HttpOption option = new HttpOption();

            option.setAuthorization("Bearer " + token);
            option.setTitle("네이버 multi-part 이미지 첨부 테스트");
            option.setContent("<font color='red'>multi-part</font>로 첨부한 글입니다. <br>  이미지 첨부 <br> <img src='#0' />");
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
//        String url = makeURL();
        HttpOption option = httpOptionSetting(token, bytes);
//        JSONObject jsonObject = makeJSON();

        VolleyMultipartRequest request = new VolleyMultipartRequest(
                Request.Method.POST,
                "https://openapi.naver.com/blog/writePost.json",
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

//        JsonCustomRequest request = new JsonCustomRequest(
//                Request.Method.POST,
//                "https://openapi.naver.com/blog/writePost.json",
//                option,
//
//                );

//        JsonCustomRequest request = new JsonCustomRequest(
//                Request.Method.GET,
//                "https://randomuser.me/api/?results=10",
//                new JSONObject(),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            Log.i("Response", response.get("results").toString());
//                            GsonBuilder gsonBuilder = new GsonBuilder();
//                            final Gson gson = gsonBuilder.create();
//                            PersonModel persons = gson.fromJson(String.valueOf(response), PersonModel.class);
//                            callback.onSuccess(persons);
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

    @Override
    public void googleDriveUpload(GoogleAccountCredential mCredential, HttpCallback callback) {
        new MakeRequestTask(mCredential).execute();
    }

    private void addRequestQueue(VolleyMultipartRequest request) {
        try {
            VolleyManager.getInstance().getRequestQueue().add(request);
        } catch (IllegalAccessException e) {
            Log.i("IllegalAccessException", e.getMessage());
        }
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, Void> {
        private com.google.api.services.drive.Drive mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.drive.Drive.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Drive API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Drive API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected Void doInBackground(Void... params) {
            try {
                getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
            }
            return null;
        }

        /**
         * Fetch a list of up to 10 file names and IDs.
         *
         * @return List of Strings describing files, or an empty list if no files
         * found.
         * @throws IOException
         */
        private void getDataFromApi() throws IOException {
            // Get a list of up to 10 files.
            List<String> fileInfo = new ArrayList<String>();

            File file1 = new File();
            file1.setName("Test");
            file1.setCreatedTime(new DateTime(System.currentTimeMillis()));
            FileContent mediaContent = new FileContent("image/jpeg", new java.io.File("/storage/sdcard0/DCIM/Camera/20113259_김보운.jpg"));

            mService.files().create(file1, mediaContent).execute();
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onCancelled() {

        }
    }
}