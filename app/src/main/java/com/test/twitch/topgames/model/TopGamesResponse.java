package com.test.twitch.topgames.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nathalielima on 8/6/15.
 */
public class TopGamesResponse implements Serializable{
    private int _total;
    private Links _links;
    private List<TopGame> top;

    public int get_total() {
        return _total;
    }

    public Links get_links() {
        return _links;
    }

    public List<TopGame> getTop() {
        return top;
    }
}
