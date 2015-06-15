package project.smarttrip.mytourguide.activity;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import project.smarttrip.mytourguide.R;
import project.smarttrip.mytourguide.adapter.PlaceListAdapter;
import project.smarttrip.mytourguide.model.PlaceItem;
import project.smarttrip.mytourguide.utils.HttpConnection;
import project.smarttrip.mytourguide.utils.PathJSONParser;
import project.smarttrip.mytourguide.utils.ServerAddress;
import project.smarttrip.mytourguide.utils.ServiceHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


public class MapActivity extends ListActivity implements OnMapReadyCallback/*, LocationListener*/ {

    private static final String TAG = "MyActivity";
    LocationManager lm;
    double myLongitude;
    double myLatitude;
    ListView lv;
    EditText inputSearch;
    ArrayList<PlaceItem> arrayPlaces = new ArrayList<PlaceItem>();
    Polyline oldPolyline;
    boolean isDrawingFinished;

    ArrayList<String> listLongitudes = new ArrayList<String>();
    ArrayList<String> listLatitudes = new ArrayList<String>();
    ArrayList<String> listTitle = new ArrayList<String>();
    ArrayList<String> listOfId = new ArrayList<String>();

    private static String url_parcours;
    private static boolean parcours_ready = false;
    private static String theme;

    private static String serverIP= ServerAddress.whatIs();

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
    ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
    TreeMap<String, Marker> markerList = new TreeMap<String,Marker>();

    Button button_creer;
    Button button_parcours;

    PlaceListAdapter placeAdapter;

    String text_duration="";
    String value_duration="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

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

        lv = getListView();
        //lv = (ListView) findViewById(R.id.list_places);
        inputSearch = (EditText) findViewById(R.id.searchInput);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {

                PlaceItem placeItem = arrayPlaces.get(position);
                String id_lieu = placeItem.getId();

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
        button_parcours = (Button) findViewById(R.id.button_genererparcours);

        button_creer.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (parcours_ready == true){
                    //Intent intent = new Intent(MapActivity.this, CreateGroupActivity.class);
                    //intent.putExtra("url_parcours",url_parcours);
                    //intent.putExtra("text_duration",text_duration);
                    //intent.putExtra("value_duration",value_duration);
                    //startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Veuillez générer un parcours d'abord", Toast.LENGTH_LONG).show();
                }

            }
        });




        button_parcours.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (markerList.size() >= 3) {
                    button_parcours.setClickable(false);
                    if(oldPolyline != null) {
                        oldPolyline.setVisible(false);
                    }
                    url_parcours = getMapsApiDirectionsUrl();
                    ReadTask downloadTask = new ReadTask();
                    downloadTask.execute(url_parcours);
                    parcours_ready=true;

                } else {
                    Toast.makeText(getApplicationContext(), "Parcours : Veuillez sélectionner au moins 3 lieux", Toast.LENGTH_LONG).show();
                }

            }

        });


        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text ["+s+"]");
                if(count < before) {
                    placeAdapter.resetData();
                }
                placeAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

