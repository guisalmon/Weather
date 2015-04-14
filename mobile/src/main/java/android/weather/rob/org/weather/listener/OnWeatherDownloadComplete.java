package android.weather.rob.org.weather.listener;

import android.weather.rob.org.weather.utility.Weather;

/**
 * Created by guillaume on 11-04-15.
 */
public interface OnWeatherDownloadComplete {
    void onCurrentWeatherTaskCompleted(Weather weather);

    void onCurrentWeatherTaskFailed();
}
