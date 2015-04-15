package android.weather.rob.org.weather.utility;

import android.graphics.Bitmap;
import android.weather.rob.org.weather.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by guillaume on 11-04-15.
 */
public class Weather {
    private byte[] mIcon;
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

    public void setmDate(long timestamp) {
        this.mDate = new Date(timestamp * 1000);
    }

    public void setmDateToCurrentTime() {
        mDate = new Date();
    }

    public String getmIconPath() {
        return mIconPath;
    }

    public void setmIconPath(String mIconPath) {
        this.mIconPath = mIconPath;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getmCondition() {
        return mCondition;
    }

    public void setmCondition(String mCondition) {
        this.mCondition = mCondition;
    }

    public int getmHumidity() {
        return mHumidity;
    }

    public void setmHumidity(int mHumidity) {
        this.mHumidity = mHumidity;
    }

    public int getmPressure() {
        return mPressure;
    }

    public void setmPressure(int mPressure) {
        this.mPressure = mPressure;
    }

    public float getmWindSpeed() {
        return mWindSpeed;
    }

    public void setmWindSpeed(float mWindSpeed) {
        this.mWindSpeed = mWindSpeed;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

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

    public Bitmap getIcon() {
        //Log.d(getClass().getName(), ""+mIcon.length);
        //return BitmapFactory.decodeByteArray(mIcon, 0, mIcon.length);
        return mImage;
    }

    /**
     * Sets an icon for this weather
     * @deprecated getIconRes() should be used to directly get the icon resource suitable for the weather
     * @param image to be set as a weather icon
     */
    @Deprecated
    public void setmImage(Bitmap image) {
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

    public int getmTemp() {
        return mTemp;
    }

    public void setmTemp(int mTemp) {
        this.mTemp = mTemp;
    }

    public float getmPrecipitations() {
        return mPrecipitations;
    }

    public void setmPrecipitations(float precipitations) {
        this.mPrecipitations = precipitations;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public void setmTempMax(float mTempMax) {
        this.mTempMax = mTempMax;
    }

    public void setmTempMin(float mTempMin) {
        this.mTempMin = mTempMin;
    }

    public void setmWindDeg(float mWindDeg) {
        this.mWindDeg = mWindDeg;
    }

    public void setmCloud(int mCloud) {
        this.mCloud = mCloud;
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
