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
 * Created by guillaume on 12-04-15.
 */
public class ForecastListAdapter extends ArrayAdapter<Forecast> {
    private Context mContext;
    private int mLayoutResourceId;
    private List<Forecast> mForecasts = null;
    private ForecastHolder holder = null;
    private String mTempUnit;


    public ForecastListAdapter(Context context, int resource, List<Forecast> forecasts, Weather.format format) {
        super(context, resource, forecasts);
        mContext = context;
        mLayoutResourceId = resource;
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
        holder.desc.setText(forecast.getWeather().getmDesc());
        holder.day.setText(getDay(forecast.getWeather().getDay()));

        return row;
    }

    private String getDay (Weather.day d){
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
