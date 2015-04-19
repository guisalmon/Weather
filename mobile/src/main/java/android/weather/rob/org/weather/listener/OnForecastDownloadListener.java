package android.weather.rob.org.weather.listener;

import android.weather.rob.org.weather.utility.Forecast;

import java.util.ArrayList;

/**
 * Listener to register in the WeatherJSONParser and implement in the class which will use the
 * weather forecast data.
 */
public interface OnForecastDownloadListener {

    /**
     * Called when a weather forecast download is finished
     *
     * @param forecast object containing the downloaded weather forecast data
     */
    void onForecastTaskCompleted(ArrayList<Forecast> forecast);

    /**
     * Called when a weather forecast download failed.
     */
    void onForecastTaskFailed();
}
