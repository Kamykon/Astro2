package com.example.michal.astro2.data;

import org.json.JSONObject;

/**
 * Created by Michal on 08.06.2017.
 */

public class Units implements JSONPopulator {
    private String temperature;
    private String pressure;
    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
        pressure = data.optString("pressure");
    }

    public String getTemperature() {
        return temperature;
    }

    public String getPressure() {
        return pressure;
    }
}
