package com.example.p000.tracker;

import android.app.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.p000.tracker.C_GlobalHolder.log_in_URL;

public class RetrofitClient {

    private String BASE_URL = log_in_URL;
    private static RetrofitClient mInstance;
    private Retrofit retrofit;
    private static Service service;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if(mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public I_Api getApi() {
        return  retrofit.create(I_Api.class);
    }

}
