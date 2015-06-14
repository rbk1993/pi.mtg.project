package com.example.invite.myapplication.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.invite.myapplication.utils.PathJSONParser;
import com.example.invite.myapplication.R;
import com.example.invite.myapplication.utils.HttpConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.OnMapReadyCallback;

public class PathGoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final LatLng LOWER_MANHATTAN = new LatLng(40.722543,
            -73.998585);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);
    private static final LatLng WALL_STREET = new LatLng(40.7064, -74.0094);

    final String TAG = "PathGoogleMapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_google_map);
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        fm.getMapAsync(this);

    }

     @Override
     public void onMapReady(GoogleMap googleMap){

         MarkerOptions options = new MarkerOptions();
         options.position(LOWER_MANHATTAN);
         options.position(BROOKLYN_BRIDGE);
         options.position(WALL_STREET);
         googleMap.addMarker(options);
         String url = getMapsApiDirectionsUrl();
         String key = "AIzaSyDoP4aHve8wYZyeR18G1NMr6KWOhS3dXRo";
         //String url = "https://maps.googleapis.com/maps/api/directions/json?origin=Chicago,IL&destination=Los+Angeles,CA&key="+key;
         ReadTask downloadTask = new ReadTask();
         downloadTask.execute(url);

         googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BROOKLYN_BRIDGE,
                 13));
         addMarkers(googleMap);

     }

    private String getMapsApiDirectionsUrl() {
        String origin = "origin="+LOWER_MANHATTAN.latitude + "," + LOWER_MANHATTAN.longitude;
        String waypoints = "waypoints=optimize:true|" + BROOKLYN_BRIDGE.latitude + "," + BROOKLYN_BRIDGE.longitude;
        String destination = "destination="+WALL_STREET.latitude + "," + WALL_STREET.longitude;
        String sensor = "sensor=false";
        String params = origin + "&" + waypoints + "&" + destination + "&" + sensor;
        String output = "json";
        String key = "key="+"@strings/API_KEY";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params + "?" + key ;
        return url;
    }

    private void addMarkers(GoogleMap googleMap) {
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(BROOKLYN_BRIDGE)
                    .title("First Point"));
            googleMap.addMarker(new MarkerOptions().position(LOWER_MANHATTAN)
                    .title("Second Point"));
            googleMap.addMarker(new MarkerOptions().position(WALL_STREET)
                    .title("Third Point"));
        }
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
            List<List<HashMap<String, String>>> routes = null;

            try {
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
            GoogleMap map = ((SupportMapFragment) getSupportFragmentManager()
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
                polyLineOptions.width(10);
                polyLineOptions.color(Color.BLUE);
            }

            map.addPolyline(polyLineOptions);
        }
    }
}
