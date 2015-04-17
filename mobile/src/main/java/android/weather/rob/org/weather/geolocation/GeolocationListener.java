package android.weather.rob.org.weather.geolocation;

import android.location.Location;

/**
 * Listener for the Geolocation updates
 */
public interface GeolocationListener {
    /**
     * Called when new location data is acquired.
     *
     * @param geolocation handling location updates
     * @param location    location acquired
     */
    public void onGeolocationRespond(Geolocation geolocation, Location location);

    /**
     * Called when geolocation failed
     *
     * @param geolocation handling location updates
     */
    public void onGeolocationFail(Geolocation geolocation);
}
