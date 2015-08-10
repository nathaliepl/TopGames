package com.test.twitch.topgames.model;

import java.io.Serializable;

/**
 * Created by nathalielima on 8/6/15.
 */
public class TopGame implements Serializable{
    private long viewers;
    private int channels;
    private Game game;

    public long getViewers() {
        return viewers;
    }

    public int getChannels() {
        return channels;
    }

    public Game getGame() {
        return game;
    }
}
