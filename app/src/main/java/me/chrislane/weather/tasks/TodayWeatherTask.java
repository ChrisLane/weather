package me.chrislane.weather.tasks;

import android.app.ProgressDialog;
import me.chrislane.weather.activities.MainActivity;
import me.chrislane.weather.models.WeatherForecastModel;
import org.json.JSONObject;

public class TodayWeatherTask extends WeatherTask {
    private final MainActivity mainActivity;

    public TodayWeatherTask(MainActivity mainActivity, ProgressDialog progressDialog, WeatherForecastModel weatherForecastModel) {
        super(mainActivity, progressDialog, weatherForecastModel);

        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        mainActivity.updateTodayUI();
    }

    @Override
    public API getAPI() {
        return API.WEATHER;
    }
}
