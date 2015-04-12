package android.weather.rob.org.weather.fragment;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.weather.rob.org.weather.R;
import android.weather.rob.org.weather.adapter.ForecastListAdapter;
import android.weather.rob.org.weather.client.WeatherJSONParser;
import android.weather.rob.org.weather.fragment.dummy.DummyContent;
import android.weather.rob.org.weather.geolocation.Geolocation;
import android.weather.rob.org.weather.geolocation.GeolocationListener;
import android.weather.rob.org.weather.listener.OnForecastDownloadComplete;
import android.weather.rob.org.weather.utility.Forecast;
import android.weather.rob.org.weather.utility.Weather;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ForecastFragment extends ListFragment implements AbsListView.OnItemClickListener, OnForecastDownloadComplete, GeolocationListener {

    private Location mCurrentLocation = null;
    private Geolocation mGeolocation = null;
    private WeatherJSONParser mWeatherUpdater = null;
    private ArrayList<Forecast> mForecast;
    private OnFragmentInteractionListener mListener;
    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ForecastFragment() {
    }

    public static ForecastFragment newInstance(String param1, String param2) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onForecastTaskFailed() {
        Toast.makeText(getActivity(), "Downloading of the forecast data failed, check your internet connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGeolocationRespond(Geolocation geolocation, Location location) {
        mWeatherUpdater.UpdateForecastDataByLocation(location, this, Weather.format.METRIC);
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
                mAdapter = new ForecastListAdapter(getActivity(), R.layout.forecast_list_item, mForecast);
                setListAdapter(mAdapter);
            } else {
                Log.w(getClass().getName(),"ListView is null");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                R.layout.forecast_list_item, android.R.id.text1, DummyContent.ITEMS);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mCurrentLocation == null) {
            mWeatherUpdater = new WeatherJSONParser();
            mGeolocation = new Geolocation((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE), this);
        }
        mAdapter = new ForecastListAdapter(getActivity(), R.layout.forecast_list_item, mForecast);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
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
