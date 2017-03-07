package me.chrislane.weather.models;

import org.json.JSONException;
import org.json.JSONObject;

public class TodayWeatherModel {
    private double temperature, pressure, minTemperature, maxTemperature, windSpeed, windDirection;
    private int sunrise, sunset, humidity;
    private String locationName, description, icon;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }

    public int getSunrise() {
        return sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setFromJson(JSONObject json) {
        try {
            setDescription(json.getJSONArray("weather").getJSONObject(0).getString("description"));
            setIcon(json.getJSONArray("weather").getJSONObject(0).getString("icon"));
            setTemperature(json.getJSONObject("main").getDouble("temp"));
            setPressure(json.getJSONObject("main").getDouble("pressure"));
            setHumidity(json.getJSONObject("main").getInt("humidity"));
            setMinTemperature(json.getJSONObject("main").getDouble("temp_min"));
            setMaxTemperature(json.getJSONObject("main").getDouble("temp_max"));
            setWindSpeed(json.getJSONObject("wind").getDouble("speed"));
            setWindDirection(json.getJSONObject("wind").getDouble("deg"));
            setSunrise(json.getJSONObject("sys").getInt("sunrise"));
            setSunset(json.getJSONObject("sys").getInt("sunset"));
            setLocationName(json.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
