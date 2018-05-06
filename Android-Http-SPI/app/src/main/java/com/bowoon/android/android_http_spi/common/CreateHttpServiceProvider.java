package com.bowoon.android.android_http_spi.common;

import com.bowoon.android.android_http_spi.okhttp.UseOkHttp;
import com.bowoon.android.android_http_spi.retrofit.UseRetrofit;
import com.bowoon.android.android_http_spi.volley.HttpService;
import com.bowoon.android.android_http_spi.volley.HttpServiceList;

public class CreateHttpServiceProvider implements CreateHttpService {
    @Override
    public HttpServiceList newVolleyService() {
        return new HttpService();
    }

    @Override
    public UseRetrofit newRetrofitService() {
        return new UseRetrofit();
    }

    @Override
    public UseOkHttp newOkHttpService() {
        return new UseOkHttp();
    }
}
