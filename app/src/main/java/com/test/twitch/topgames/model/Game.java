package com.test.twitch.topgames.model;

/**
 * Created by nathalielima on 8/6/15.
 */
public class Game {
    private String name;
    private int _id;
    private int giantbomb_id;
    private ImageSizes box;
    private ImageSizes logo;

    public String getName() {
        return name;
    }

    public int get_id() {
        return _id;
    }

    public int getGiantbomb_id() {
        return giantbomb_id;
    }

    public ImageSizes getBox() {
        return box;
    }

    public ImageSizes getLogo() {
        return logo;
    }
}
