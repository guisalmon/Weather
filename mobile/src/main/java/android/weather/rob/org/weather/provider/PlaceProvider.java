package android.weather.rob.org.weather.provider;

import android.location.Location;
import android.weather.rob.org.weather.listener.OnPlaceChangeListener;
import android.weather.rob.org.weather.utility.Weather;

/**
 * Created by guillaume on 19-04-15.
 */
public interface PlaceProvider {
    Location getLocation ();
    String getCity ();
    PlaceType getPlaceType ();
    void setCityInvalid ();
    void setCityValid ();
    void registerOnPlaceChangeListener (OnPlaceChangeListener listener);
    void unregisterOnPlaceChangeListener ();
    enum PlaceType {
        GEOLOCATION,
        CITY_NAME
    }
}
