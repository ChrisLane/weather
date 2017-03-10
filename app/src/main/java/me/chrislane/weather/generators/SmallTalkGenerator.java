package me.chrislane.weather.generators;

import android.content.Context;
import android.util.Log;
import me.chrislane.weather.R;
import me.chrislane.weather.models.WeatherForecastModel;
import me.chrislane.weather.models.WeatherModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SmallTalkGenerator {

    private final WeatherForecastModel forecast;
    private final Context context;
    private final Random random;

    public SmallTalkGenerator(Context context, WeatherForecastModel forecast) {
        this.context = context;
        this.forecast = forecast;
        random = new Random();
    }

    public String getCurrentWeatherComment() {
        int weatherID = forecast.getToday().getWeatherID();
        int stringArrayID = getStringArrayID(weatherID);

        if (stringArrayID >= 0) {
            String[] smallTalk = context.getResources().getStringArray(stringArrayID);
            int randomIndex = ThreadLocalRandom.current().nextInt(smallTalk.length);

            return smallTalk[randomIndex];
        }
        return "";
    }

    public String getFutureWeatherComment() {
        if (!forecast.getFutureDays().isEmpty()) {
            int currentWeatherID = forecast.getToday().getWeatherID();
            boolean currentGood = isWeatherGood(currentWeatherID);
            DateFormat formatter = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            int count = 0;

            if (currentGood) {
                for (WeatherModel day : forecast.getFutureDays()) {
                    if (!isWeatherGood(day.getWeatherID())) {
                        if (count == 0) {
                            return "Weather looks good till later today";
                        } else if (count == 1) {
                            return "Weather looks good till tomorrow";
                        }
                        return "Weather looks good till " + formatter.format(day.getDate());
                    }
                    count++;
                }
                return "Weather looks good all week!";
            } else {
                for (WeatherModel day : forecast.getFutureDays()) {
                    if (isWeatherGood(day.getWeatherID())) {
                        if (count == 0) {
                            return "Weather's looking better later today";
                        } else if (count == 1) {
                            return "Weather's looking better tomorrow";
                        }
                        return "Weather's looking better on " + formatter.format(day.getDate());
                    }
                    count++;
                }
            }

            return "Weather doesn't look better any time soon";
        }
        return "";
    }

    public boolean isWeatherGood(int weatherId) {
        Integer integer = weatherId;
        String id = integer.toString();

        return id.matches("8\\d\\d");
    }

    public int getStringArrayID(int weatherId) {
        Integer integer = weatherId;
        String id = integer.toString();

        if (id.matches("2\\d\\d")) {
            return R.array.weather_group_2xx;
        } else if (id.matches("3\\d\\d")) {
            return R.array.weather_group_3xx;
        } else if (id.matches("5\\d\\d")) {
            return R.array.weather_group_5xx;
        } else if (id.matches("6\\d\\d")) {
            return R.array.weather_group_6xx;
        } else if (id.matches("7\\d\\d")) {
            return R.array.weather_group_7xx;
        } else if (id.matches("800")) {
            return R.array.weather_group_800;
        } else if (id.matches("80\\d")) {
            return R.array.weather_group_80x;
        } else if (id.matches("90\\d")) {
            return R.array.weather_group_90x;
        } else if (id.matches("9\\d\\d")) {
            return R.array.weather_group_9xx;
        }

        Log.e("SmallTalkGenerator", "Weather id didn't match any weather groups");
        return -1;
    }
}
