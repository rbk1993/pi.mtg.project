package com.example.invite.myapplication.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.invite.myapplication.R;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends Activity {

    private ProgressDialog pDialog;
    private static String serverIP= ServerAddress.whatIs();
    //private static String serverIP="192.168.137.1"; //PC HP Virtual Router
    //private static String serverIP="192.168.75.1"; //PC HP Connectify
    //private static String serverIP = "192.168.75.104";//PC Dell Connectify
    //private static String serverIP = "192.168.43.214";//PC Dell Wifi Mobile
    private static String url = "http://"+serverIP+":8080/login";
    EditText user_id;
    EditText password;
    Button button_login;
    Button button_inscription;
    int authenticationSuccess=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.NoActionBar);
        setContentView(R.layout.activity_login);

        RelativeLayout layout = (RelativeLayout) this.findViewById(R.id.rlayout_login);

        int sdk= Build.VERSION.SDK_INT;

        user_id = (EditText) findViewById(R.id.edittext_user_id);
        password = (EditText) findViewById(R.id.edittext_password);
        button_login = (Button) findViewById(R.id.button_login);
        button_inscription = (Button) findViewById(R.id.button_inscription);

        //layout.setBackgroundColor(Color.CYAN);

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        Log.d("Hour", String.valueOf(hour));
        if (hour > 5 && hour < 20) {
            user_id.setBackgroundColor(Color.WHITE);
            password.setBackgroundColor(Color.WHITE);
            user_id.setTextColor(Color.BLUE);
            password.setTextColor(Color.BLUE);

        } else {
            user_id.setBackgroundColor(Color.WHITE);
            password.setBackgroundColor(Color.WHITE);
            user_id.setTextColor(Color.BLUE);
            password.setTextColor(Color.BLUE);
        }

        button_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(user_id.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your Mail (or Username) and Password", Toast.LENGTH_LONG).show();
                } else {
                    new WSRequest().execute();
                }
            }
        });
        button_inscription.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });

            /*
            plustard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent (Premiere.this, Accueil.class);
                    startActivity(intent);
                }
            });

            button_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(user_id.getText().toString() == "" || password.getText().toString() == "") {
                        Toast.makeText(getApplicationContext(), "Please enter your Mail (or Username) and Password", Toast.LENGTH_LONG).show();
                    } else {
                        new WSRequest().execute();
                    }
                }
            });
            */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    /**
     * Async task class to get json by making HTTP call
     * */
    private class WSRequest extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Connecting to server"+ServerAddress.whatIs());
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

            paramsList.add(new BasicNameValuePair("login",user_id.getText().toString()));
            paramsList.add(new BasicNameValuePair("password", password.getText().toString()));

            //Log.d("Username and Password",user_id.getText().toString()+" "+password.getText().toString());

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET, paramsList);

            //Log.d("jsonStr >",jsonStr);

                /*
                String jsonStr =

                        { "success" : 0 / 1 };
                */

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject wholeJson = new JSONObject(jsonStr);

                    authenticationSuccess = Integer.parseInt(wholeJson.getString("success"));

                    Log.d("Response (success) : ", "> " + authenticationSuccess);

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

            if(authenticationSuccess != 0) {
                Toast.makeText(getApplicationContext(), "Success ! UserID ="+authenticationSuccess, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainPageActivity.class/*MainPageActivity.class*/);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Fail !", Toast.LENGTH_LONG).show();
            }

        }

    }
}
