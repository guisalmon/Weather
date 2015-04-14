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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.weather.rob.org.weather.R;
import android.weather.rob.org.weather.fragment.ForecastFragment;
import android.weather.rob.org.weather.fragment.NavigationDrawerFragment;
import android.weather.rob.org.weather.fragment.TodayFragment;
import android.weather.rob.org.weather.utility.Weather;


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
    private int mCurrentFragment = -1;

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
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        setFragment(position);
        mCurrentFragment = position;
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

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.today_section);
                break;
            case 2:
                mTitle = getString(R.string.forecast_section);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.weather, menu);
            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, WeatherPreferenceActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {
        //TODO
        //Nothing to do here so far
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //TODO
        //Nothing to do here so far
    }
}
