package com.example.michal.astro2.data;

import org.json.JSONObject;

/**
 * Created by Michal on 13.06.2017.
 */

public class Wind implements JSONPopulator {
    private int speed;
    private int direction;

    public int getSpeed() {
        return speed;
    }

    public int getDirection() {
        return direction;
    }

    @Override
    public void populate(JSONObject data) {
        speed = data.optInt("speed");
        direction = data.optInt("direction");
    }
}
