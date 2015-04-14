package android.weather.rob.org.weather.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.weather.rob.org.weather.R;
import android.weather.rob.org.weather.utility.DrawerItem;
import android.weather.rob.org.weather.utility.Forecast;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by guillaume on 14-04-15.
 */
public class DrawerlistAdapter extends ArrayAdapter<DrawerItem> {
    private Context mContext;
    private DrawerItem[] mItems;

    public DrawerlistAdapter(Context context, int resource, DrawerItem[] objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mItems = objects;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView itemIcon = (ImageView) convertView.findViewById(R.id.drawer_item_icon);
        TextView itemTitle = (TextView) convertView.findViewById(R.id.drawer_item_title);

        itemIcon.setImageResource(mItems[position].icon);
        itemTitle.setText(mItems[position].title);


        return convertView;
    }
}
