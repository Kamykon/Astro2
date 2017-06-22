package com.example.michal.astro2.service;

import com.example.michal.astro2.data.Channel;

/**
 * Created by Michal on 08.06.2017.
 */

public interface WeatherServiceCallback {
    void serviceSucces(Channel chanel);
    void serviceFailure(Exception exception);
}
