package com.practice.android.overlay.service;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by null on 11/20/17.
 */

public interface RestAPIMethod {
    @FormUrlEncoded
    @Headers({
            "X-Naver-Client-Id: RFK0kLnsAaftYMktF6XS",
            "X-Naver-Client-Secret: hZ85cZ8bNp"
    })
    @POST("v1/language/translate")
    Flowable<TranslationModel> getSMTText(@Field("source") String source,
                                          @Field("target") String target,
                                          @Field("text") String text);

    @FormUrlEncoded
    @Headers({
            "X-Naver-Client-Id: RFK0kLnsAaftYMktF6XS",
            "X-Naver-Client-Secret: hZ85cZ8bNp"
    })
    @POST("v1/papago/n2mt")
    Flowable<TranslationModel> getNMTText(@Field("source") String source,
                                          @Field("target") String target,
                                          @Field("text") String text);

//    @FormUrlEncoded
//    @Headers({
//            "X-Naver-Client-Id: RFK0kLnsAaftYMktF6XS",
//            "X-Naver-Client-Secret: hZ85cZ8bNp"
//    })
//    @POST("v1/language/translate")
//    Call<TranslationModel> getSMTText(@Field("source") String source,
//                                    @Field("target") String target,
//                                    @Field("text") String text);
//
//    @FormUrlEncoded
//    @Headers({
//            "X-Naver-Client-Id: RFK0kLnsAaftYMktF6XS",
//            "X-Naver-Client-Secret: hZ85cZ8bNp"
//    })
//    @POST("v1/papago/n2mt")
//    Call<TranslationModel> getNMTText(@Field("source") String source,
//                                        @Field("target") String target,
//                                        @Field("text") String text);
}
