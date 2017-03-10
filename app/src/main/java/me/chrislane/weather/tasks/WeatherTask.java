package me.chrislane.weather.tasks;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import me.chrislane.weather.Constants;
import me.chrislane.weather.activities.MainActivity;
import me.chrislane.weather.models.WeatherForecastModel;
import org.json.JSONObject;

public abstract class WeatherTask extends AsyncTask<Location, String, JSONObject> {
    private final ProgressDialog progressDialog;
    private final WeatherForecastModel weatherForecastModel;
    private final MainActivity mainActivity;

    public WeatherTask(MainActivity mainActivity, ProgressDialog progressDialog, WeatherForecastModel weatherForecastModel) {
        this.mainActivity = mainActivity;
        this.progressDialog = progressDialog;
        this.weatherForecastModel = weatherForecastModel;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("WeatherTask", "Task pre execute");

        progressDialog.setMessage("Getting weather...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected JSONObject doInBackground(Location... params) {
        Log.d("WeatherTask", "Task background process");

        Location location = params[0];
        String url = "";
        switch (getAPI()) {
            case WEATHER:
                url = "http://api.openweathermap.org/data/2.5/" +
                        "weather?" +
                        "lat=" + location.getLatitude() +
                        "&lon=" + location.getLongitude() +
                        "&units=" + Constants.UNIT +
                        "&appid=" + Constants.API_KEY;
                break;
            case FORECAST:
                url = "http://api.openweathermap.org/data/2.5/" +
                        "forecast/daily?" +
                        "lat=" + location.getLatitude() +
                        "&lon=" + location.getLongitude() +
                        "&units=" + Constants.UNIT +
                        "&appid=" + Constants.API_KEY;
        }

        return JsonTask.fetchJson(url);
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        Log.d("WeatherTask", "Task post execute");

        mainActivity.setCurrentLocationFound(true);

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        // Update today's weather model
        weatherForecastModel.formWeatherJson(jsonObject, getAPI());
    }

    public abstract API getAPI();

    public enum API {
        WEATHER, FORECAST
    }
}
