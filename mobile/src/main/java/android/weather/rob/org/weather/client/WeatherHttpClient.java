package android.weather.rob.org.weather.client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by guillaume on 11-04-15.
 */
public class WeatherHttpClient {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
    private static final String IMG_URL = "http://openweathermap.org/img/w/";
    private static final String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";

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
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null)
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            return buffer.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }

        return null;

    }

    /**
     * Returns a weather icon downloaded from the openweathermap api using the weather code
     * @param code corresponding to the weather
     * @return a Bitmap containing the downloaded icon
     */
    protected Bitmap getImage(String code) {
        HttpURLConnection con = null;
        InputStream is = null;
        try {
            con = (HttpURLConnection) (new URL(IMG_URL + code +".png")).openConnection();
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
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }

        return null;

    }

    protected enum WeatherRequest {
        CURRENT,
        FORECAST
    }
}
