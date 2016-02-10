package com.datainfosys.myshops;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.datainfosys.myshops.data.InfoShop;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends Fragment{

    /*MyShopsApplication application= null;
    ArrayList<InfoShop> listShops= null;
    private ListView listView;
    private Context mContext= null;




    private class ViewHolder{
        TextView txtShopName= null;
        TextView txtShopAddress= null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        application= (MyShopsApplication) getApplication();
        mContext= this;
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
        listView= (ListView) findViewById(R.id.list_shops);
        //ArrayAdapter<InfoShop> adapter= new ArrayAdapter<InfoShop>(this, android.R.layout.simple_list_item_2, arrShops);
        ShopAdapter adapter= new ShopAdapter(this, listShops);
        listView.setAdapter(adapter);

        *//*if(checkForLocationServices(this))
        {

            mGoogleApiClient = new GoogleApiClient
                    .Builder( this )
                    .enableAutoManage( this, 0, this )
                    .addApi( Places.GEO_DATA_API )
                    .addApi( Places.PLACE_DETECTION_API )
                    .addConnectionCallbacks( this )
                    .addOnConnectionFailedListener( this )
                    .build();
        }*//*


    }





    private class ShopAdapter extends ArrayAdapter<InfoShop>
    {
        LayoutInflater mInflater=null;

        public ShopAdapter(Context context,ArrayList<InfoShop> listShops) {
            super(context,0, listShops);
            mInflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case LOCATION_SETTINGS:
                if(checkForLocationServices(this))
                {

                }
                break;

            case REQUEST_CODE_RECOVER_PLAY_SERVICES: {
                if (resultCode == RESULT_OK) {
                    // Make sure the app is not already connected or attempting to connect
                    if (!mGoogleApiClient.isConnecting() &&
                            !mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.connect();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(mContext, "Google Play Services must be installed.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }*/
}
