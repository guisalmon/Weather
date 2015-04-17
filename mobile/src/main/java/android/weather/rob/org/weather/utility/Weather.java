package android.weather.rob.org.weather.utility;

import android.graphics.Bitmap;
import android.weather.rob.org.weather.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Weather contains all the data downloaded from the API concerning the weather on a specific day
 */
public class Weather {
    private Bitmap mImage;
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
    private int mTemp;
    private float mWindSpeed;
    private float mWindDeg;
    private int mCloud;
    private Date mDate;
    private float mPrecipitations;

    public void setDate(long timestamp) {
        this.mDate = new Date(timestamp * 1000);
    }

    public void setDateToCurrentTime() {
        this.mDate = new Date();
    }

    public String getIconPath() {
        return mIconPath;
    }

    public void setIconPath(String mIconPath) {
        this.mIconPath = mIconPath;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getCondition() {
        return mCondition;
    }

    public void setCondition(String mCondition) {
        this.mCondition = mCondition;
    }

    public int getHumidity() {
        return mHumidity;
    }

    public void setHumidity(int mHumidity) {
        this.mHumidity = mHumidity;
    }

    public int getPressure() {
        return mPressure;
    }

    public void setPressure(int mPressure) {
        this.mPressure = mPressure;
    }

    public float getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(float mWindSpeed) {
        this.mWindSpeed = mWindSpeed;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    /**
     * Gets the day of the week corresponding to the date of this weather
     *
     * @return the day of the week as an element of the day enum structure
     */
    public day getDay() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(mDate);
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return day.MONDAY;
            case Calendar.TUESDAY:
                return day.TUESDAY;
            case Calendar.FRIDAY:
                return day.FRIDAY;
            case Calendar.WEDNESDAY:
                return day.WEDNESDAY;
            case Calendar.THURSDAY:
                return day.THURSDAY;
            case Calendar.SATURDAY:
                return day.SATURDAY;
            case Calendar.SUNDAY:
                return day.SUNDAY;
        }
        return day.NONE;
    }

    /**
     * Returns the icon for the current weather as a bitmap
     *
     * @return current weather icon Bitmap
     * @deprecated getIconRes() should be used to directly get the icon resource suitable for the weather
     */
    public Bitmap getIcon() {
        //Log.d(getClass().getName(), ""+mIcon.length);
        //return BitmapFactory.decodeByteArray(mIcon, 0, mIcon.length);
        return mImage;
    }

    /**
     * Sets an icon for this weather
     *
     * @param image to be set as a weather icon
     * @deprecated getIconRes() should be used to directly get the icon resource suitable for the weather
     */
    @Deprecated
    public void setImage(Bitmap image) {
        this.mImage = image;
    }

    public String getWindDirection() {
        if ((mWindDeg < 22.5) || (mWindDeg > 337.5)) return "N";
        if (mWindDeg < 67.5) return "NE";
        if (mWindDeg < 112.5) return "E";
        if (mWindDeg < 157.5) return "SE";
        if (mWindDeg < 202.5) return "S";
        if (mWindDeg < 247.5) return "SW";
        if (mWindDeg < 292.5) return "W";
        else return "NW";
    }

    /**
     * Gets the icon related to this weather from resources
     *
     * @return the icon's id.
     */
    public int getIconRes() {
        if (mIconPath.equals("01d")) {
            return R.drawable.ic_01d;
        }
        if (mIconPath.equals("01n")) {
            return R.drawable.ic_01n;
        }
        if (mIconPath.equals("02d")) {
            return R.drawable.ic_02d;
        }
        if (mIconPath.equals("02n")) {
            return R.drawable.ic_02n;
        }
        if (mIconPath.equals("03d")) {
            return R.drawable.ic_03d;
        }
        if (mIconPath.equals("03n")) {
            return R.drawable.ic_03n;
        }
        if (mIconPath.equals("04d")) {
            return R.drawable.ic_04d;
        }
        if (mIconPath.equals("04n")) {
            return R.drawable.ic_04n;
        }
        if (mIconPath.equals("09d")) {
            return R.drawable.ic_09d;
        }
        if (mIconPath.equals("09n")) {
            return R.drawable.ic_09n;
        }
        if (mIconPath.equals("10d")) {
            return R.drawable.ic_10d;
        }
        if (mIconPath.equals("10n")) {
            return R.drawable.ic_10n;
        }
        if (mIconPath.equals("11d")) {
            return R.drawable.ic_11d;
        }
        if (mIconPath.equals("11n")) {
            return R.drawable.ic_11n;
        }
        if (mIconPath.equals("13d")) {
            return R.drawable.ic_13d;
        }
        if (mIconPath.equals("13n")) {
            return R.drawable.ic_13n;
        }
        if (mIconPath.equals("50d")) {
            return R.drawable.ic_50d;
        }
        if (mIconPath.equals("50n")) {
            return R.drawable.ic_50n;
        }
        return 0;
    }

    public int getTemp() {
        return mTemp;
    }

    public void setTemp(int mTemp) {
        this.mTemp = mTemp;
    }

    public float getPrecipitations() {
        return mPrecipitations;
    }

    public void setPrecipitations(float precipitations) {
        this.mPrecipitations = precipitations;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public void setTempMax(float mTempMax) {
        this.mTempMax = mTempMax;
    }

    public void setTempMin(float mTempMin) {
        this.mTempMin = mTempMin;
    }

    public void setWindDeg(float mWindDeg) {
        this.mWindDeg = mWindDeg;
    }

    public void setCloud(int mCloud) {
        this.mCloud = mCloud;
    }

    public void setSunrise(int mSunrise) {
        this.mSunrise = mSunrise;
    }

    public void setSunset(int mSunset) {
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

    public enum format {
        METRIC,
        IMPERIAL
    }

    public enum day {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY,
        NONE
    }
}
