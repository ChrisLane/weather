package me.chrislane.weather.models;

import me.chrislane.weather.R;
import me.chrislane.weather.tasks.WeatherTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class WeatherModel {
    private double temperature, pressure, minTemperature, maxTemperature, windSpeed, windDirection;
    private int humidity, weatherID;
    private Date sunrise, sunset, date;
    private String countryCode, cityName, description, icon;

    public int getWeatherID() {
        return weatherID;
    }

    public void setWeatherID(int weatherID) {
        this.weatherID = weatherID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = new Date(date * 1000L);
    }

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
        if (cityName != null) {
            this.cityName = cityName;
        } else {
            this.cityName = "";
        }
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        if (countryCode != null) {
            this.countryCode = countryCode;
        } else {
            this.countryCode = "";
        }
    }

    public void setFromJson(JSONObject json, WeatherTask.API api) {
        try {
            JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
            switch (api) {
                case WEATHER:
                    JSONObject main = json.getJSONObject("main");
                    JSONObject wind = json.getJSONObject("wind");
                    JSONObject sys = json.getJSONObject("sys");

                    setWeatherID(weather.getInt("id"));
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

                    setDate(json.getInt("dt"));
                    setTemperature(temp.getDouble("day"));
                    setMinTemperature(temp.getDouble("min"));
                    setMaxTemperature(temp.getDouble("max"));
                    setPressure(json.getDouble("pressure"));
                    setHumidity(json.getInt("humidity"));
                    setWeatherID(weather.getInt("id"));
                    setDescription(weather.getString("description"));
                    setIcon(weather.getString("icon"));
                    setWindSpeed(json.getDouble("speed"));
                    setWindDirection(json.getDouble("deg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getIconResourceString() {
        String icon = getIcon();

        // Day Icons
        switch (icon) {
            case "01d":
                return R.string.wi_day_sunny;
            case "02d":
            case "03d":
            case "04d":
                return R.string.wi_day_cloudy;
            case "09d":
                return R.string.wi_day_rain_mix;
            case "10d":
                return R.string.wi_day_rain;
            case "11d":
                return R.string.wi_day_thunderstorm;
            case "13d":
                return R.string.wi_day_snow;
            case "50d":
                return R.string.wi_day_fog;
        }

        // Night Icons
        switch (icon) {
            case "01n":
                return R.string.wi_night_clear;
            case "02n":
            case "03n":
            case "04n":
                return R.string.wi_night_cloudy;
            case "09n":
                return R.string.wi_night_rain_mix;
            case "10n":
                return R.string.wi_night_rain;
            case "11n":
                return R.string.wi_night_thunderstorm;
            case "13n":
                return R.string.wi_night_snow;
            case "50n":
                return R.string.wi_night_fog;
        }

        return -1;
    }
}
