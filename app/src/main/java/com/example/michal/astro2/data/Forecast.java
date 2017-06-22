package com.example.michal.astro2.data;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Michal on 13.06.2017.
 */

public class Forecast implements JSONPopulator{
    private String date;
    private String day;
    private int high;
    private int low;
    private String text;

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public int getHigh() {
        return high;
    }

    public int getLow() {
        return low;
    }

    public String getText() {
        return text;
    }

    @Override
    public void populate(JSONObject data) {
        date = data.optString("date");
        day = data.optString("day");
        high = data.optInt("high");
        low = data.optInt("low");
        text = data.optString("text");
    }
}
