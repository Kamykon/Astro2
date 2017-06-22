package com.example.michal.astro2.data;

import org.json.JSONObject;

/**
 * Created by Michal on 08.06.2017.
 */

public class Location implements JSONPopulator {
    private String city;

    public String getCity() {
        return city;
    }

    @Override
    public void populate(JSONObject data) {
        city = data.optString("city");
    }
}
