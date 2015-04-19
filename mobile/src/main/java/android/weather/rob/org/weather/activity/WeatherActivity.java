package android.weather.rob.org.weather.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.weather.rob.org.weather.R;
import android.weather.rob.org.weather.fragment.ForecastFragment;
import android.weather.rob.org.weather.fragment.NavigationDrawerFragment;
import android.weather.rob.org.weather.fragment.TodayFragment;
import android.weather.rob.org.weather.geolocation.Geolocation;
import android.weather.rob.org.weather.geolocation.GeolocationListener;
import android.weather.rob.org.weather.listener.OnPlaceChangeListener;
import android.weather.rob.org.weather.provider.PlaceProvider;
import android.weather.rob.org.weather.utility.Weather;
import android.widget.Toast;

/**
 * Main activity of the application
 */
public class WeatherActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, SearchView.OnQueryTextListener, PlaceProvider, GeolocationListener {

    public Weather.format unitFormat = Weather.format.IMPERIAL;
    public int numberOfForecasts = 7;

    private Geolocation mGeolocation = null;
    private String mCityName = "";
    private String mPreviousCityName = "";
    private PlaceType mPreviousPlaceType = PlaceType.GEOLOCATION;
    private PlaceType mPlaceType = PlaceType.GEOLOCATION;
    private Location mCurrentLocation = null;
    private OnPlaceChangeListener mPlaceListener = null;
    private SearchView mSearchCity;
    private MenuItem mMenuItemSearch;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Set preferred unit style
        String unitStyle = PreferenceManager.getDefaultSharedPreferences(this).getString("unitStyle", "1");
        if (unitStyle.equals("1")) {
            unitFormat = Weather.format.METRIC;
        } else {
            unitFormat = Weather.format.IMPERIAL;
        }

        //Set preferred number of days of forecast
        String numberOfForecastsText = PreferenceManager.getDefaultSharedPreferences(this).getString("forecastLength", "7");
        numberOfForecasts = Integer.parseInt(numberOfForecastsText);
        if (numberOfForecasts > 15) {
            numberOfForecasts = 16;
        } else {
            numberOfForecasts += 1;
        }

        //Set geolocation
        if (mCurrentLocation == null) {
            mGeolocation = new Geolocation((LocationManager) getSystemService(Context.LOCATION_SERVICE), this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mGeolocation.stop();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        setFragment(position);
    }

    private void setFragment(int i) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new TodayFragment();
        switch (i) {
            case 0:
                fragment = new TodayFragment();
                break;
            case 1:
                fragment = new ForecastFragment();
                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.weather, menu);
            restoreActionBar();
            mMenuItemSearch = menu.findItem(R.id.citySearchItem);
            mSearchCity = (SearchView) MenuItemCompat.getActionView(mMenuItemSearch);
            mSearchCity.setQueryHint(getResources().getString(R.string.city_search_hint));
            mSearchCity.setOnQueryTextListener(this);
            mSearchCity.setIconifiedByDefault(true);
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent i = new Intent(this, WeatherPreferenceActivity.class);
                startActivity(i);
                return true;
            case R.id.locationSearchItem:
                mCityName = "";
                mPlaceType = PlaceType.GEOLOCATION;
                mPreviousPlaceType = PlaceType.GEOLOCATION;
                mPlaceListener.updateData(mPlaceType, this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        mCityName = s;
        mPlaceType = PlaceType.CITY_NAME;
        mPlaceListener.updateData(mPlaceType, this);
        mMenuItemSearch.collapseActionView();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public Location getLocation() {
        return mCurrentLocation;
    }

    @Override
    public String getCity() {
        return mCityName;
    }

    @Override
    public PlaceType getPlaceType() {
        return mPlaceType;
    }

    @Override
    public void setCityInvalid() {
        if (mPreviousPlaceType == PlaceType.CITY_NAME) {
            mCityName = mPreviousCityName;
        } else {
            mPlaceType = PlaceType.GEOLOCATION;
        }
        mPlaceListener.updateData(mPlaceType, this);
        Toast.makeText(this, R.string.error_in_city_name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setCityValid() {
        mPreviousCityName = mCityName;
        mPreviousPlaceType = mPlaceType;
    }

    @Override
    public void registerOnPlaceChangeListener(OnPlaceChangeListener listener) {
        mPlaceListener = listener;
        listener.updateData(mPlaceType, this);
    }

    @Override
    public void unregisterOnPlaceChangeListener() {
        mPlaceListener = null;
    }

    @Override
    public void onGeolocationRespond(Geolocation geolocation, Location location) {
        mCurrentLocation = location;
        if (mPlaceListener != null) {
            mPlaceListener.updateData(mPlaceType, this);
        }
    }

    @Override
    public void onGeolocationFail(Geolocation geolocation) {
        Toast.makeText(this, R.string.error_location, Toast.LENGTH_SHORT).show();
    }
}
