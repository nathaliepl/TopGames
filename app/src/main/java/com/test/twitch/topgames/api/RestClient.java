package com.test.twitch.topgames.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by nathalielima on 8/6/15.
 */
public class RestClient {
    public static final String BASE_URL = "https://api.twitch.tv/kraken";
    private String defaultHost = BASE_URL;

    public RestClient() {}

    public ApiService buildApiService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("dd/MM/yyyy")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .setEndpoint(defaultHost)
                .setClient(new OkClient(new OkHttpClient()))
                .build();

        return restAdapter.create(ApiService.class);
    }


    public RestClient setEndPoint(String endPoint) {
        defaultHost = endPoint;
        return this;
    }
}
