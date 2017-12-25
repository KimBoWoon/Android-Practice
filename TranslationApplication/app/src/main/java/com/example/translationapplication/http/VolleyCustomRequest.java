package com.example.translationapplication.http;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 보운 on 2017-12-23.
 */

public class VolleyCustomRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Response.Listener<T> listener;
    private final String text;

    public VolleyCustomRequest(String url, Class<T> clazz, final String text,
                               Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.clazz = clazz;
        this.listener = listener;
        this.text = text;
    }

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

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            Log.i("RequestForPapagoAPI", json);
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(json);
            String data = jsonElement.getAsJsonObject().get("message").getAsJsonObject().get("result").toString();

            return Response.success(
                    gson.fromJson(data, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}