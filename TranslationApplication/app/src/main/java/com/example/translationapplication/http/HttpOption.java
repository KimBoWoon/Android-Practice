package com.example.translationapplication.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 보운 on 2018-01-01.
 */

public class HttpOption {
    private Map<String, String> headers;
    private Map<String, String> params;
    private String bodyContentType;

    public HttpOption() {
        headers = new HashMap<String, String>();
        params = new HashMap<String, String>();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getBodyContentType() {
        return bodyContentType;
    }

    public void setBodyContentType(String bodyContentType) {
        this.bodyContentType = bodyContentType;
    }

    public void setClientID(String key, String value) {
        headers.put(key, value);
    }

    public void setClientSecret(String key, String value) {
        headers.put(key, value);
    }

    public void setSource(String key, String value) {
        params.put(key, value);
    }

    public void setTarget(String key, String value) {
        params.put(key, value);
    }

    public void setText(String key, String value) {
        params.put(key, value);
    }
}
