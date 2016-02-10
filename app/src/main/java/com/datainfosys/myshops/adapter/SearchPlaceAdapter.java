package com.datainfosys.myshops.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.datainfosys.myshops.R;
import com.datainfosys.myshops.web.PlaceAPI;

import java.util.ArrayList;


public class SearchPlaceAdapter extends ArrayAdapter<String> implements Filterable {

    private String LOGTAG = SearchPlaceAdapter.class.getName();

    ArrayList<String> resultList = new ArrayList<>();

    Context mContext;
    int mResource;

    PlaceAPI mPlaceAPI = new PlaceAPI();

    public SearchPlaceAdapter(Context context, int resource) {
        super(context, resource);

        mContext = context;
        mResource = resource;
    }

    @Override
    public int getCount() {
        // Last item will be the footer
        return resultList.size();
    }

    @Override
    public String getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    resultList = mPlaceAPI.autocomplete(constraint.toString());

                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        try{
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.place_search_adapter, parent, false);
            TextView autocompleteTextView = (TextView) view.findViewById(R.id.place_name);
            if(resultList.get(position) != null){
                autocompleteTextView.setText(resultList.get(position));
            }
        }catch(Exception e){
            Log.e(LOGTAG, e.getMessage());
        }

        return view;
    }

}