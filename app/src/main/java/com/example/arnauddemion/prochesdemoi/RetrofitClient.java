package com.example.arnauddemion.prochesdemoi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitClient {
    private static final RetrofitClient ourInstance = new RetrofitClient();
    private static final String BASE_URL = "https://proches-de-moi.piment-noir.org/";
    private static Retrofit retrofit;
    private static Gson gson;
    private static RESTService API = null;

    private RetrofitClient() {
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        API = retrofit.create(RESTService.class);
    }

    static RetrofitClient getInstance() {
        return ourInstance;
    }

    public RESTService getAPI() {
        if (API == null) {
            API = retrofit.create(RESTService.class);
        }
        return API;
    }
}
