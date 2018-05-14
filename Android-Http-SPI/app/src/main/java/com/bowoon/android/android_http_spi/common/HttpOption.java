package com.bowoon.android.android_http_spi.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 보운 on 2018-01-01.
 */

public class HttpOption {
    private Map<String, String> headers;
    private Map<String, String> params;
    private Map<String, FileDataPart> byteData;
    private String bodyContentType;

    public HttpOption() {
        headers = new HashMap<String, String>();
        params = new HashMap<String, String>();
        byteData = new HashMap<String, FileDataPart>();
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

    public Map<String, FileDataPart> getByteData() {
        return byteData;
    }

    public void setByteData(Map<String, FileDataPart> byteData) {
        this.byteData = byteData;
    }

    public String getBodyContentType() {
        return bodyContentType;
    }

    public void setBodyContentType(String bodyContentType) {
        this.bodyContentType = bodyContentType;
    }

    public void setAuthorization(String authorization) {
        setHeaders("Authorization", authorization);
    }

    public void setContentDisposition(String contentDisposition) {
        setHeaders("Content-Disposition", contentDisposition);
    }

    public void setContentTransferEncoding(String contentTransferEncoding) {
        setHeaders("Content-Transfer-Encoding", contentTransferEncoding);
    }

    public void setCategoryNo(int value) {
        setParams("categoryNo", String.valueOf(value));
    }

    public void setTitle(String value) {
        setParams("title", value);
    }

    public void setContent(String value) {
        setParams("contents", value);
    }

    public void setContentType(String value) {
        setHeaders("Content-Type", value);
    }

    public void setImage(FileDataPart value) {
        setByteData("image", value);
    }

    public void setOpen(String value) {
        setParams("options.openType", value);
    }

    public void setAcceptEncoding(String value) {
        setHeaders("Accept-Encoding", value);
    }

    public void setSecretKey(String secretKey) {
        setHeaders("secretKey", secretKey);
    }

    private void setHeaders(String key, String value) {
        headers.put(key, value);
    }

    private void setParams(String key, String value) {
        params.put(key, value);
    }

    private void setByteData(String key, FileDataPart value) {
        byteData.put(key, value);
    }
}
