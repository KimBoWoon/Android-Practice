package com.bowoon.android.android_http_spi.volley;


import com.bowoon.android.android_http_spi.common.HttpCallback;

public interface HttpServiceList {
    void naverBlogPost(String token, byte[] bytes, final HttpCallback callback);
    void twitterPost(byte[] bytes, final HttpCallback callback);
}
