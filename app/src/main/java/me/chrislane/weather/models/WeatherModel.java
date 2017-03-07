package me.chrislane.weather.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class WeatherModel {
    private double temperature, pressure, minTemperature, maxTemperature, windSpeed, windDirection;
    private int humidity;
    private Date sunrise, sunset;
    private String countryCode, cityName, description, icon;

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

    public Date getSunrise() {
        return sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = new Date(sunrise * 1000L);
    }

    public Date getSunset() {
        return sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = new Date(sunset * 1000L);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description.substring(0,1).toUpperCase() + description.substring(1).toLowerCase();
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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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
            setCountryCode(json.getJSONObject("sys").getString("country"));
            setSunrise(json.getJSONObject("sys").getInt("sunrise"));
            setSunset(json.getJSONObject("sys").getInt("sunset"));
            setCityName(json.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
