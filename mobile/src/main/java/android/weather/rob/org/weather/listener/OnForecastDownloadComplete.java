package android.weather.rob.org.weather.listener;

import android.weather.rob.org.weather.utility.Forecast;

import java.util.ArrayList;

/**
 * Created by guillaume on 12-04-15.
 */
public interface OnForecastDownloadComplete {
    void onForecastTaskCompleted(ArrayList<Forecast> forecast);

    void onForecastTaskFailed();
}
