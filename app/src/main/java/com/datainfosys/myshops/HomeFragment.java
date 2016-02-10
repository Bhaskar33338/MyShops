package com.datainfosys.myshops;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.datainfosys.myshops.data.InfoShop;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener{
    private OnFragmentInteractionListener mListener;
    private MyShopsApplication application=null;
    private Context mContext;
    private ArrayList<InfoShop> listShops= null;
    private ListView listView=null;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        InfoShop item= listShops.get(position);
        startActivity(new Intent(mContext, NewOrderActivity.class));
    }


    private class ViewHolder{
        TextView txtShopName= null;
        TextView txtShopAddress= null;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        application= (MyShopsApplication) getActivity().getApplication();
        mContext= getActivity();
        InfoShop[] arrShops= new InfoShop[]{
                new InfoShop(1, "Bhartiya Bakery", "Dalda Factory Rd, Nalanda Vihar " + "0141 276 3669"),
                new InfoShop(1, "Bharat Bakers", "Tonk Road, Durgapur, Near Durgapura, Durgapura, Jaipur, Rajasthan 302015"),
                new InfoShop(1, "Manglam Arts", "Tonk Road, Durgapur, Near Durgapura, Durgapura, Jaipur, Rajasthan 302015"),
                new InfoShop(1, "Sunrise Optical", "299, Mahaveer Nagar IInd, Opp. Petrol Pump, Maharani Farm Durgapura, Jaipur, Rajasthan 302018"),
        };

        //listShops.addAll(Arrays.asList(arrShops));

        listShops= new ArrayList<InfoShop>();
        listShops.add(new InfoShop(1, "Bhartiya Bakery", "Dalda Factory Rd, Nalanda Vihar " + "0141 276 3669"));
        listShops.add(new InfoShop(2, "Bharat Bakers", "Tonk Road, Durgapur, Near Durgapura, Durgapura, Jaipur, Rajasthan 302015"));
        listShops.add(new InfoShop(3, "Manglam Arts", "Tonk Road, Durgapur, Near Durgapura, Durgapura, Jaipur, Rajasthan 302015"));
        listShops.add(new InfoShop(4, "Sunrise Optical", "299, Mahaveer Nagar IInd, Opp. Petrol Pump, Maharani Farm Durgapura, Jaipur, Rajasthan 302018"));
        listView= (ListView) view.findViewById(R.id.list_shops);
        //ArrayAdapter<InfoShop> adapter= new ArrayAdapter<InfoShop>(this, android.R.layout.simple_list_item_2, arrShops);
        ShopAdapter adapter= new ShopAdapter(mContext, listShops);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class ShopAdapter extends ArrayAdapter<InfoShop>
    {
        LayoutInflater mInflater=null;

        public ShopAdapter(Context context,ArrayList<InfoShop> listShops) {
            super(context, 0, listShops);
            mInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            InfoShop item= getItem(position);
            if(convertView==null)
            {
                convertView = mInflater.inflate(R.layout.row_shop, null);
                viewHolder= new ViewHolder();
                viewHolder.txtShopName= (TextView) convertView.findViewById(R.id.txtShopName);
                viewHolder.txtShopAddress= (TextView) convertView.findViewById(R.id.txtShopAddress);
                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.txtShopName.setText(item.getShopName());
            viewHolder.txtShopAddress.setText(item.getShopAddress());

            return convertView;
        }

        @Override
        public InfoShop getItem(int position) {
            return super.getItem(position);
        }


    }

}
