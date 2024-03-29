package me.chrislane.weather.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.pwittchen.weathericonview.WeatherIconView;
import me.chrislane.weather.R;
import me.chrislane.weather.generators.SmallTalkGenerator;
import me.chrislane.weather.models.WeatherForecastModel;
import me.chrislane.weather.models.WeatherModel;
import me.chrislane.weather.tasks.FutureWeatherTask;
import me.chrislane.weather.tasks.TodayWeatherTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    // TODO: Extra features: Adjust theme based on current time

    private final static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private final static int MAPS_REQUEST_CODE = 5555;
    private final static String ACTIVITY = "MainActivity";

    static {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);
    }

    private LocationManager locationManager;
    private ProgressDialog progressDialog;
    private WeatherForecastModel weatherForecastModel;
    private TextView locationName, todayTemperature, todayDescription, todayWind, todayPressure,
            todayHumidity, todaySunrise, todaySunset, smallTalk;
    private WeatherIconView todayIcon;
    private boolean currentLocationFound = false;
    private SmallTalkGenerator smallTalkGenerator;
    private Location currentLocation;
    private boolean ignoreResume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialise location manager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Initialise progress dialog
        progressDialog = new ProgressDialog(this);
        // Initialise today's weather model
        weatherForecastModel = new WeatherForecastModel();
        // Initialise small talk generator
        smallTalkGenerator = new SmallTalkGenerator(this, weatherForecastModel);

        // Initialise UI elements
        locationName = (TextView) findViewById(R.id.location_name);
        todayTemperature = (TextView) findViewById(R.id.today_temperature);
        todayDescription = (TextView) findViewById(R.id.today_description);
        todayWind = (TextView) findViewById(R.id.today_wind);
        todayPressure = (TextView) findViewById(R.id.today_pressure);
        todayHumidity = (TextView) findViewById(R.id.today_humidity);
        /*todaySunrise = (TextView) findViewById(R.id.today_sunrise);
        todaySunset = (TextView) findViewById(R.id.today_sunset);*/
        smallTalk = (TextView) findViewById(R.id.small_talk);
        todayIcon = (WeatherIconView) findViewById(R.id.today_icon);

        final SearchView locationSearch = (SearchView) findViewById(R.id.location_search);

        locationSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (Geocoder.isPresent()) {
                    try {
                        Geocoder gc = new Geocoder(MainActivity.this);
                        List<Address> addresses = gc.getFromLocationName(query, 1);

                        if (addresses != null && !addresses.isEmpty()) {
                            Location location = new Location("");
                            location.setLatitude(addresses.get(0).getLatitude());
                            location.setLongitude(addresses.get(0).getLongitude());

                            new TodayWeatherTask(MainActivity.this, progressDialog, weatherForecastModel).execute(location);
                            new FutureWeatherTask(MainActivity.this, progressDialog, weatherForecastModel).execute(location);
                        } else {
                            // Inform the user their location couldn't be found
                            Toast.makeText(MainActivity.this, "Couldn't find location", 5).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this, "Requires play-services", 5).show();
                        Log.e(ACTIVITY, "Failed to fetch location. Are play-services installed? Running on an emulator?", e);
                    }
                }

                // Hide keyboard
                locationSearch.onActionViewCollapsed();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            // Don't check location again
            locationManager.removeUpdates(this);

            currentLocation = location;

            new TodayWeatherTask(this, progressDialog, weatherForecastModel).execute(location);
            new FutureWeatherTask(this, progressDialog, weatherForecastModel).execute(location);
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

        if (!ignoreResume) {
            getLocation();
        } else {
            ignoreResume = false;
        }
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
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // TODO: Do something about permission being denied (Notification)
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (MAPS_REQUEST_CODE): {
                if (resultCode == Activity.RESULT_OK) {
                    double latitude = data.getDoubleExtra("latitude", 0);
                    double longitude = data.getDoubleExtra("longitude", 0);
                    Location location = new Location("");
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);

                    new TodayWeatherTask(this, progressDialog, weatherForecastModel).execute(location);
                    new FutureWeatherTask(this, progressDialog, weatherForecastModel).execute(location);
                }
                break;
            }
        }
    }

    private void getLocation() {
        String locationProvider = LocationManager.GPS_PROVIDER;

        // Get weather for the current location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else if (locationManager.isProviderEnabled(locationProvider)) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

            // Get weather for the last known location
            if (lastKnownLocation != null && !currentLocationFound) {
                currentLocation = lastKnownLocation;

                new TodayWeatherTask(this, progressDialog, weatherForecastModel).execute(lastKnownLocation);
                new FutureWeatherTask(this, progressDialog, weatherForecastModel).execute(lastKnownLocation);
            }

            locationManager.requestLocationUpdates(locationProvider, 0, 0, this);
        }
    }

    public ArrayList<View> getDayViews() {
        ArrayList<View> result = new ArrayList<>();
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.content_list);
        int days = weatherForecastModel.getFutureDays().size();
        mainLayout.removeAllViews();

        for (int i = 0; i < days; i++) {
            View day = View.inflate(this, R.layout.weather_list_item, null);
            mainLayout.addView(day);
            result.add(day);
        }

        return result;
    }

    public void updateTodayUI() {
        WeatherModel today = weatherForecastModel.getToday();
        locationName.setText(today.getCityName() + ", " + today.getCountryCode());
        todayTemperature.setText(String.format(Locale.ENGLISH, "%1$,.1f°C", today.getTemperature()));
        todayDescription.setText(today.getDescription());
        todayWind.setText(String.format(Locale.ENGLISH, "Wind Speed: %d m/s", Math.round(today.getWindSpeed())));
        todayPressure.setText(String.format(Locale.ENGLISH, "Pressure: %d hPa", Math.round(today.getPressure())));
        todayHumidity.setText(String.format(Locale.ENGLISH, "Humidity: %d%%", today.getHumidity()));

        /*if (today.getSunrise() != null && today.getSunset() != null) {
            DateFormat formatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            todaySunrise.setText(String.format(Locale.ENGLISH, "Sunrise: %s", formatter.format(today.getSunrise())));
            todaySunset.setText(String.format(Locale.ENGLISH, "Sunset: %s", formatter.format(today.getSunset())));
        }*/

        smallTalk.setText(smallTalkGenerator.getCurrentWeatherComment());
        todayIcon.setIconResource(getString(today.getIconResourceString()));
    }

    public void updateFutureUI() {
        ArrayList<WeatherModel> daysWeather = weatherForecastModel.getFutureDays();
        ArrayList<View> dayViews = getDayViews();
        for (int i = 0; i < daysWeather.size(); i++) {
            WeatherModel dayWeather = daysWeather.get(i);
            View dayView = dayViews.get(i);

            TextView date = (TextView) dayView.findViewById(R.id.date);
            TextView description = (TextView) dayView.findViewById(R.id.description);
            TextView windSpeed = (TextView) dayView.findViewById(R.id.wind_speed);
            TextView pressure = (TextView) dayView.findViewById(R.id.pressure);
            TextView humidity = (TextView) dayView.findViewById(R.id.humidity);
            TextView temperature = (TextView) dayView.findViewById(R.id.temperature);
            WeatherIconView icon = (WeatherIconView) dayView.findViewById(R.id.icon);

            if (dayWeather.getDate() != null) {
                DateFormat formatter = new SimpleDateFormat("EEE, MMM d", Locale.ENGLISH);
                date.setText(String.format(Locale.ENGLISH, "%s", formatter.format(dayWeather.getDate())));
            }
            description.setText(dayWeather.getDescription());
            windSpeed.setText(String.format(Locale.ENGLISH, "Wind Speed: %d m/s", Math.round(dayWeather.getWindSpeed())));
            pressure.setText(String.format(Locale.ENGLISH, "Pressure: %d hPa", Math.round(dayWeather.getPressure())));
            humidity.setText(String.format(Locale.ENGLISH, "Humidity: %d%%", dayWeather.getHumidity()));
            temperature.setText(String.format(Locale.ENGLISH, "%1$,.1f°C", dayWeather.getTemperature()));
            icon.setIconResource(getString(dayWeather.getIconResourceString()));
            smallTalk.setText(smallTalkGenerator.getCurrentWeatherComment() + "\n" + smallTalkGenerator.getFutureWeatherComment());
        }
    }

    public void setCurrentLocationFound(boolean currentLocationFound) {
        this.currentLocationFound = currentLocationFound;
    }

    public void onClickCurrentLocation(View view) {
        getLocation();
    }

    public void onClickShare(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, smallTalkGenerator.getCurrentWeatherComment() + "\n" + smallTalkGenerator.getFutureWeatherComment());
        ignoreResume = true;
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    public void onClickSelectLocation(View view) {
        if (currentLocation != null) {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("latitude", currentLocation.getLatitude());
            intent.putExtra("longitude", currentLocation.getLongitude());
            ignoreResume = true;
            startActivityForResult(intent, MAPS_REQUEST_CODE);
        }
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public WeatherForecastModel getWeatherForecastModel() {
        return weatherForecastModel;
    }
}