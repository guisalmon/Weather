package android.weather.rob.org.weather.listener;

import android.weather.rob.org.weather.utility.Weather;

/**
 * Listener to register in the WeatherJSONParser and implement in the class which will use the weather data.
 */
public interface OnWeatherDownloadListener {
    /**
     * Called when a weather download is finished
     * @param weather object containing the downloaded data
     */
    void onCurrentWeatherTaskCompleted(Weather weather);

    /**
     * Called when a weather download failed
     */
    void onCurrentWeatherTaskFailed();
}
