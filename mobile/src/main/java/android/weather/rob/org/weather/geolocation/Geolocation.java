package android.weather.rob.org.weather.geolocation;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Geolocation handles all the location updates from the providers.
 */
public class Geolocation implements LocationListener {
    private static final int LOCATION_AGE = 60000 * 30; // milliseconds
    private static final int LOCATION_TIMEOUT = 30000; // milliseconds

    private final WeakReference<GeolocationListener> mListener;
    private final Timer mTimer;
    private LocationManager mLocationManager;
    private Location mCurrentLocation;
    private int providerCount = 2;


    public Geolocation(LocationManager locationManager, GeolocationListener listener) {
        mLocationManager = locationManager;
        mListener = new WeakReference<>(listener);
        mTimer = new Timer();
        init();
    }


    private void init() {
        // get last known location
        Location lastKnownLocation = getLastKnownLocation(mLocationManager);

        // try to listen last known location
        if (lastKnownLocation != null) {
            onLocationChanged(lastKnownLocation);
        }

        if (mCurrentLocation == null) {
            // start timer to check timeout
            TimerTask task = new TimerTask() {
                public void run() {
                    if (mCurrentLocation == null) {
                        Log.v(getClass().getName(), "Geolocation.timer: timeout");
                        stop();
                        GeolocationListener listener = mListener.get();
                        if (listener != null) listener.onGeolocationFail(Geolocation.this);
                    }
                }
            };
            mTimer.schedule(task, LOCATION_TIMEOUT);

            // register location updates
            try {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0.0f, this);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            try {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0l, 0.0f, this);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Stop all location updates
     */
    public void stop() {
        Log.v(getClass().getName(), "Geolocation.stop()");
        mTimer.cancel();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
            mLocationManager = null;
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.v(getClass().getName(), "Geolocation.onLocationChanged(): " + location.getProvider() + " / " + location.getLatitude() + " / " + location.getLongitude() + " / " + new Date(location.getTime()).toString());

        // check location age
        long timeDelta = System.currentTimeMillis() - location.getTime();
        if (timeDelta > LOCATION_AGE) {
            Log.v(getClass().getName(), "Geolocation.onLocationChanged(): gotten location is too old");
            // gotten location is too old
            return;
        }

        // return location
        mCurrentLocation = new Location(location);
        stop();
        GeolocationListener listener = mListener.get();
        if (listener != null)
            listener.onGeolocationRespond(Geolocation.this, mCurrentLocation);
    }


    @Override
    public void onProviderDisabled(String provider) {
        Log.v(getClass().getName(), "Geolocation.onProviderDisabled(): " + provider);
        providerCount -= 1;
        Log.d(getClass().getName(), "provider count : "+providerCount);
        if (providerCount == 0) {
            mListener.get().onGeolocationFail(this);
        }

    }


    @Override
    public void onProviderEnabled(String provider) {
        Log.v(getClass().getName(), "Geolocation.onProviderEnabled(): " + provider);
        providerCount += 1;
        Log.d(getClass().getName(), "provider count : "+providerCount);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.v(getClass().getName(), "Geolocation.onStatusChanged(): " + provider);
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                Log.v(getClass().getName(), "Geolocation.onStatusChanged(): status OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.v(getClass().getName(), "Geolocation.onStatusChanged(): status TEMPORARILY_UNAVAILABLE");
                break;
            case LocationProvider.AVAILABLE:
                Log.v(getClass().getName(), "Geolocation.onStatusChanged(): status AVAILABLE");
                break;
        }
    }


    // returns last known freshest location from network or GPS
    private Location getLastKnownLocation(LocationManager locationManager) {
        Log.v(getClass().getName(), "Geolocation.getLastKnownLocation()");

        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        long timeNet = 0l;
        long timeGps = 0l;

        if (locationNet != null) {
            timeNet = locationNet.getTime();
        }

        if (locationGps != null) {
            timeGps = locationGps.getTime();
        }

        if (timeNet > timeGps) return locationNet;
        else return locationGps;
    }
}
