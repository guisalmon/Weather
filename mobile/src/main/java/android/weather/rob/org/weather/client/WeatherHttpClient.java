package android.weather.rob.org.weather.client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * WeatherHttpClient is handling downloading of the current weather and of weather forecast.
 */
public class WeatherHttpClient {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
    private static final String IMG_URL = "http://openweathermap.org/img/w/";
    private static final String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";

    /**
     * Downloads the current or forecast weather for a given location and returns the raw result as
     * a String. Returns null if the download failed.
     *
     * @param location    as String with the following format "lat=XX&lon=XX".
     * @param requestType specifying if it should return the weather forecast or the current weather
     * @return a String containing the JSON answer from the server or null if something went wrong
     */
    protected String getWeatherData(String location, WeatherRequest requestType) {
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            if (requestType == WeatherRequest.CURRENT) {
                con = (HttpURLConnection) (new URL(BASE_URL + location)).openConnection();
            } else {
                con = (HttpURLConnection) (new URL(FORECAST_URL + location)).openConnection();
            }
            Log.d(getClass().getName(), con.toString());
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuilder buffer = new StringBuilder();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
                buffer.append("\r\n");
            }

            is.close();
            con.disconnect();
            return buffer.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
                Log.d(getClass().getName(), "Did not manage to close the incoming stream while downloading weather");
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
                Log.d(getClass().getName(), "Did not manage to disconnect from the API while downloading weather");
            }
        }

        return null;

    }

    /**
     * Returns a weather icon downloaded from the openweathermap api using the weather code
     *
     * @param code corresponding to the weather
     * @return a Bitmap containing the downloaded icon
     */
    protected Bitmap getImage(String code) {
        HttpURLConnection con = null;
        InputStream is = null;
        try {
            con = (HttpURLConnection) (new URL(IMG_URL + code + ".png")).openConnection();
            Log.d(getClass().getName(), con.toString());
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            is = con.getInputStream();

            return BitmapFactory.decodeStream(is);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
                Log.d(getClass().getName(), "Did not manage to close the incoming stream while downloading the icon");
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
                Log.d(getClass().getName(), "Did not manage to disconnect from the API while downloading the icon");
            }
        }

        return null;

    }

    protected enum WeatherRequest {
        CURRENT,
        FORECAST
    }
}
