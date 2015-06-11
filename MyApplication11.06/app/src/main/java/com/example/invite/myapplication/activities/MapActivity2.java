package com.example.invite.myapplication.activities;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.invite.myapplication.R;
import com.example.invite.myapplication.adapter.NavDrawerListAdapter;
import com.example.invite.myapplication.model.NavDrawerItem;
import com.example.invite.myapplication.utils.ServerAddress;
import com.example.invite.myapplication.utils.ServiceHandler;
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

public /*abstract*/ class MapActivity2 extends ListActivity implements OnMapReadyCallback/*, LocationListener*/ {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    private static final String TAG = "MyActivity";
    LocationManager lm;
    double myLongitude;
    double myLatitude;
    ArrayList<String> listLongitudes = new ArrayList<String>();
    ArrayList<String> listLatitudes = new ArrayList<String>();
    ArrayList<String> listTitle = new ArrayList<String>();
    ArrayList<String> listOfId = new ArrayList<String>();

    private static String theme;
    private static String serverIP= ServerAddress.whatIs();
    //private static String serverIP="192.168.137.1"; //PC HP Virtual Router
    //private static String serverIP="192.168.75.1"; //PC HP Connectify
    //private static String serverIP = "192.168.75.104";//PC Dell Connectify
    //private static String serverIP = "192.168.43.214";//PC Dell Wifi Mobile
    private static String url;

    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_LONG = "longitude";
    public static final String EXTRA_LAT = "latitude";

    public static final String TAG_PACKAGE = "com.example.invite.myapplication";
    private ProgressDialog pDialog;

    private static final String TAG_LAT = "latitude";
    private static final String TAG_LONG = "longitude";
    private static final String TAG_DESCR = "typedetail";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_OPEN = "opening";

    JSONArray contacts = null;
    ArrayList<HashMap<String, String>> contactList;
    HashMap<String, Marker> markerList = new HashMap<String,Marker>();

    Button button_creer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));


        // Recycle the typed array
        navMenuIcons.recycle();

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                //R.drawable.geo_fence_24, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            //displayView(0);
        }

        Intent data = getIntent();

        /*
        if(data.hasExtra(EXTRA_DESCRIPTION) && data.hasExtra(EXTRA_LONG) && data.hasExtra(EXTRA_LAT) && data.hasExtra(EXTRA_ID)) {
            listTitle = data.getStringArrayListExtra(TAG_PACKAGE+EXTRA_DESCRIPTION);
            listLongitudes = data.getStringArrayListExtra(TAG_PACKAGE+EXTRA_LONG);
            listLatitudes = data.getStringArrayListExtra(TAG_PACKAGE+EXTRA_LAT);
            listOfId = data.getStringArrayListExtra(TAG_PACKAGE+EXTRA_ID);
        }
        */

        theme = data.getStringExtra("theme");

        url = "http://"+serverIP+":8080/placesselection?theme="+theme;

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        contactList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        // Calling async task to get json

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {

                HashMap<String, String> myHashMap = (HashMap<String, String>) getListAdapter().getItem(position);
                String id_lieu = myHashMap.get(TAG_ID);

                if (markerList.containsKey(id_lieu)) {
                    markerList.get(id_lieu).remove();
                    markerList.remove(id_lieu);
/*
                    int i = listOfId.indexOf(id_lieu);


                    listOfId.remove(id);
                    listLongitudes.remove(i);
                    listLatitudes.remove(i);
                    listTitle.remove(i);
*/
                    Toast.makeText(getApplicationContext(), "Place : " + id + " successfully removed ! ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Place : " + id + " is not already selected", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        button_creer = (Button) findViewById(R.id.button_creergroupe);

        button_creer.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MapActivity2.this, CreateGroupActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(MapActivity2.this, MainPageActivity.class);

        /*
        if(listOfId.size() > 0) {
            a.putStringArrayListExtra(TAG_PACKAGE+EXTRA_LONG, listLongitudes); //list of longitudes of places selected
            a.putStringArrayListExtra(TAG_PACKAGE+EXTRA_LAT, listLatitudes); //list of latitudes of places selected
            a.putStringArrayListExtra(TAG_PACKAGE+EXTRA_DESCRIPTION, listTitle); //list of descriptions of places selected
            a.putStringArrayListExtra(TAG_PACKAGE+EXTRA_ID, listOfId); //list of id of places selected
        }*/
        setResult(RESULT_OK, a);
        finish();
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

    @Override
    public void onMapReady(GoogleMap map) {

        LatLng myPosition = new LatLng(45.799844,4.885732);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 8));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
/*
        if(listOfId.size() > 0) {
            for (String identifier : listOfId) {

                int index = listOfId.indexOf(identifier);

                LatLng placePosition = new LatLng(Double.parseDouble(listLatitudes.get(index)), Double.parseDouble(listLongitudes.get(index)));

                Marker marker = map.addMarker(new MarkerOptions()
                        .position(placePosition)
                        .title(listTitle.get(index))
                        .snippet(listTitle.get(index))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                markerList.put(identifier, marker);
            }
        }
*/
        new GetContacts().execute();

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "Start Drag !", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "End Drag !", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "Dragging !", Toast.LENGTH_LONG).show();

            }
        });

    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MapActivity2.this);
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
                        String open = d.getString(TAG_OPEN);
                        String name = d.getString(TAG_NAME);
                        String id = d.getString(TAG_ID);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        contact.put(TAG_LONG,longitude);
                        contact.put(TAG_LAT, latitude);
                        contact.put(TAG_NAME, name);

                        // ---added
                        contact.put(TAG_DESCR, description);
                        contact.put(TAG_OPEN, open);
                        contact.put(TAG_ID, id);

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

