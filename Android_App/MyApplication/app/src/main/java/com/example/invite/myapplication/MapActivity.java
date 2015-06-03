package com.example.invite.myapplication;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public /*abstract*/ class MapActivity extends ListActivity implements OnMapReadyCallback/*, LocationListener*/ {

    private static final String TAG = "MyActivity";
    LocationManager lm;
    double myLongitude;
    double myLatitude;

    private static String theme = "amusement";

    private static String serverIP = "192.168.43.214";
    private static String url = "http://"+serverIP+":8080/placesselection?theme="+theme;

    private ProgressDialog pDialog;
    private static final String TAG_LAT = "latitude";
    private static final String TAG_LONG = "longitude";
    private static final String TAG_DESCR = "description";
    JSONArray contacts = null;
    ArrayList<HashMap<String, String>> contactList;
    HashMap<String, Marker> markerList = new HashMap<String,Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        contactList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        // Calling async task to get json

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int position, long id) {

                HashMap<String, String> myHashMap = (HashMap<String, String>) getListAdapter().getItem(position);
                String description = myHashMap.get(TAG_DESCR);

                if(markerList.containsKey(description)) {
                    markerList.get(description).remove();
                    markerList.remove(description);
                    Toast.makeText(getApplicationContext(), "Place : "+description+" successfully removed ! ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Place : "+description+" is not already selected", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
       */
    }

    @Override
    protected void onPause() {
        super.onPause();
        //lm.removeUpdates(this);
    }

    /*
    @Override // Location Management
    public void onLocationChanged(Location location) {
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();


        String msg = String.format(
                getResources().getString(R.string.new_location), myLatitude,
                myLongitude);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }
    */

    /*
    @Override // Location Management
    public void onProviderDisabled(String provider) {
        String msg = String.format(
                getResources().getString(R.string.provider_disabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    */
    /*
    @Override
    public void onProviderEnabled(String provider) {
        String msg = String.format(
                getResources().getString(R.string.provider_enabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    */

    /*
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        String newStatus = "";
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                newStatus = "OUT_OF_SERVICE";
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                newStatus = "TEMPORARILY_UNAVAILABLE";
                break;
            case LocationProvider.AVAILABLE:
                newStatus = "AVAILABLE";
                break;
        }
        String msg = String.format(
                getResources().getString(R.string.provider_disabled), provider,
                newStatus);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    */

    @Override
    public void onMapReady(GoogleMap map) {

        new GetContacts().execute();

        LatLng myPosition = new LatLng(45.799844,4.885732);
        //LatLng partDieu = new LatLng(45.759844,4.855732);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 10));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        /*
        map.addMarker(new MarkerOptions()
                .position(myPosition)
                .title("You are here")
                .snippet(":)")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        map.addMarker(new MarkerOptions()
                .position(partDieu)
                .title("Centre Commercial Part Dieu")
                .snippet("Le centre commercial le plus populaire de Lyon avec de nombreuses marques")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        */

    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MapActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            /*
            String jsonStr =

                    "[{\"adresse_exacte\":\"\",\"description\":\"test lieu 1 (lyon)\",\"longitude\":" +
                            "4.833,\"latitude\":45.767,\"id_lieu\":1,\"nom_theme\":\"culture\"},{\"adresse_exacte\":" +
                            "\"\",\"description\":\"test lieu 3 (montpellier)\",\"longitude\":3.883,\"latitude\":43.6,\"id_lieu\":" +
                            "3,\"nom_theme\":\"culture\"}]";
            */

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject wholeJson = new JSONObject(jsonStr);

                    String jsonPlacesStr = wholeJson.getString("content");

                    Log.d("Response (places) : ", "> " + jsonPlacesStr);

                    //JSONArray contacts = new JSONArray(jsonStr);
                    JSONArray contacts = new JSONArray(jsonPlacesStr);

                    for (int i = 0; i < contacts.length(); i++) {

                        JSONObject d = contacts.getJSONObject(i);

                        String longitude = d.getString(TAG_LONG);
                        String latitude = d.getString(TAG_LAT);
                        String description = d.getString(TAG_DESCR);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        contact.put(TAG_LONG,longitude);
                        contact.put(TAG_LAT, latitude);
                        contact.put(TAG_DESCR, description);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MapActivity.this, contactList,
                    R.layout.list_item, new String[] { TAG_DESCR, TAG_LAT,
                    TAG_LONG }, new int[] { R.id.place_name,
                    R.id.latitude_value, R.id.longitude_value });

            setListAdapter(adapter);
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        HashMap<String, String> myHashMap = (HashMap<String, String>) getListAdapter().getItem(position);
        String longitude = myHashMap.get(TAG_LONG);
        String latitude = myHashMap.get(TAG_LAT);
        String description = myHashMap.get(TAG_DESCR);

        LatLng placePosition = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));

        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        Marker marker = map.addMarker(new MarkerOptions()
                .position(placePosition)
                .title(description)
                .snippet(":)")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        markerList.put(description,marker);

        Toast.makeText(getApplicationContext(), "Place : "+description+" successfully added to map ! ", Toast.LENGTH_LONG).show();
    }

}