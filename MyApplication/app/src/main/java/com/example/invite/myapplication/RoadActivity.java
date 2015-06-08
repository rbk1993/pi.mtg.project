package com.example.invite.myapplication;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;



public class RoadActivity extends MapActivity {

    LinearLayout linearLayout;
    MapView mapView;
    private Road mRoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road);


        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);

        new Thread() {
            @Override
            public void run() {
                double fromLat = 49.85, fromLon = 24.016667, toLat = 50.45, toLon = 30.523333;
                String url = RoadProvider
                        .getUrl(fromLat, fromLon, toLat, toLon);
                InputStream is = getConnection(url);
                mRoad = RoadProvider.getRoute(is);
                mHandler.sendEmptyMessage(0);
            }
        }.start();


    }


    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            TextView textView = (TextView) findViewById(R.id.description);
            textView.setText(mRoad.mName + " " + mRoad.mDescription);
            MapOverlay mapOverlay = new MapOverlay(mRoad, mapView);
            List<Overlay> listOfOverlays = mapView.getOverlays();
            listOfOverlays.clear();
            listOfOverlays.add(mapOverlay);
            mapView.invalidate();
        }
    };



    private InputStream getConnection(String url) {
        InputStream is = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            is = conn.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_road, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
