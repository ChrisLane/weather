package me.chrislane.weather.tasks;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonTask {

    protected static JSONObject fetchJson(String param) {
        Log.d("JsonTask", "Task background process");

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(param);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder buffer = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
                Log.d("JsonTask Response", "> " + line);   //here u ll get whole response...... :-)

            }

            try {
                return new JSONObject(buffer.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new JSONObject();
    }
}