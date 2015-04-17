package android.weather.rob.org.weather.utility;

/**
 * Forecast contains all the data downloaded from the API concerning the weather forecast for the
 * next days
 */
public class Forecast {
    private Weather weather;
    private int mDayTemp;
    private float mMinTemp;
    private float mMaxTemp;
    private float mNightTemp;
    private float mEveTemp;
    private float mMorningTemp;

    public void setmMinTemp(float mMinTemp) {
        this.mMinTemp = mMinTemp;
    }

    public void setmMaxTemp(float mMaxTemp) {
        this.mMaxTemp = mMaxTemp;
    }

    public void setmNightTemp(float mNightTemp) {
        this.mNightTemp = mNightTemp;
    }

    public void setmEveTemp(float mEveTemp) {
        this.mEveTemp = mEveTemp;
    }

    public void setmMorningTemp(float mMorningTemp) {
        this.mMorningTemp = mMorningTemp;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public int getmDayTemp() {
        return mDayTemp;
    }

    public void setmDayTemp(int mDayTemp) {
        this.mDayTemp = mDayTemp;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "weather=" + weather +
                ", mDayTemp=" + mDayTemp +
                ", mMinTemp=" + mMinTemp +
                ", mMaxTemp=" + mMaxTemp +
                ", mNightTemp=" + mNightTemp +
                ", mEveTemp=" + mEveTemp +
                ", mMorningTemp=" + mMorningTemp +
                '}';
    }
}
