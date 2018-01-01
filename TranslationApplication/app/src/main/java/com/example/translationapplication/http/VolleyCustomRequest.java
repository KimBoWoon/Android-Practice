package com.example.translationapplication.http;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.translationapplication.util.DataManager;
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
    private Gson gson = new Gson();
    private Class<T> clazz;
    private Response.Listener<T> listener;
    private String text;
    private HttpOption option;

    public VolleyCustomRequest(String url, Class<T> clazz, String text,
                               Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.clazz = clazz;
        this.listener = listener;
        this.text = text;
    }

    public VolleyCustomRequest(String url, Class<T> clazz, String text, HttpOption option,
                               Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.clazz = clazz;
        this.listener = listener;
        this.text = text;
        this.option = option;
    }

    @Override
    public String getBodyContentType() {
        if (option.getBodyContentType() != null) {
            return option.getBodyContentType();
        }
        return "application/x-www-form-urlencoded; charset=utf-8";
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (option.getHeaders() != null) {
//            Map<String, String> params = new HashMap<String, String>();
//
//            params.put("X-Naver-Client-Id", "RFK0kLnsAaftYMktF6XS");
//            params.put("X-Naver-Client-Secret", "hZ85cZ8bNp");

            return option.getHeaders();
        }
        return null;
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        if (option.getParams() != null) {
//            Map<String, String> params = new HashMap<String, String>();
//
//            params.put("source", DataManager.getInstance().getSource());
//            params.put("target", DataManager.getInstance().getTarget());
//            params.put("text", text);

            return option.getParams();
        }
        return null;
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
