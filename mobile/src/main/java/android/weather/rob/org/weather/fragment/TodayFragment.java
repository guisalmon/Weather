package android.weather.rob.org.weather.fragment;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment implements GeolocationListener, OnWeatherDownloadComplete {

    private final Location mCurrentLocation = null;
    private Geolocation mGeolocation = null;
    private WeatherJSONParser mWeatherUpdater = null;
    private Weather mWeather = null;
    private View mRootView;
    private OnFragmentInteractionListener mListener;
    private String[] mUnits;

    public TodayFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TodayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayFragment newInstance(String param1, String param2) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCurrentWeatherTaskCompleted(Weather weather) {
        Locale country = new Locale("", weather.getmCountry());
        ((TextView) getActivity().findViewById(R.id.todayLocation)).setText(weather.getmCity() + ", " + country.getDisplayCountry());
        ((TextView) getActivity().findViewById(R.id.todayWeatherDescription)).setText(weather.getmTemp() + mUnits[0] + " | " + weather.getmCondition());
        ((TextView) getActivity().findViewById(R.id.todayWeatherHumidity)).setText(weather.getmHumidity() + "%");
        ((TextView) getActivity().findViewById(R.id.todayWeatherPrecipitations)).setText(weather.getmPrecipitations() + " " + mUnits[1]);
        ((TextView) getActivity().findViewById(R.id.todayWeatherPressure)).setText(weather.getmPressure() + " hPa");
        ((TextView) getActivity().findViewById(R.id.todayWeatherWindSpeed)).setText(weather.getmWindSpeed() + " " + mUnits[2]);
        ((TextView) getActivity().findViewById(R.id.todayWeatherDirection)).setText(weather.getWindDirection());
        ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(weather.getIconRes());

    }

    @Override
    public void onCurrentWeatherTaskFailed() {
        Toast.makeText(getActivity(), "Downloading of the current weather data failed, check your internet connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        if (((WeatherActivity) getActivity()).unitFormat == Weather.format.METRIC) {
            mUnits = getResources().getStringArray(R.array.metric);
        } else {
            mUnits = getResources().getStringArray(R.array.imperial);
        }
        if (mCurrentLocation == null) {
            mWeatherUpdater = new WeatherJSONParser();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        mWeatherUpdater.UpdateCurrentDataByLocation(location, this, ((WeatherActivity) getActivity()).unitFormat);
    }

    @Override
    public void onGeolocationFail(Geolocation geolocation) {
        if (mRootView == null) return;
        Log.d(getClass().getName(), "Fragment.onGeolocationFail()");
    }

    /*
    public void setIconfromPath(String iconPath){
        Log.d(getClass().getName(), "."+iconPath+"."+iconPath.equals("O1n"));
        if (iconPath.equals("01d")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_01d);
        }
        if (iconPath.equals("01n")){
            Log.d(getClass().getName(), "Fuck this shit");
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_01n);
        }
        if (iconPath.equals("02d")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_02d);
        }
        if (iconPath.equals("02n")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_02n);
        }
        if (iconPath.equals("03d")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_03d);
        }
        if (iconPath.equals("03n")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_03n);
        }
        if (iconPath.equals("04d")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_04d);
        }
        if (iconPath.equals("04n")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_04n);
        }
        if (iconPath.equals("09d")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_09d);
        }
        if (iconPath.equals("09n")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_09n);
        }
        if (iconPath.equals("10d")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_10d);
        }
        if (iconPath.equals("10n")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_10n);
        }
        if (iconPath.equals("11d")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_11d);
        }
        if (iconPath.equals("11n")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_11n);
        }
        if (iconPath.equals("13d")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_13d);
        }
        if (iconPath.equals("13n")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_13n);
        }
        if (iconPath.equals("50d")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_50d);
        }
        if (iconPath.equals("50n")){
            ((ImageView) getActivity().findViewById(R.id.todayWeatherIcon)).setImageResource(R.drawable.ic_50n);
        }
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
