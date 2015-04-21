package android.weather.rob.org.weather.provider;

import android.location.Location;
import android.weather.rob.org.weather.listener.OnPlaceChangeListener;

/**
 * Provider for the current place
 */
public interface PlaceProvider {
    Location getLocation();

    String getCity();

    PlaceType getPlaceType();

    void setCityInvalid();

    void setCityValid();

    void registerOnPlaceChangeListener(OnPlaceChangeListener listener);

    void unregisterOnPlaceChangeListener();

    void showToast(int resource);

    enum PlaceType {
        GEOLOCATION,
        CITY_NAME
    }
}
