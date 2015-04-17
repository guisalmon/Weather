package android.weather.rob.org.weather.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.weather.rob.org.weather.R;
import android.weather.rob.org.weather.utility.Forecast;
import android.weather.rob.org.weather.utility.Weather;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter creating the rows displayed by ForecastFragment with the data from Forecast objects
 */
public class ForecastListAdapter extends ArrayAdapter<Forecast> {
    private final Context mContext;
    private final int mLayoutResourceId;
    private final String mTempUnit;
    private List<Forecast> mForecasts = null;


    public ForecastListAdapter(Context context, List<Forecast> forecasts, Weather.format format) {
        super(context, R.layout.forecast_list_item, forecasts);
        mContext = context;
        mLayoutResourceId = R.layout.forecast_list_item;
        mForecasts = forecasts;
        if (format == Weather.format.METRIC) {
            mTempUnit = "°C";
        } else {
            mTempUnit = "°F";
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ForecastHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new ForecastHolder();
            holder.image = (ImageView) row.findViewById(R.id.forecastListItemImage);
            holder.day = (TextView) row.findViewById(R.id.forecastListItemDay);
            holder.desc = (TextView) row.findViewById(R.id.forecastListItemDesc);
            holder.temp = (TextView) row.findViewById(R.id.forecastListItemTemp);

            row.setTag(holder);
        } else {
            holder = (ForecastHolder) row.getTag();
        }

        Forecast forecast = mForecasts.get(position);
        holder.image.setImageResource(forecast.getWeather().getIconRes());
        holder.temp.setText("" + forecast.getmDayTemp() + mTempUnit);
        holder.desc.setText(forecast.getWeather().getDesc());
        holder.day.setText(getDay(forecast.getWeather().getDay()));

        return row;
    }

    private String getDay(Weather.day d) {
        switch (d) {
            case MONDAY:
                return mContext.getResources().getString(R.string.monday);
            case TUESDAY:
                return mContext.getResources().getString(R.string.tuesday);
            case WEDNESDAY:
                return mContext.getResources().getString(R.string.wednesday);
            case THURSDAY:
                return mContext.getResources().getString(R.string.thursday);
            case FRIDAY:
                return mContext.getResources().getString(R.string.friday);
            case SATURDAY:
                return mContext.getResources().getString(R.string.saturday);
            case SUNDAY:
                return mContext.getResources().getString(R.string.sunday);
        }
        return "";
    }

    static class ForecastHolder {
        ImageView image;
        TextView day;
        TextView temp;
        TextView desc;
    }
}
