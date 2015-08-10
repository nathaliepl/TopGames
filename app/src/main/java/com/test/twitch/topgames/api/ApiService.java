package com.test.twitch.topgames.api;


import com.test.twitch.topgames.model.TopGamesResponse;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by nathalielima on 8/6/15.
 */
public interface ApiService {
    @Headers({
            "Accept: application/vnd.twitchtv.v3+json"
    })
    @GET("/games/top?limit=50")
    void getTopGames(Callback<TopGamesResponse> callback);


    @Headers({
            "Accept: application/vnd.twitchtv.v3+json"
    })
    @GET("/{lastPath}")
    void executeGet(@Path("lastPath") String lastPath, @QueryMap Map<String, String> parameters, Callback<TopGamesResponse> callback);

}
