package android.weather.rob.org.weather.fragment;

import android.app.Activity;
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
public class ForecastFragment extends ListFragment implements OnForecastDownloadListener, OnPlaceChangeListener {

    private ArrayList<Forecast> mForecast;
    private ListView mListView;
    private PlaceProvider mPlaceProvider;
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
        if (mPlaceProvider.getPlaceType() == PlaceProvider.PlaceType.CITY_NAME) {
            mPlaceProvider.setCityInvalid();
        } else {
            Toast.makeText(getActivity(), R.string.error_forecast_download, Toast.LENGTH_SHORT).show();
            updateData(mPlaceProvider.getPlaceType(), mPlaceProvider);
        }
    }

    @Override
    public void onForecastTaskCompleted(ArrayList<Forecast> forecasts) {
        if (forecasts != null) {
            mForecast = forecasts;
            refreshView();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void refreshView() {
        if (mListView != null) {
            mAdapter = new ForecastListAdapter(getActivity(), mForecast, (((WeatherActivity) getActivity()).unitFormat));
            setListAdapter(mAdapter);
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
