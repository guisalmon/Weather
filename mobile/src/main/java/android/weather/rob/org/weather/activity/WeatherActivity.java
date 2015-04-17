package android.weather.rob.org.weather.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.weather.rob.org.weather.R;
import android.weather.rob.org.weather.fragment.ForecastFragment;
import android.weather.rob.org.weather.fragment.NavigationDrawerFragment;
import android.weather.rob.org.weather.fragment.TodayFragment;
import android.weather.rob.org.weather.utility.Weather;

/**
 * Main activity of the application
 */
public class WeatherActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ForecastFragment.OnFragmentInteractionListener, TodayFragment.OnFragmentInteractionListener {

    public Weather.format unitFormat = Weather.format.IMPERIAL;
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
        String unitStyle = PreferenceManager.getDefaultSharedPreferences(this).getString("unitStyle", "1");
        if (unitStyle.equals("1")) {
            unitFormat = Weather.format.METRIC;
        } else {
            unitFormat = Weather.format.IMPERIAL;
        }
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
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, WeatherPreferenceActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {
        //Nothing to do here so far
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //Nothing to do here so far
    }
}
