package android.weather.rob.org.weather.geolocation;

import android.location.Location;

/**
 * Created by guillaume on 11-04-15.
 */
public interface GeolocationListener {
    public void onGeolocationRespond(Geolocation geolocation, Location location);
    public void onGeolocationFail(Geolocation geolocation);
}
