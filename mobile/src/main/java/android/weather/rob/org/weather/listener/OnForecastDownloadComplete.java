package android.weather.rob.org.weather.listener;

import android.weather.rob.org.weather.utility.Weather;

import java.util.ArrayList;

/**
 * Created by guillaume on 12-04-15.
 */
public interface OnForecastDownloadComplete {
    void onForecastTaskCompleted(ArrayList<Weather> forecast);
}