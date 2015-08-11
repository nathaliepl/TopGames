package com.test.twitch.topgames.api;

import android.net.Uri;
import android.text.TextUtils;

import com.test.twitch.topgames.model.TopGamesResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import retrofit.Callback;

/**
 * Created by nathalielima on 8/6/15.
 */
public class API {

    public static void getTopGames(Callback<TopGamesResponse> callback) {
        RestClient restClient = new RestClient();
        restClient.buildApiService().getTopGames(callback);
    }

    public static void getTopGamesNextPage(String nextPage, Callback<TopGamesResponse> listener) {
        if (TextUtils.isEmpty(nextPage)) {
            return;
        }

        Uri uri = Uri.parse(nextPage);

        RestClient restClient = new RestClient().setEndPoint(buildUriPathWithoutLastSegment(uri));
        restClient.buildApiService().executeGet(getLastSegment(uri.getPathSegments()), getQueryParameters(uri), listener);
    }

    private static String buildUriPathWithoutLastSegment(Uri uri) {
        StringBuilder path = new StringBuilder();
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments != null) {
            for (String segment : pathSegments) {
                if (segment.equals(pathSegments.get(pathSegments.size() - 1))) {
                    break;
                }

                path.append('/').append(segment);
            }

        }

        return uri.getScheme() + "://" + uri.getHost() + path.toString();
    }

    private static String getLastSegment(List<String> pathSegments) {
        return pathSegments != null && !pathSegments.isEmpty() ? pathSegments.get(pathSegments.size() - 1) : "";
    }

    public static HashMap<String, String> getQueryParameters(Uri uri) {
        HashMap<String, String> params = new HashMap<>();
        Set<String> paramNames = uri.getQueryParameterNames();
        if (paramNames != null) {
            for (String name : paramNames) {
                params.put(name, uri.getQueryParameter(name));
            }
        }

        return params;
    }

    private static Integer getOffsetQueryParamValue(Uri uri) {
        Set<String> paramNames = uri.getQueryParameterNames();
        String offset = null;

        if (paramNames != null) {
            for (String name : paramNames) {
                if (name.equals("offset")) {
                    offset = uri.getQueryParameter(name);
                }
            }
        }

        return offset != null ? Integer.parseInt(offset) : null;
    }
}
