package com.bowoon.android.android_http_spi.volley;


import com.bowoon.android.android_http_spi.common.HttpCallback;

public interface HttpServiceList {
    void naverBlogCategory(String token, final HttpCallback callback);
    void naverBlogPost(String token, int categoryNo, byte[] bytes, final HttpCallback callback);
}
