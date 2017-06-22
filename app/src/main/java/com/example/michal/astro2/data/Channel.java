package com.example.michal.astro2.data;

import org.json.JSONObject;

/**
 * Created by Michal on 08.06.2017.
 */

public class Channel implements JSONPopulator {
    private Item item;
    private Units units;
    private Atmosphere atmosphere;
    private Location location;
    private Wind wind;

    public JSONObject data;

    public Item getItem() {
        return item;
    }

    public Units getUnits() {
        return units;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public Location getLocation() {
        return location;
    }

    public Wind getWind() {
        return wind;
    }

    @Override
    public void populate(JSONObject data) {
        units = new Units();
        units.populate(data.optJSONObject("units"));

        item = new Item();
        item.populate(data.optJSONObject("item"));

        atmosphere = new Atmosphere();
        atmosphere.populate(data.optJSONObject("atmosphere"));

        location = new Location();
        location.populate(data.optJSONObject("location"));

        wind = new Wind();
        wind.populate(data.optJSONObject("wind"));

        this.data = data;
    }
}
