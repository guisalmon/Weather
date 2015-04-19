package android.weather.rob.org.weather.listener;

import android.weather.rob.org.weather.provider.PlaceProvider;

/**
 * Created by guillaume on 19-04-15.
 */
public interface OnPlaceChangeListener {
    void updateData (PlaceProvider.PlaceType type, PlaceProvider provider);

}
