package me.chrislane.weather.models;

import me.chrislane.weather.tasks.WeatherTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherForecastModel {
    private WeatherModel
            today = new WeatherModel(),
            dayOne = new WeatherModel(),
            dayTwo = new WeatherModel(),
            dayThree = new WeatherModel(),
            dayFour = new WeatherModel(),
            dayFive = new WeatherModel();

    public WeatherModel getToday() {
        return today;
    }

    public WeatherModel getDayOne() {
        return dayOne;
    }

    public WeatherModel getDayTwo() {
        return dayTwo;
    }

    public WeatherModel getDayThree() {
        return dayThree;
    }

    public WeatherModel getDayFour() {
        return dayFour;
    }

    public WeatherModel getDayFive() {
        return dayFive;
    }

    public WeatherModel getDay(int i) {
        switch (i) {
            case 0:
                return dayOne;
            case 1:
                return dayTwo;
            case 2:
                return dayThree;
            case 3:
                return dayFour;
            case 4:
                return dayFive;
        }
        return null;
    }

    public void formWeatherJson(JSONObject json, WeatherTask.API api) {
        switch (api) {
            case WEATHER:
                today.setFromJson(json, api);
                break;
            case FORECAST:
                try {
                    JSONArray list = json.getJSONArray("list");
                    String city = json.getJSONObject("city").getString("name");
                    String country = json.getJSONObject("city").getString("country");
                    for (int i = 0; i < 5; i++) {
                        getDay(i).setCityName(city);
                        getDay(i).setCountryCode(country);
                        getDay(i).setFromJson(list.getJSONObject(i), api);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }
}
