package android.weather.rob.org.weather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.weather.rob.org.weather.R;
import android.weather.rob.org.weather.activity.WeatherActivity;
import android.weather.rob.org.weather.adapter.ForecastListAdapter;
import android.weather.rob.org.weather.client.WeatherJSONParser;
import android.weather.rob.org.weather.listener.OnForecastDownloadListener;
import android.weather.rob.org.weather.listener.OnPlaceChangeListener;
import android.weather.rob.org.weather.provider.PlaceProvider;
import android.weather.rob.org.weather.utility.Forecast;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Fragment displaying the list of weather forecasts for the next days
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 */
public class ForecastFragment extends ListFragment implements OnForecastDownloadListener, OnPlaceChangeListener {

    private ArrayList<Forecast> mForecast;
    private ListView mListView;
    private PlaceProvider mPlaceProvider;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ForecastFragment() {
    }

    @Override
    public void onForecastTaskFailed() {
        if (mPlaceProvider.getPlaceType() == PlaceProvider.PlaceType.CITY_NAME) {
            mPlaceProvider.setCityInvalid();
        } else {
            mPlaceProvider.showToast(R.string.error_forecast_download);
        }
    }

    @Override
    public void onForecastTaskCompleted(ArrayList<Forecast> forecasts) {
        mForecast = forecasts;
        refreshView();
        if (mPlaceProvider.getPlaceType() == PlaceProvider.PlaceType.GEOLOCATION) {
            mPlaceProvider.showToast(R.string.forecast_acquired_location);
        } else {
            mPlaceProvider.showToast(getResources().getString(R.string.forecast_acquired_city) + " " + mPlaceProvider.getCity());
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mForecast != null) {
            refreshView();
        }
        mPlaceProvider = (PlaceProvider) getActivity();
        mPlaceProvider.registerOnPlaceChangeListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = getListView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mListView = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlaceProvider.unregisterOnPlaceChangeListener();
    }

    private void refreshView() {
        if (mListView != null) {
            /*
      The Adapter which will be used to populate the ListView/GridView with
      Views.
     */
            ForecastListAdapter adapter = new ForecastListAdapter(getActivity(), mForecast, (((WeatherActivity) getActivity()).unitFormat));
            setListAdapter(adapter);
        } else {
            Log.w(getClass().getName(), "ListView is null");
        }
    }

    @Override
    public void updateData(PlaceProvider.PlaceType type, PlaceProvider provider) {
        switch (type) {
            case GEOLOCATION:
                if (provider.getLocation() != null) {
                    WeatherJSONParser.UpdateForecastDataByLocation(
                            provider.getLocation(),
                            this,
                            ((WeatherActivity) getActivity()).unitFormat,
                            ((WeatherActivity) getActivity()).numberOfForecasts);
                }
                break;
            case CITY_NAME:
                WeatherJSONParser.UpdateForecastDataByCityName(
                        provider.getCity(),
                        this,
                        ((WeatherActivity) getActivity()).unitFormat,
                        ((WeatherActivity) getActivity()).numberOfForecasts);
                break;
        }
    }
}
