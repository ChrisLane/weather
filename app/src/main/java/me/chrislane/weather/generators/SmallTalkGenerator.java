package me.chrislane.weather.generators;

import android.content.Context;
import android.util.Log;
import me.chrislane.weather.R;
import me.chrislane.weather.models.WeatherForecastModel;

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
        String[] smallTalk = context.getResources().getStringArray(stringArrayID);
        int randomIndex = ThreadLocalRandom.current().nextInt(smallTalk.length);

        return smallTalk[randomIndex];
    }

    public String getFutureWeatherComment() {
        throw new UnsupportedOperationException("Unimplemented method");
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
