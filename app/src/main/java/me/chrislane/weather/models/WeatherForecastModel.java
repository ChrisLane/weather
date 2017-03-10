package me.chrislane.weather.models;

import me.chrislane.weather.tasks.WeatherTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherForecastModel {
    private WeatherModel today = new WeatherModel();
    private ArrayList<WeatherModel> futureDays = new ArrayList<>();

    public WeatherModel getToday() {
        return today;
    }

    public ArrayList<WeatherModel> getFutureDays() {
        return futureDays;
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
                    for (int i = 1; i < list.length(); i++) {
                        WeatherModel day = new WeatherModel();
                        day.setCityName(city);
                        day.setCountryCode(country);
                        day.setFromJson(list.getJSONObject(i), api);
                        futureDays.add(day);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }
}
