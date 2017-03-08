package me.chrislane.weather.tasks;

import android.app.ProgressDialog;
import me.chrislane.weather.activities.MainActivity;
import me.chrislane.weather.models.WeatherForecastModel;

public class TodayWeatherTask extends WeatherTask {

    public TodayWeatherTask(MainActivity mainActivity, ProgressDialog progressDialog, WeatherForecastModel weatherForecastModel) {
        super(mainActivity, progressDialog, weatherForecastModel);
    }

    @Override
    public API getAPI() {
        return API.WEATHER;
    }
}
