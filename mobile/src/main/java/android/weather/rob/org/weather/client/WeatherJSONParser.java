package android.weather.rob.org.weather.client;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.weather.rob.org.weather.listener.OnForecastDownloadComplete;
import android.weather.rob.org.weather.listener.OnWeatherDownloadComplete;
import android.weather.rob.org.weather.utility.Forecast;
import android.weather.rob.org.weather.utility.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by guillaume on 11-04-15.
 */
public class WeatherJSONParser {

    private static String mSuffix;

    /**
     * Returns a weather forecast for a given location fir the next 5 days through the listener given in parameter
     *
     * @param location containing coordinates
     * @param listener to call when the data retrieval is done
     * @param format   can be Weather.format.METRIC or Weather.format.IMPERIAL
     */
    public static void UpdateForecastDataByLocation(Location location, OnForecastDownloadComplete listener, Weather.format format) {
        //Formatting the request suffix
        if (format == Weather.format.IMPERIAL) {
            mSuffix = "lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&units=imperial&cnt=5";
        } else {
            mSuffix = "lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&units=metric&cnt=5";
        }

        //Launching the download task
        new DownloadForecastTask().execute(listener);
    }

    /**
     * Returns the current weather for a given location through the listener given in parameter
     *
     * @param location containing coordinates
     * @param listener to call when the data retrieval is done
     * @param format   can be Weather.format.METRIC or Weather.format.IMPERIAL
     */
    public static void UpdateCurrentDataByLocation(Location location, OnWeatherDownloadComplete listener, Weather.format format) {

        //Formatting the request suffix
        if (format == Weather.format.IMPERIAL) {
            mSuffix = "lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&units=imperial";
        } else {
            mSuffix = "lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&units=metric";
        }

        //Launching the download task
        new DownloadWeatherTask().execute(listener);
    }

    private static ArrayList<Forecast> getForecastWeather(String data) throws JSONException{
        JSONObject jObj = null;
        ArrayList<Forecast> forecasts = new ArrayList<>();

        jObj = new JSONObject(data);
        JSONObject jCity = jObj.getJSONObject("city");

        String city = jCity.getString("name");
        String country = jCity.getString("country");

        JSONArray jArr = jObj.getJSONArray("list"); // Here we have the forecast for every day

        for (int i = 0; i < jArr.length(); i++) {
            Forecast f = new Forecast();
            Weather w = new Weather();

            w.setmCountry(country);
            w.setmCity(city);

            JSONObject jDayForecast = jArr.getJSONObject(i);

            w.setmDate(jDayForecast.getLong("dt"));

            JSONObject jTempObj = jDayForecast.getJSONObject("temp");

            f.setmDayTemp((float) jTempObj.getDouble("day"));
            f.setmMinTemp((float) jTempObj.getDouble("min"));
            f.setmMaxTemp((float) jTempObj.getDouble("max"));
            f.setmNightTemp((float) jTempObj.getDouble("night"));
            f.setmEveTemp((float) jTempObj.getDouble("eve"));
            f.setmMorningTemp((float) jTempObj.getDouble("morn"));

            w.setmHumidity((int) jDayForecast.getDouble("humidity"));
            w.setmPressure((int) jDayForecast.getDouble("pressure"));

            JSONArray jWeatherArr = jDayForecast.getJSONArray("weather");
            JSONObject jWeatherObj = jWeatherArr.getJSONObject(0);

            w.setmId(getInt("id", jWeatherObj));
            w.setmDesc(getString("description", jWeatherObj));
            w.setmCondition(getString("main", jWeatherObj));
            w.setmIconPath(getString("icon", jWeatherObj));

            f.setWeather(w);
            forecasts.add(f);
        }
        return forecasts;
    }

    private static Weather getCurrentWeather(String data) throws JSONException {
        Weather weather = new Weather();
        JSONObject jObj = null;

        jObj = new JSONObject(data);

        JSONObject sysObj = getObject("sys", jObj);
        weather.setmCity(getString("name", jObj));
        weather.setmCountry(getString("country", sysObj));
        weather.setmSunrise(getInt("sunrise", sysObj));
        weather.setmSunset(getInt("sunset", sysObj));

        JSONArray jArr = jObj.getJSONArray("weather");

        JSONObject JSONWeather = jArr.getJSONObject(0);
        weather.setmId(getInt("id", JSONWeather));
        weather.setmDesc(getString("description", JSONWeather));
        weather.setmCondition(getString("main", JSONWeather));
        weather.setmIconPath(getString("icon", JSONWeather));

        JSONObject mainObj = getObject("main", jObj);
        weather.setmHumidity(getInt("humidity", mainObj));
        weather.setmPressure(getInt("pressure", mainObj));
        weather.setmTempMax(getFloat("temp_max", mainObj));
        weather.setmTempMin(getFloat("temp_min", mainObj));
        weather.setmTemp(getFloat("temp", mainObj));

        JSONObject wObj = getObject("wind", jObj);
        weather.setmWindSpeed(getFloat("speed", wObj));
        weather.setmWindDeg(getFloat("deg", wObj));

        JSONObject cObj = getObject("clouds", jObj);
        weather.setmCloud(getInt("all", cObj));

        JSONObject rObj = getObject("rain", jObj);
        weather.setmPrecipitations(getFloat("3h", rObj));
        return weather;
    }

    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

    private static class DownloadForecastTask extends AsyncTask<OnForecastDownloadComplete, Void, ArrayList<Forecast>> {
        private OnForecastDownloadComplete listener;

        @Override
        protected ArrayList<Forecast> doInBackground(OnForecastDownloadComplete... params) {
            ArrayList<Forecast> forecast = new ArrayList<Forecast>();
            listener = params[0];
            String data = ((new WeatherHttpClient()).getWeatherData(mSuffix, WeatherHttpClient.WeatherRequest.FORECAST));

            if (data != null) {
                try {
                    forecast = getForecastWeather(data);
                } catch (JSONException e) {
                    Log.e("JSONParser", "Weather forecast data unavailable");
                    e.printStackTrace();
                    return null;
                }
            }

            // Let's retrieve the icon
            if (forecast != null) {
                for (Forecast f : forecast) {
                    f.getWeather().setmImage((new WeatherHttpClient()).getImage(f.getWeather().getmIconPath()));
                }
            }

            return forecast;
        }


        @Override
        protected void onPostExecute(ArrayList<Forecast> forecast) {
            if (forecast != null) {
                listener.onForecastTaskCompleted(forecast);
            } else {
                listener.onForecastTaskFailed();
            }
        }
    }

    private static class DownloadWeatherTask extends AsyncTask<OnWeatherDownloadComplete, Void, Weather> {
        private OnWeatherDownloadComplete listener;

        @Override
        protected Weather doInBackground(OnWeatherDownloadComplete... params) {
            Weather weather = new Weather();
            listener = params[0];
            String data = ((new WeatherHttpClient()).getWeatherData(mSuffix, WeatherHttpClient.WeatherRequest.CURRENT));

            if (data != null) {
                try {
                    weather = getCurrentWeather(data);
                    // Let's retrieve the icon
                    weather.setmImage((new WeatherHttpClient()).getImage(weather.getmIconPath()));
                } catch (JSONException e) {
                    Log.e("JSONParser", "Weather data unavailable");
                    e.printStackTrace();
                    return null;
                }
            }

            weather.setmDateToCurrentTime();

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            if (weather != null) {
                listener.onCurrentWeatherTaskCompleted(weather);
            } else {
                listener.onCurrentWeatherTaskFailed();
            }
        }
    }
}
