package com.example.michal.astro2.data;

import org.json.JSONObject;

/**
 * Created by Michal on 08.06.2017.
 */

public class Condition implements JSONPopulator {
    private int temperature;
    private String description;

    public int getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void populate(JSONObject data) {
        temperature = data.optInt("temp");
        description = data.optString("text");

    }
}
