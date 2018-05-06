package com.bowoon.android.android_http_spi.volley;


import com.bowoon.android.android_http_spi.common.HttpCallback;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

public interface HttpServiceList {
    void naverBlogPost(String token, byte[] bytes, final HttpCallback callback);
    void googleDriveUpload(GoogleAccountCredential mCredential, final HttpCallback callback);
}