/*
    @Override
    public void onBackPressed() {
        //Intent a = new Intent(MapActivity.this, ThemesActivity.class);


            if(listOfId.size() > 0) {
                a.putStringArrayListExtra(TAG_PACKAGE+EXTRA_LONG, listLongitudes); //list of longitudes of places selected
                a.putStringArrayListExtra(TAG_PACKAGE+EXTRA_LAT, listLatitudes); //list of latitudes of places selected
                a.putStringArrayListExtra(TAG_PACKAGE+EXTRA_DESCRIPTION, listTitle); //list of descriptions of places selected
                a.putStringArrayListExtra(TAG_PACKAGE+EXTRA_ID, listOfId); //list of id of places selected
            }
        setResult(RESULT_OK, a);
        finish();
    }
*/

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

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                for (String keys : markerList.keySet()) {
                    if (Math.abs(markerList.get(keys).getPosition().latitude - latLng.latitude) < 0.005 && Math.abs(markerList.get(keys).getPosition().longitude - latLng.longitude) < 0.005) {

                        Toast.makeText(MapActivity.this, "suppression...", Toast.LENGTH_SHORT).show(); //do some stuff

                        markerList.get(keys).remove(); // Supprime le Marker de la carte
                        markerList.remove(keys); // Supprime l'entrée du Marker dans la markerList
        /*
                            int i = listOfId.indexOf(id_lieu);


                            listOfId.remove(id);
                            listLongitudes.remove(i);
                            listLatitudes.remove(i);
                            listTitle.remove(i);
        */
                        Toast.makeText(getApplicationContext(), "Place successfully removed ! ", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }
        });
    }

    private String getMapsApiDirectionsUrl() {

        Double originLat = markerList.firstEntry().getValue().getPosition().latitude;
        Double originLon = markerList.firstEntry().getValue().getPosition().longitude;
        Double destLat = markerList.lastEntry().getValue().getPosition().latitude;
        Double destLon = markerList.lastEntry().getValue().getPosition().longitude;
        Double wayLat;
        Double wayLon;

        String origin = "origin="+ originLat + "," + originLon;
        String destination = "destination="+ destLat + "," + destLon;
        String waypoints = "waypoints=optimize:true";
        for (String id_lieu : markerList.keySet()) {

            //          if ( (markerList.firstEntry().getKey().equals(id_lieu))
            //                && (markerList.lastEntry().getKey().equals(id_lieu))) {

            wayLat = markerList.get(id_lieu).getPosition().latitude;
            wayLon = markerList.get(id_lieu).getPosition().longitude;

            waypoints += "|" + wayLat + "," + wayLon;
            //}

        }

        String sensor = "sensor=false";
        String params = origin + "&" + waypoints + "&" + destination + "&" + sensor;
        String output = "json";
        String key = "key="+"@strings/API_DIR_KEY";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params + "?" + key ;
        return url;
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String myString=url[0];
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            JSONObject myJsonObject;
            List<List<HashMap<String, String>>> routes = null;

            try {

                // Fonctions perso ...
                myJsonObject = new JSONObject(jsonData[0]);

                JSONArray myJsonArray = new JSONArray(myJsonObject.getString("routes"));

                JSONObject myJsonObject2 = myJsonArray.getJSONObject(0);

                JSONArray myJsonArray2 = new JSONArray(myJsonObject2.getString("legs"));

                JSONObject myJsonObject3 = myJsonArray2.getJSONObject(0);

                JSONObject myJsonObject4 = new JSONObject(myJsonObject3.getString("duration"));

                text_duration = myJsonObject4.getString("text");

                value_duration = myJsonObject4.getString("value");

                Log.d("Response (dureeparc) >","texte :"+text_duration+" et value :"+value_duration);

                // ... Fin fonctions perso

                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {

            GoogleMap map = ((MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map)).getMap();

            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            Log.d("routes.size() >",String.valueOf(routes.size()));

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(20);
                polyLineOptions.color(Color.RED);
            }

            button_parcours.setClickable(true);

            if(polyLineOptions != null) {
                Polyline line = map.addPolyline(polyLineOptions);
                oldPolyline = line;
            } else {
                Log.d("Error Polyline >", "polyLineOptions is null !");
            }
        }
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
                        //HashMap<String, String> contact = new HashMap<String, String>();

                        //contact.put(TAG_LONG,longitude);
                        //contact.put(TAG_LAT, latitude);
                        //contact.put(TAG_NAME, name);

                        // ---added
                        //contact.put(TAG_DESCR, description);
                        //contact.put(TAG_OPEN, open);
                        //contact.put(TAG_ID, id);

                        // adding contact to contact list
                        //contactList.add(contact);

                        arrayPlaces.add(new PlaceItem(name, description, open, longitude, latitude, id));
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

            /*

            ListAdapter adapter2 = new SimpleAdapter(
                    MapActivity.this, contactList,
                    R.layout.list_item, new String[] {TAG_NAME, TAG_LAT,
                    TAG_LONG, TAG_DESCR, TAG_OPEN, TAG_ID }, new int[] {
                    R.id.name_value,R.id.latitude_value, R.id.longitude_value,
                    R.id.descr_value,R.id.open_value, R.id.id_value});

            setListAdapter(adapter2);
*/

            placeAdapter = new PlaceListAdapter(MapActivity.this, arrayPlaces);
            setListAdapter(placeAdapter);
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //HashMap<String, String> myHashMap = (HashMap<String, String>) getListAdapter().getItem(position);

        PlaceItem placeItem = arrayPlaces.get(position);

        String longitude = placeItem.getLongitude();
        String latitude = placeItem.getLatitude();
        String name = placeItem.getTitle();
        String description = placeItem.getDescription();
        String open = placeItem.getOpen();
        String id_lieu = placeItem.getId();

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
                    .draggable(false));

            //markerList.put(name, marker);
            markerList.put(id_lieu, marker);
    /*
                listLongitudes.add(String.valueOf(marker.getPosition().longitude));
                listLatitudes.add(String.valueOf(marker.getPosition().latitude));
                listTitle.add(marker.getName());
                listOfId.add(id_lieu);
    */

            Toast.makeText(getApplicationContext(), "Place : " + name + " successfully added to map ! ", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "You already selected this place !", Toast.LENGTH_LONG).show();
        }
    }
}