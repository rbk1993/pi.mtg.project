package pi.mytourguide.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pi.mytourguide.R;
import pi.mytourguide.utils.ServerAddress;

public class AuthentificationActivity extends ActionBarActivity {

    private Button login;
    private Button register;
    private EditText usernameField;
    private EditText passwordField;
    private String username;
    private String password;
    private Button later;

    private ProgressDialog pDialog;

    private static String serverIP= ServerAddress.whatIs();

    private static String url = "http://"+serverIP+":8080/login";
    int authenticationSuccess=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //MyApplication.myIdentity=null;
        //MyApplication.currentGroup=null;

        setContentView(R.layout.activity_authentification);

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
                } else {
                    new WSRequest().execute();
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
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
            pDialog = new ProgressDialog(AuthentificationActivity.this);
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
                Intent intent = new Intent(AuthentificationActivity.this, MainActivity.class/*MainPageFragment.class*/);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else {
                Toast.makeText(getApplicationContext(), "Fail !", Toast.LENGTH_LONG).show();
            }

        }

    }


}
