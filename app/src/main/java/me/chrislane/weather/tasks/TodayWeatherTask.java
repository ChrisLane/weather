package me.chrislane.weather.tasks;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import me.chrislane.weather.Constants;
import me.chrislane.weather.activities.MainActivity;
import me.chrislane.weather.models.TodayWeatherModel;
import org.json.JSONObject;

public class TodayWeatherTask extends AsyncTask<Location, String, JSONObject> {
    private final ProgressDialog progressDialog;
    private final TodayWeatherModel todayWeatherModel;
    private final MainActivity mainActivity;

    public TodayWeatherTask(MainActivity mainActivity, ProgressDialog progressDialog, TodayWeatherModel todayWeatherModel) {
        this.mainActivity = mainActivity;
        this.progressDialog = progressDialog;
        this.todayWeatherModel = todayWeatherModel;

    }

    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("TodayWeatherTask", "Task pre execute");

        progressDialog.setMessage("Getting weather...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected JSONObject doInBackground(Location... params) {
        Log.d("TodayWeatherTask", "Task background process");

        Location location = params[0];
        String url = "http://api.openweathermap.org/data/2.5/weather?" +
                "lat=" + location.getLatitude() +
                "&lon=" + location.getLongitude() +
                "&units=" + Constants.UNIT +
                "&appid=" + Constants.API_KEY;

        return JsonTask.fetchJson(url);
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        Log.d("TodayWeatherTask", "Task post execute");

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        // TODO: Update today's weather model
        todayWeatherModel.setFromJson(jsonObject);

        // TODO: Update today's weather UI
        mainActivity.updateTodayUI();
    }
}
