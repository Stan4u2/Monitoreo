package com.example.monitoreo.data.remote;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RetrofitClient {

    public static String token = "";

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseURL) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    //.client(getOkHClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /*
    private static OkHttpClient getOkHClient() {
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request authorizedRequest = originalRequest.newBuilder()
                        .header("Authorization", token)
                        .build();
                return chain.proceed(authorizedRequest);
            }
        }).build();
    }
    */
}
