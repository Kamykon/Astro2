package com.example.michal.astro2.data;

import org.json.JSONObject;

/**
 * Created by Michal on 08.06.2017.
 */

public class Atmosphere implements JSONPopulator {
    private double pressure;
    private int humidity;
    private double visibility;

    public int getHumidity() {
        return humidity;
    }

    public double getVisibility() {
        return visibility;
    }

    public double getPressure() {
        return pressure;
    }

    @Override
    public void populate(JSONObject data) {
        pressure = data.optDouble("pressure");
        humidity = data.optInt("humidity");
        visibility = data.optDouble("visibility");
    }
}
