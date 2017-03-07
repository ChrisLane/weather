package me.chrislane.weather.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import me.chrislane.weather.R;
import me.chrislane.weather.models.TodayWeatherModel;
import me.chrislane.weather.tasks.TodayWeatherTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private final static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private LocationManager locationManager;
    private ProgressDialog progressDialog;
    private TodayWeatherModel todayWeatherModel;
    private TextView locationName, todayTemperature, todayDescription, todayWind, todayPressure,
            todayHumidity, todaySunrise, todaySunset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialise location manager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Initialise progress dialog
        progressDialog = new ProgressDialog(this);
        // Initialise today's weather model
        todayWeatherModel = new TodayWeatherModel();

        // Initialise UI elements
        locationName = (TextView) findViewById(R.id.location);
        todayTemperature = (TextView) findViewById(R.id.today_temperature);
        todayDescription = (TextView) findViewById(R.id.today_description);
        todayWind = (TextView) findViewById(R.id.today_wind);
        todayPressure = (TextView) findViewById(R.id.today_pressure);
        todayHumidity = (TextView) findViewById(R.id.today_humidity);
        todaySunrise = (TextView) findViewById(R.id.today_sunrise);
        todaySunset = (TextView) findViewById(R.id.today_sunset);

        // Get the device's location
        getLocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            // Don't check location again
            locationManager.removeUpdates(this);

            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());

            new TodayWeatherTask(this, progressDialog, todayWeatherModel).execute(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    protected void onResume() {
        super.onResume();

        getLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Request device location again
                    getLocation();

                } else {
                    // TODO: Do something about permission being denied
                }
            }
        }
    }

    private void getLocation() {
        String locationProvider = LocationManager.GPS_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        // Get weather for the last known location
        if (lastKnownLocation != null) {
            new TodayWeatherTask(this, progressDialog, todayWeatherModel).execute(lastKnownLocation);
        }

        // Get weather for the current location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Explain permissions if permission previously denied
            // Request permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else if (locationManager.isProviderEnabled(locationProvider)) {
            locationManager.requestLocationUpdates(locationProvider, 0, 0, this);
        }
    }

    public void updateTodayUI() {
        locationName.setText(todayWeatherModel.getCityName() + ", " + todayWeatherModel.getCountryCode());
        todayTemperature.setText(String.format(Locale.ENGLISH, "%1$,.1f°C", todayWeatherModel.getTemperature()));
        todayDescription.setText(todayWeatherModel.getDescription());
        todayWind.setText(String.format(Locale.ENGLISH, "Wind Speed %d m/s", Math.round(todayWeatherModel.getWindSpeed())));
        todayPressure.setText(String.format(Locale.ENGLISH, "Pressure %d hPa", Math.round(todayWeatherModel.getPressure())));
        todayHumidity.setText(String.format(Locale.ENGLISH, "Humidity %d%%", todayWeatherModel.getHumidity()));

        if (todayWeatherModel.getSunrise() != null && todayWeatherModel.getSunset() != null) {
            DateFormat formatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            todaySunrise.setText(String.format(Locale.ENGLISH, "Sunrise %s", formatter.format(todayWeatherModel.getSunrise())));
            todaySunset.setText(String.format(Locale.ENGLISH, "Sunset %s", formatter.format(todayWeatherModel.getSunset())));
        }
    }
}