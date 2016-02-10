package com.datainfosys.myshops.adapter;

/**
 * Created by sagar on 14/7/15.
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datainfosys.myshops.R;
import com.datainfosys.myshops.data.NavigationItem;

import java.util.ArrayList;

public class NavDrawerListAdapter extends ArrayAdapter<NavigationItem> {

    private Context context;
    private ArrayList<NavigationItem> navDrawerItems;

    public NavDrawerListAdapter(Context context, ArrayList<NavigationItem> navDrawerItems){
        super(context, R.layout.drawer_list_item, navDrawerItems);
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public NavigationItem getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());


        if(navDrawerItems.get(position).getTitle().equalsIgnoreCase("My Shop Orders"))
            txtTitle.setTextColor(Color.parseColor("#FF0000"));
        txtTitle.setText(navDrawerItems.get(position).getTitle());

        // displaying count
        // check whether it set visible or not
        /*if(navDrawerItems.get(position).getCounterVisibility()){
            txtCount.setText(navDrawerItems.get(position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }*/

        return convertView;
    }

}
