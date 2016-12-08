package com.zyp.thirdloginlib.data;

import android.util.Log;

import com.zyp.thirdloginlib.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zyp on 2016/10/11.
 */

public class ServiceManager {
    private static final String endPoint = "https://api.weixin.qq.com";

    public static <T> T createRestService(String endPoint, OkHttpClient restClient, Class<T> service) {
        Retrofit serviceAdapter = new Retrofit.Builder()
                .baseUrl(endPoint)
                .addConverterFactory(
                        GsonConverterFactory.create())
                .addCallAdapterFactory(
                        RxJavaCallAdapterFactory.create())
                .client(restClient)
                .build();

        return serviceAdapter.create(service);
    }


    public static WechatApiService getWeChatApiService() {
        WechatApiService wechatApiService = createRestService(endPoint, OkHttpConnection.getOkHttpClient(), WechatApiService.class);
        return wechatApiService;
    }
}
