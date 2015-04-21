package android.weather.rob.org.weather.listener;

import android.weather.rob.org.weather.provider.PlaceProvider;

/**
 * Listener for target place changes
 */
public interface OnPlaceChangeListener {
    void updateData (PlaceProvider.PlaceType type, PlaceProvider provider);

}
