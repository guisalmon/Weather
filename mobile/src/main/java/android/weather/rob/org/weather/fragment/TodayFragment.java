package android.weather.rob.org.weather.fragment;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.weather.rob.org.weather.R;
import android.weather.rob.org.weather.activity.WeatherActivity;
import android.weather.rob.org.weather.client.WeatherJSONParser;
import android.weather.rob.org.weather.geolocation.Geolocation;
import android.weather.rob.org.weather.geolocation.GeolocationListener;
import android.weather.rob.org.weather.listener.OnWeatherDownloadComplete;
import android.weather.rob.org.weather.utility.Weather;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

//import android.util.Log;

/**
 * Fragment displaying the weather information for today
 * Activities that contain this fragment must implement the
 * {@link TodayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TodayFragment extends Fragment implements GeolocationListener, OnWeatherDownloadComplete {

    private final Location mCurrentLocation = null;
    private Geolocation mGeolocation = null;
    private View mRootView;
    private OnFragmentInteractionListener mListener;
    private String[] mUnits;

    public TodayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCurrentWeatherTaskCompleted(Weather weather) {

        //Checks if the root view is still present when the task finishes and if weather actually got the needed data
        if (mRootView != null && weather.getCountry() != null) {
            Locale country = new Locale("", weather.getCountry());
            ((TextView) getActivity().findViewById(R.id.todayLocation)).setText(weather.getCity() + ", " + country.getDisplayCountry());
            ((TextView) getActivity().findViewById(R.id.todayWeatherDescription)).setText(weather.getTemp() + mUnits[0] + " | " + weather.getCondition());
            ((TextView) getActivity().findViewById(R.id.todayWeatherHumidity)).setText(weather.getHumidity() + "%");
            ((TextView) getActivity().findViewById(R.id.todayWeatherPrecipitations)).setText(weather.getPrecipitations() + " " + mUnits[1]);
            ((TextView) getActivity().findViewById(R.id.todayWeatherPressure)).setText(weather.getPressure() + " hPa");
            ((TextView) getActivity().findViewById(R.id.todayWeatherWindSpeed)).setText(weather.getWindSpeed() + " " + mUnits[2]);
            ((TextView) getActivity().findViewById(R.id.todayWeatherDirection)).setText(weather.getWindDirection());
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(weather.getIconRes());
        }

    }

    @Override
    public void onCurrentWeatherTaskFailed() {
        Toast.makeText(getActivity(), R.string.error_weather_download, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        if (((WeatherActivity) getActivity()).unitFormat == Weather.format.METRIC) {
            mUnits = getResources().getStringArray(R.array.metric);
        } else {
            mUnits = getResources().getStringArray(R.array.imperial);
        }
        if (mCurrentLocation == null) {
            mGeolocation = new Geolocation((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE), this);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        // stop geolocation
        if (mGeolocation != null) mGeolocation.stop();
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
        try {
            mListener = (OnFragmentInteractionListener) activity;
            if (((WeatherActivity) getActivity()).unitFormat == Weather.format.METRIC) {
                mUnits = getResources().getStringArray(R.array.metric);
            } else {
                mUnits = getResources().getStringArray(R.array.imperial);
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onGeolocationRespond(Geolocation geolocation, Location location) {
        if (mRootView == null) return;
        WeatherJSONParser.UpdateCurrentDataByLocation(location, this, ((WeatherActivity) getActivity()).unitFormat);
    }

    @Override
    public void onGeolocationFail(Geolocation geolocation) {
        if (mRootView == null) return;
        Log.d(getClass().getName(), "Fragment.onGeolocationFail()");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
