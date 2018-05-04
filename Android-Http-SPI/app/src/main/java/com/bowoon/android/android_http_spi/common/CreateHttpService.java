package com.bowoon.android.android_http_spi.common;

import com.bowoon.android.android_http_spi.retrofit.UseRetrofit;
import com.bowoon.android.android_http_spi.volley.HttpServiceList;

public interface CreateHttpService {
    HttpServiceList newVolleyService();
    UseRetrofit newRetrofitService();
}
