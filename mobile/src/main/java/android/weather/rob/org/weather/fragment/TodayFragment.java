package android.weather.rob.org.weather.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.weather.rob.org.weather.R;
import android.weather.rob.org.weather.activity.WeatherActivity;
import android.weather.rob.org.weather.client.WeatherJSONParser;
import android.weather.rob.org.weather.listener.OnPlaceChangeListener;
import android.weather.rob.org.weather.listener.OnWeatherDownloadListener;
import android.weather.rob.org.weather.provider.PlaceProvider;
import android.weather.rob.org.weather.utility.Weather;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

//import android.util.Log;

/**
 * Fragment displaying the weather information for today
 */
public class TodayFragment extends Fragment implements OnWeatherDownloadListener, OnPlaceChangeListener {

    private View mRootView;
    private String[] mUnits;
    private PlaceProvider mPlaceProvider = null;
    private Weather mWeather = null;

    public TodayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCurrentWeatherTaskCompleted(Weather weather) {

        mWeather = weather;
        if (mPlaceProvider.getPlaceType() == PlaceProvider.PlaceType.CITY_NAME){
            mPlaceProvider.setCityValid();
        }
        //Checks if the root view is still present when the task finishes and if weather actually got the needed data
        if (mRootView != null && weather.getCountry() != null) {
            refreshView();
        }

    }

    private void refreshView () {
        if (mWeather.getCountry() != null) {
            Locale country = new Locale("", mWeather.getCountry());
            ((TextView) getActivity().findViewById(R.id.todayLocation)).setText(mWeather.getCity() + ", " + country.getDisplayCountry());
            ((TextView) getActivity().findViewById(R.id.todayWeatherDescription)).setText(mWeather.getTemp() + mUnits[0] + " | " + mWeather.getCondition());
            ((TextView) getActivity().findViewById(R.id.todayWeatherHumidity)).setText(mWeather.getHumidity() + "%");
            ((TextView) getActivity().findViewById(R.id.todayWeatherPrecipitations)).setText(mWeather.getPrecipitations() + " " + mUnits[1]);
            ((TextView) getActivity().findViewById(R.id.todayWeatherPressure)).setText(mWeather.getPressure() + " hPa");
            ((TextView) getActivity().findViewById(R.id.todayWeatherWindSpeed)).setText(mWeather.getWindSpeed() + " " + mUnits[2]);
            ((TextView) getActivity().findViewById(R.id.todayWeatherDirection)).setText(mWeather.getWindDirection());
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(mWeather.getIconRes());
        }
    }

    @Override
    public void onCurrentWeatherTaskFailed() {
        if (mPlaceProvider.getPlaceType() == PlaceProvider.PlaceType.CITY_NAME) {
            mPlaceProvider.setCityInvalid();
        } else {
            Toast.makeText(getActivity(), R.string.error_weather_download, Toast.LENGTH_SHORT).show();
            updateData(mPlaceProvider.getPlaceType(), mPlaceProvider);
        }
    }

    @Override
    public void onResume() {
        if (((WeatherActivity) getActivity()).unitFormat == Weather.format.METRIC) {
            mUnits = getResources().getStringArray(R.array.metric);
        } else {
            mUnits = getResources().getStringArray(R.array.imperial);
        }
        mPlaceProvider = (PlaceProvider) getActivity();
        mPlaceProvider.registerOnPlaceChangeListener(this);

        if (mWeather != null) {
            refreshView();
        }

        super.onResume();
    }

    @Override
    public void onPause() {
        mPlaceProvider.unregisterOnPlaceChangeListener();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_today, container, false);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (((WeatherActivity) getActivity()).unitFormat == Weather.format.METRIC) {
            mUnits = getResources().getStringArray(R.array.metric);
        } else {
            mUnits = getResources().getStringArray(R.array.imperial);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void updateData(PlaceProvider.PlaceType type, PlaceProvider provider) {
        switch (type) {
            case GEOLOCATION:
                if (provider.getLocation() != null) {
                    WeatherJSONParser.UpdateCurrentDataByLocation(
                            provider.getLocation(),
                            this,
                            ((WeatherActivity) getActivity()).unitFormat);
                }
                break;
            case CITY_NAME:
                WeatherJSONParser.UpdateCurrentDataByCityName(
                        provider.getCity(),
                        this,
                        ((WeatherActivity) getActivity()).unitFormat);
                break;
        }
    }

}
