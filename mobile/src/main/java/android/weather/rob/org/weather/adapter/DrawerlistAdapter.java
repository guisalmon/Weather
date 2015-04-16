package android.weather.rob.org.weather.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.weather.rob.org.weather.R;
import android.weather.rob.org.weather.utility.DrawerItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by guillaume on 14-04-15.
 */
public class DrawerlistAdapter extends ArrayAdapter<DrawerItem> {
    private final Context mContext;
    private final DrawerItem[] mItems;
    private int mSelectedItem;

    public DrawerlistAdapter(Context context, DrawerItem[] objects) {
        super(context, R.layout.drawer_list_item, objects);
        this.mContext = context;
        this.mItems = objects;
    }

    public void setmSelectedItem(int mSelectedItem) {
        this.mSelectedItem = mSelectedItem;
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

        if (mSelectedItem == position) {
            itemTitle.setTypeface(Typeface.DEFAULT_BOLD);
            itemTitle.setTextColor(mContext.getResources().getColor(R.color.base_blue));
        } else {
            itemTitle.setTypeface(Typeface.DEFAULT);
            itemTitle.setTextColor(mContext.getResources().getColor(android.R.color.black));
        }


        return convertView;
    }
}
