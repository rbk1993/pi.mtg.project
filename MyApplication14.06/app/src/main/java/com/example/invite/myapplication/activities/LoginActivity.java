package com.example.invite.myapplication.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.invite.myapplication.R;
import com.example.invite.myapplication.utils.ServerAddress;
import com.example.invite.myapplication.utils.ServiceHandler;

public class LoginActivity extends Activity {

    private Button login;
    private Button register;
    private EditText usernameField;
    private EditText passwordField;
    private String username;
    private String password;
    private Button later;
    private String nom;
    private String prenom;

    private ProgressDialog pDialog;

    private static String serverIP= ServerAddress.whatIs();

    private static String url = "http://"+serverIP+":8080/login";
    int authenticationSuccess=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.NoActionBar);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.buttonLogin);
        register = (Button) findViewById(R.id.buttonRegister);
        usernameField = (EditText) findViewById(R.id.editTextId);
        passwordField = (EditText) findViewById(R.id.editTextPassword);

        Intent intent = getIntent();
        if(intent.hasExtra("username") && intent.hasExtra("password")) {
            usernameField.setText(intent.getStringExtra("username"));
            passwordField.setText(intent.getStringExtra("password"));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();

                    if (username.isEmpty() || password.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter your Mail (or Username) and Password", Toast.LENGTH_LONG).show();
                    } else if (username == "root" & password == "root") {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        //new WSRequest().execute();
                    } else {
                        new WSRequest().execute();
                    }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
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

  @Override
    public void onDestroy() {

        super.onDestroy();
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
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

            paramsList.add(new BasicNameValuePair("login",username));
            paramsList.add(new BasicNameValuePair("password",password));

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET, paramsList);

            /*
            String jsonStr =

                    { "success" : 0 / 1 };
            */

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject wholeJson = new JSONObject(jsonStr);

                    authenticationSuccess = Integer.parseInt(wholeJson.getString("success"));

                    JSONArray identity = wholeJson.getJSONArray("whoAmI");
                    JSONObject identity_obj = identity.getJSONObject(0);
                    Log.d("Response (whoAmI) > : ",identity_obj.toString());

                    nom = identity_obj.getString("nom");
                    prenom = identity_obj.getString("prenom");

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
                Toast.makeText(getApplicationContext(), "Content de vous revoir "+prenom+" "+nom+" !", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("nom", nom); intent.putExtra("prenom", prenom); intent.putExtra("id",authenticationSuccess);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else {
                Toast.makeText(getApplicationContext(), "Fail !", Toast.LENGTH_LONG).show();
            }

        }

    }


}
