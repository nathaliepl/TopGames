package com.test.twitch.topgames.api;

import com.test.twitch.topgames.model.TopGamesResponse;

import retrofit.Callback;

/**
 * Created by nathalielima on 8/6/15.
 */
public class API {

    public static void getTopGames(Callback<TopGamesResponse> callback) {
        RestClient restClient = new RestClient();
        restClient.buildApiService().getTopGames(callback);
    }

}
