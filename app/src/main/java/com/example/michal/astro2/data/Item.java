package com.example.michal.astro2.data;


import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Michal on 08.06.2017.
 */

public class Item implements JSONPopulator {
    private Condition condition;
    private String description;
    private double latitude;
    private double longitude;
    private String pubDate;
    //private JSONArray forecast;
    private Forecast forecast1,forecast2,forecast3,forecast4;

    public Condition getCondition() {
        return condition;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPubDate() {
        return pubDate;
    }

    public Forecast getForecast1() {
        return forecast1;
    }

    public Forecast getForecast2() {
        return forecast2;
    }

    public Forecast getForecast3() {
        return forecast3;
    }

    public Forecast getForecast4() {
        return forecast4;
    }

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
        description = data.optString("description");
        longitude = data.optDouble("long");
        latitude = data.optDouble("lat");
        pubDate = data.optString("pubDate");
        forecast1 = new Forecast();
        forecast1.populate(data.optJSONArray("forecast").optJSONObject(0));
        forecast2 = new Forecast();
        forecast2.populate(data.optJSONArray("forecast").optJSONObject(1));
        forecast3 = new Forecast();
        forecast3.populate(data.optJSONArray("forecast").optJSONObject(2));
        forecast4 = new Forecast();
        forecast4.populate(data.optJSONArray("forecast").optJSONObject(3));
    }
}
