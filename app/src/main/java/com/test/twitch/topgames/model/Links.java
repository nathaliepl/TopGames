package com.test.twitch.topgames.model;

import java.io.Serializable;

/**
 * Created by nathalielima on 8/6/15.
 */
public class Links implements Serializable{
    private String self;
    private String next;

    public String getSelf() {
        return self;
    }

    public String getNext() {
        return next;
    }
}
