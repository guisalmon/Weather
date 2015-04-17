package android.weather.rob.org.weather.fragment;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.weather.rob.org.weather.R;
import android.weather.rob.org.weather.activity.WeatherActivity;
import android.weather.rob.org.weather.adapter.ForecastListAdapter;
import android.weather.rob.org.weather.client.WeatherJSONParser;
import android.weather.rob.org.weather.geolocation.Geolocation;
import android.weather.rob.org.weather.geolocation.GeolocationListener;
import android.weather.rob.org.weather.listener.OnForecastDownloadComplete;
import android.weather.rob.org.weather.utility.Forecast;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Fragment displaying the list of weather forecasts for the next days
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ForecastFragment extends ListFragment implements OnForecastDownloadComplete, GeolocationListener {

    private final Location mCurrentLocation = null;
    private Geolocation mGeolocation = null;
    private WeatherJSONParser mWeatherUpdater = null;
    private ArrayList<Forecast> mForecast;
    private OnFragmentInteractionListener mListener;
    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ForecastListAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ForecastFragment() {
    }

    @Override
    public void onForecastTaskFailed() {
        Toast.makeText(getActivity(), R.string.error_forecast_download, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGeolocationRespond(Geolocation geolocation, Location location) {
        WeatherJSONParser.UpdateForecastDataByLocation(location, this, ((WeatherActivity) getActivity()).unitFormat);
    }

    @Override
    public void onGeolocationFail(Geolocation geolocation) {
        Log.d(getClass().getName(), "Fragment.onGeolocationFail()");
    }

    @Override
    public void onForecastTaskCompleted(ArrayList<Forecast> forecast) {
        if (forecast != null) {
            for (Forecast f : forecast) {
                Log.d(getClass().getName(), f.toString());
            }
            mForecast = forecast;
            if (getListView() != null) {
                mAdapter = new ForecastListAdapter(getActivity(), mForecast, (((WeatherActivity) getActivity()).unitFormat));
                setListAdapter(mAdapter);
            } else {
                Log.w(getClass().getName(), "ListView is null");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCurrentLocation == null) {
            mWeatherUpdater = new WeatherJSONParser();
            mGeolocation = new Geolocation((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE), this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ForecastListAdapter(getActivity(), mForecast, (((WeatherActivity) getActivity()).unitFormat));
    }

    @Override
    public void onPause() {
        super.onPause();
        // stop geolocation
        if (mGeolocation != null) mGeolocation.stop();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
