package android.weather.rob.org.weather.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.weather.rob.org.weather.R;
import android.weather.rob.org.weather.utility.Forecast;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by guillaume on 12-04-15.
 */
public class ForecastListAdapter extends ArrayAdapter<Forecast> {
    Context mContext;
    int mLayoutResourceId;
    List<Forecast> mForecasts = null;
    ForecastHolder holder = null;

    public ForecastListAdapter(Context context, int resource, List<Forecast> forecasts) {
        super(context, resource, forecasts);
        mContext = context;
        mLayoutResourceId = resource;
        mForecasts = forecasts;
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
        holder.image.setImageBitmap(forecast.getWeather().getIcon());
        holder.temp.setText("" + forecast.getmDayTemp()+"°C");
        holder.desc.setText(forecast.getWeather().getmDesc());
        holder.day.setText(forecast.getWeather().getDay());

        return row;
    }

    static class ForecastHolder {
        ImageView image;
        TextView day;
        TextView temp;
        TextView desc;
    }
}
