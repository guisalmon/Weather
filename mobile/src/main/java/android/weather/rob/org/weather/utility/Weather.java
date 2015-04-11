package android.weather.rob.org.weather.utility;

import android.location.Location;

/**
 * Created by guillaume on 11-04-15.
 */
public class Weather {
    private byte[] mIcon;
    //private Location mLocation;
    private String mCity;
    private String mCountry;
    private int mSunrise;
    private int mSunset;
    private int mId;
    private String mDesc;
    private String mCondition;
    private String mIconPath;
    private int mHumidity;
    private int mPressure;
    private float mTempMax;
    private float mTempMin;
    private float mTemp;
    private float mWindSpeed;
    private float mWindDeg;
    private int mCloud;

    public Weather() {
    }

    public String getmIconPath() {
        return mIconPath;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public void setmCondition(String mCondition) {
        this.mCondition = mCondition;
    }

    public void setmIconPath(String mIconPath) {
        this.mIconPath = mIconPath;
    }

    public void setmHumidity(int mHumidity) {
        this.mHumidity = mHumidity;
    }

    public void setmPressure(int mPressure) {
        this.mPressure = mPressure;
    }

    public void setmTempMax(float mTempMax) {
        this.mTempMax = mTempMax;
    }

    public void setmTempMin(float mTempMin) {
        this.mTempMin = mTempMin;
    }

    public void setmTemp(float mTemp) {
        this.mTemp = mTemp;
    }

    public void setmWindSpeed(float mWindSpeed) {
        this.mWindSpeed = mWindSpeed;
    }

    public void setmWindDeg(float mWindDeg) {
        this.mWindDeg = mWindDeg;
    }

    public void setmCloud(int mCloud) {
        this.mCloud = mCloud;
    }

    public void iconData(byte[] icon){
        mIcon = icon;
    }

    //public void setmLocation (Location location) { mLocation = location }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public void setmSunrise(int mSunrise) {
        this.mSunrise = mSunrise;
    }

    public void setmSunset(int mSunset) {
        this.mSunset = mSunset;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "mCity='" + mCity + '\'' +
                ", mCountry='" + mCountry + '\'' +
                ", mSunrise=" + mSunrise +
                ", mSunset=" + mSunset +
                ", mId=" + mId +
                ", mDesc='" + mDesc + '\'' +
                ", mCondition='" + mCondition + '\'' +
                ", mIconPath='" + mIconPath + '\'' +
                ", mHumidity=" + mHumidity +
                ", mPressure=" + mPressure +
                ", mTempMax=" + mTempMax +
                ", mTempMin=" + mTempMin +
                ", mTemp=" + mTemp +
                ", mWindSpeed=" + mWindSpeed +
                ", mWindDeg=" + mWindDeg +
                ", mCloud=" + mCloud +
                '}';
    }
}