/*
             ListAdapter adapter = new SimpleAdapter(
                    MapActivity.this, contactList,
                    R.layout.list_item, new String[] { TAG_NAME, TAG_LAT,
                    TAG_LONG }, new int[] { R.id.name_value,
                    R.id.latitude_value, R.id.longitude_value });
*/

            ListAdapter adapter2 = new SimpleAdapter(
                    MapActivity2.this, contactList,
                    R.layout.list_item, new String[] {TAG_NAME, TAG_LAT,
                    TAG_LONG, TAG_DESCR, TAG_OPEN, TAG_ID }, new int[] {
                    R.id.name_value,R.id.latitude_value, R.id.longitude_value,
                    R.id.descr_value,R.id.open_value, R.id.id_value});

            setListAdapter(adapter2);
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        HashMap<String, String> myHashMap = (HashMap<String, String>) getListAdapter().getItem(position);

        String longitude = myHashMap.get(TAG_LONG);
        String latitude = myHashMap.get(TAG_LAT);
        String name = myHashMap.get(TAG_NAME);
        String description = myHashMap.get(TAG_DESCR);
        String open = myHashMap.get(TAG_OPEN);
        String id_lieu = myHashMap.get(TAG_ID);

        //We have to replace description of place by id : "More unique...".
        if(!markerList.containsKey(id_lieu)){
        //if (!markerList.containsKey(name)) {

            LatLng placePosition = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

            GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

            Marker marker = map.addMarker(new MarkerOptions()
                    .position(placePosition)
                    .title(name)
                    .snippet(description + " " + open)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .draggable(true));

            //markerList.put(name, marker);
            markerList.put(id_lieu, marker);
/*
            listLongitudes.add(String.valueOf(marker.getPosition().longitude));
            listLatitudes.add(String.valueOf(marker.getPosition().latitude));
            listTitle.add(marker.getTitle());
            listOfId.add(id_lieu);
*/

            Toast.makeText(getApplicationContext(), "Place : " + name + " successfully added to map ! ", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "You already selected this place !", Toast.LENGTH_LONG).show();
        }
    }
}