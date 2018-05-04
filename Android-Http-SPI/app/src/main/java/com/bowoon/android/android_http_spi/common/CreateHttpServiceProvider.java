package com.bowoon.android.android_http_spi.common;

import com.bowoon.android.android_http_spi.retrofit.UseRetrofit;
import com.bowoon.android.android_http_spi.volley.HttpService;
import com.bowoon.android.android_http_spi.volley.HttpServiceList;

public class CreateHttpServiceProvider implements CreateHttpService {
    @Override
    public HttpServiceList newVolleyService() {
        return new HttpService();
    }

    public UseRetrofit newRetrofitService() {
        return new UseRetrofit();
    }
}
