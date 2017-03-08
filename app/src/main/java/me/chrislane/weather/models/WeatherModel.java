package me.chrislane.weather.models;

import me.chrislane.weather.tasks.WeatherTask;
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
        this.description = description.substring(0, 1).toUpperCase() + description.substring(1).toLowerCase();
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

    public void setFromJson(JSONObject json, WeatherTask.API api) {
        try {
            JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
            switch (api) {
                case WEATHER:
                    JSONObject main = json.getJSONObject("main");
                    JSONObject wind = json.getJSONObject("wind");
                    JSONObject sys = json.getJSONObject("sys");

                    setDescription(weather.getString("description"));
                    setIcon(weather.getString("icon"));
                    setTemperature(main.getDouble("temp"));
                    setPressure(main.getDouble("pressure"));
                    setHumidity(main.getInt("humidity"));
                    setMinTemperature(main.getDouble("temp_min"));
                    setMaxTemperature(main.getDouble("temp_max"));
                    setWindSpeed(wind.getDouble("speed"));
                    setWindDirection(wind.getDouble("deg"));
                    setSunrise(sys.getInt("sunrise"));
                    setSunset(sys.getInt("sunset"));
                    setCountryCode(sys.getString("country"));
                    setCityName(json.getString("name"));
                    break;
                case FORECAST:
                    JSONObject temp = json.getJSONObject("temp");

                    setTemperature(temp.getDouble("day"));
                    setMinTemperature(temp.getDouble("min"));
                    setMaxTemperature(temp.getDouble("max"));
                    setPressure(json.getDouble("pressure"));
                    setHumidity(json.getInt("humidity"));
                    setDescription(weather.getString("description"));
                    setIcon(weather.getString("icon"));
                    setWindSpeed(json.getDouble("speed"));
                    setWindDirection(json.getDouble("deg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
