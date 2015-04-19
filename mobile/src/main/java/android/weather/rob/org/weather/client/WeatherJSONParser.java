package android.weather.rob.org.weather.client;

import android.location.Location;
import android.os.AsyncTask;
import android.weather.rob.org.weather.listener.OnForecastDownloadListener;
import android.weather.rob.org.weather.listener.OnWeatherDownloadListener;
import android.weather.rob.org.weather.utility.Forecast;
import android.weather.rob.org.weather.utility.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * WeatherJSONParser handles the parsing of the JSON data from the server
 */
public class WeatherJSONParser {

    private static String mSuffix;

    /**
     * Returns a weather forecast for a given location for specified amount of days through the listener given in parameter
     *
     * @param numberOfDays amount of days it should retrieve data for, can't be more than 15
     * @param location containing coordinates
     * @param listener to call when the data retrieval is done
     * @param format   can be Weather.format.METRIC or Weather.format.IMPERIAL
     */
    public static void UpdateForecastDataByLocation(Location location, OnForecastDownloadListener listener, Weather.format format, int numberOfDays) {
        //Formatting the request suffix
        if (format == Weather.format.IMPERIAL) {
            mSuffix = "lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&units=imperial&cnt=" + numberOfDays;
        } else {
            mSuffix = "lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&units=metric&cnt=" + numberOfDays;
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
    public static void UpdateCurrentDataByLocation(Location location, OnWeatherDownloadListener listener, Weather.format format) {

        //Formatting the request suffix
        if (format == Weather.format.IMPERIAL) {
            mSuffix = "lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&units=imperial";
        } else {
            mSuffix = "lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&units=metric";
        }

        //Launching the download task
        new DownloadWeatherTask().execute(listener);
    }

    /**
     * Returns a weather forecast for a given city for specified amount of days through the listener given in parameter
     *
     * @param numberOfDays amount of days it should retrieve data for, can't be more than 15
     * @param cityName name of the city we want a forecast for
     * @param listener to call when the data retrieval is done
     * @param format   can be Weather.format.METRIC or Weather.format.IMPERIAL
     */
    public static void UpdateForecastDataByCityName(String cityName, OnForecastDownloadListener listener, Weather.format format, int numberOfDays) {
        //Formatting the request suffix
        if (format == Weather.format.IMPERIAL) {
            mSuffix = "q=" + cityName + "&units=imperial&cnt=" + numberOfDays;
        } else {
            mSuffix = "q=" + cityName + "&units=metric&cnt=" + numberOfDays;
        }

        //Launching the download task
        new DownloadForecastTask().execute(listener);
    }

    /**
     * Returns the current weather for a given city through the listener given in parameter
     *
     * @param cityName name of the city
     * @param listener to call when the data retrieval is done
     * @param format   can be Weather.format.METRIC or Weather.format.IMPERIAL
     */

    public static void UpdateCurrentDataByCityName(String cityName, OnWeatherDownloadListener listener, Weather.format format) {

        //Formatting the request suffix
        if (format == Weather.format.IMPERIAL) {
            mSuffix = "q=" + cityName + "&units=imperial";
        } else {
            mSuffix = "q=" + cityName + "&units=metric";
        }

        //Launching the download task
        new DownloadWeatherTask().execute(listener);
    }

    private static ArrayList<Forecast> getForecastWeather(String data) throws JSONException {
        JSONObject jObj;
        ArrayList<Forecast> forecasts = new ArrayList<>();

        jObj = new JSONObject(data);
        JSONObject jCity = jObj.getJSONObject("city");

        String city = jCity.getString("name");
        String country = jCity.getString("country");

        JSONArray jArr = jObj.getJSONArray("list"); // Here we have the forecast for every day

        //Skip the first object to start on the next day and not on the current one
        for (int i = 1; i < jArr.length(); i++) {
            Forecast f = new Forecast();
            Weather w = new Weather();

            w.setCountry(country);
            w.setCity(city);

            JSONObject jDayForecast = jArr.getJSONObject(i);

            w.setDate(jDayForecast.getLong("dt"));

            JSONObject jTempObj = jDayForecast.getJSONObject("temp");

            f.setmDayTemp((int) jTempObj.getDouble("day"));
            f.setmMinTemp((float) jTempObj.getDouble("min"));
            f.setmMaxTemp((float) jTempObj.getDouble("max"));
            f.setmNightTemp((float) jTempObj.getDouble("night"));
            f.setmEveTemp((float) jTempObj.getDouble("eve"));
            f.setmMorningTemp((float) jTempObj.getDouble("morn"));

            w.setHumidity((int) jDayForecast.getDouble("humidity"));
            w.setPressure((int) jDayForecast.getDouble("pressure"));

            JSONArray jWeatherArr = jDayForecast.getJSONArray("weather");
            JSONObject jWeatherObj = jWeatherArr.getJSONObject(0);

            w.setId(getInt("id", jWeatherObj));
            w.setDesc(getString("description", jWeatherObj));
            w.setCondition(getString("main", jWeatherObj));
            w.setIconPath(getString("icon", jWeatherObj));

            f.setWeather(w);
            forecasts.add(f);
        }
        return forecasts;
    }

    private static Weather getCurrentWeather(String data) throws JSONException {
        Weather weather = new Weather();
        JSONObject jObj;

        jObj = new JSONObject(data);

        JSONObject sysObj = getObject("sys", jObj);
        weather.setCity(getString("name", jObj));
        weather.setCountry(getString("country", sysObj));
        weather.setSunrise(getInt("sunrise", sysObj));
        weather.setSunset(getInt("sunset", sysObj));

        JSONArray jArr = jObj.getJSONArray("weather");

        JSONObject JSONWeather = jArr.getJSONObject(0);
        weather.setId(getInt("id", JSONWeather));
        weather.setDesc(getString("description", JSONWeather));
        weather.setCondition(getString("main", JSONWeather));
        weather.setIconPath(getString("icon", JSONWeather));

        JSONObject mainObj = getObject("main", jObj);
        weather.setHumidity(getInt("humidity", mainObj));
        weather.setPressure(getInt("pressure", mainObj));
        weather.setTempMax(getFloat("temp_max", mainObj));
        weather.setTempMin(getFloat("temp_min", mainObj));
        weather.setTemp(getInt("temp", mainObj));

        JSONObject wObj = getObject("wind", jObj);
        weather.setWindSpeed(getFloat("speed", wObj));
        weather.setWindDeg(getFloat("deg", wObj));

        JSONObject cObj = getObject("clouds", jObj);
        weather.setCloud(getInt("all", cObj));

        // When no rainfall has been recorded, instead of sending 0mm as a value the api doesn't
        // send any rain JSON object, the try catch block is a fix for this case.
        try {
            JSONObject rObj = getObject("rain", jObj);
            weather.setPrecipitations(getFloat("3h", rObj));
        } catch (JSONException e) {
            weather.setPrecipitations(0);
        }

        return weather;
    }

    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getJSONObject(tagName);
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

    private static class DownloadForecastTask extends AsyncTask<OnForecastDownloadListener, Void, ArrayList<Forecast>> {
        private OnForecastDownloadListener listener;

        @Override
        protected ArrayList<Forecast> doInBackground(OnForecastDownloadListener... params) {
            ArrayList<Forecast> forecast = new ArrayList<>();
            listener = params[0];
            String data = ((new WeatherHttpClient()).getWeatherData(mSuffix, WeatherHttpClient.WeatherRequest.FORECAST));

            if (data != null) {
                try {
                    forecast = getForecastWeather(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /*
            // If you want to retrieve the weather icon from the openweathermap api, uncomment this code.

            if (forecast != null) {
                for (Forecast f : forecast) {
                    f.getWeather().setImage((new WeatherHttpClient()).getImage(f.getWeather().getIconPath()));
                }
            }
            */

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

    private static class DownloadWeatherTask extends AsyncTask<OnWeatherDownloadListener, Void, Weather> {
        private OnWeatherDownloadListener listener;

        @Override
        protected Weather doInBackground(OnWeatherDownloadListener... params) {
            Weather weather = new Weather();
            listener = params[0];
            String data = ((new WeatherHttpClient()).getWeatherData(mSuffix, WeatherHttpClient.WeatherRequest.CURRENT));

            if (data != null) {
                try {
                    weather = getCurrentWeather(data);
                    // Let's retrieve the icon
                    weather.setImage((new WeatherHttpClient()).getImage(weather.getIconPath()));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            weather.setDateToCurrentTime();

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
