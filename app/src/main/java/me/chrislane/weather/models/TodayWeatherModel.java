package me.chrislane.weather.models;

public class TodayWeatherModel {
    private double temperature;

    public double getTemperature() {
        return temperature - 273.15;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
