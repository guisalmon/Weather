package android.weather.rob.org.weather.client.parser;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.weather.rob.org.weather.client.WeatherHttpClient;
import android.weather.rob.org.weather.listener.OnWeatherDownloadComplete;
import android.weather.rob.org.weather.utility.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by guillaume on 11-04-15.
 */
public class WeatherJSONParser{

    private static String mLoc;

    private static class DownloadWeatherTask extends AsyncTask<OnWeatherDownloadComplete, Void, Weather> {
        private OnWeatherDownloadComplete listener;
        @Override
        protected Weather doInBackground(OnWeatherDownloadComplete... params) {
            Weather weather = new Weather();
            listener = params[0];
            String data = ( (new WeatherHttpClient()).getWeatherData(mLoc));

            if (data != null) {
                weather = getWeather(data);
            }

            // Let's retrieve the icon
            weather.iconData((new WeatherHttpClient()).getImage(weather.getmIconPath()));

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            listener.onTaskCompleted(weather);
        }
    }

    public static Weather UpdateData(Location location, OnWeatherDownloadComplete listener){
        mLoc = "lat=" + location.getLatitude() + "&lon=" + location.getLongitude();
        if ((mLoc != null)&&(mLoc != "")) {
            new DownloadWeatherTask().execute(listener);
        }
        return null;
    }

    private static Weather getWeather(String data){
        Weather weather = new Weather();
        JSONObject jObj = null;
        try {
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
            return weather;
        } catch (JSONException e) {
            Log.e("JSONParser", "Weather data unavailable");
            return null;
        }

    }

    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }
}
