package com.technologies.mobile.free_exchange_admin.rest;

import android.util.Log;

import com.technologies.mobile.free_exchange_admin.rest.model.IpUrl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by diviator on 23.10.2016.
 */

public class RetrofitService {

    private static String LOG_TAG = "interceptor";

    private static final String DOMAIN = "http://www.obmendar.ru/";

    public static String API_BASE_URL = "http://5.61.32.217";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .readTimeout(24, TimeUnit.HOURS)
            .connectTimeout(24, TimeUnit.HOURS)
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Log.e(LOG_TAG,"URL = " + request.url().host());
                    Response response = chain.proceed(request);

                    retrofit2.Response<IpUrl> ipUrlResponse = null;

                    Log.e(LOG_TAG,"CODE = " + response.code());
                    if( response.code() != 200 ){
                        builder = getBuilder(DOMAIN);
                        ipUrlResponse = createService(RestClient.class).getIpUrl(RestClient.apiKey).execute();
                        builder = getBuilder(ipUrlResponse.body().getUrl());
                        Request newRequest = request.newBuilder()
                                .url(request.url().newBuilder().host(ipUrlResponse.body().getHost()).build()).build();
                        response = chain.proceed(newRequest);
                    }
                    return response;
                }
            });

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    private static Retrofit.Builder getBuilder(String baseUrl){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create());
    }
}
