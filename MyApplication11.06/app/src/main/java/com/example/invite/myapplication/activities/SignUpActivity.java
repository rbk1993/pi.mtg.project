package com.example.invite.myapplication.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.invite.myapplication.R;
import com.example.invite.myapplication.utils.ServerAddress;
import com.example.invite.myapplication.utils.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SignUpActivity extends Activity {

    NumberPicker np1;
    NumberPicker np2;
    NumberPicker np3;

    int dayBirth;
    int monthBirth;
    int yearBirth;
    String dateBirth;

    private EditText nomField;
    private EditText prenomField;
    private EditText phoneField;
    private EditText mailField;
    private EditText mailCField;
    private EditText passwordField;
    private EditText passwordCField;
    private EditText loginField;

    private String nom;
    private String prenom;
    private String phone;
    private String mail;
    private String mailC;
    private String password;
    private String passwordC;
    private String login;

    private Button signUp;

    private ProgressDialog pDialog;
    private static String serverIP= ServerAddress.whatIs();
    //private static String serverIP="192.168.137.1"; //PC HP Virtual Router
    //private static String serverIP="192.168.75.1"; //PC HP Connectify
    //private static String serverIP = "192.168.75.104";//PC Dell Connectify
    //private static String serverIP = "192.168.43.214";//PC Dell Wifi Mobile
    private static String url = "http://"+serverIP+":8080/signup";

    int inscSuccess = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.NoActionBar);
        setContentView(R.layout.activity_sign_up);

        nomField = (EditText) findViewById(R.id.editText_Nom);
        prenomField = (EditText) findViewById(R.id.editText_Prenom);
        phoneField = (EditText) findViewById(R.id.editText_phone);
        mailField = (EditText) findViewById(R.id.editText_mail);
        mailCField = (EditText) findViewById(R.id.editText_mailc);
        passwordField = (EditText) findViewById(R.id.editText_password);
        passwordCField = (EditText) findViewById(R.id.editText_password_c);
        loginField = (EditText) findViewById(R.id.editText_login);
        signUp = (Button) findViewById(R.id.button_signup);
        np1 = (NumberPicker) findViewById(R.id.numberPicker1);
        np2 = (NumberPicker) findViewById(R.id.numberPicker2);
        np3 = (NumberPicker) findViewById(R.id.numberPicker3);

        np1.setMinValue(1);
        np1.setMaxValue(31);
        np2.setMinValue(1);
        np2.setMaxValue(12);
        np3.setMinValue(1920);
        np3.setMaxValue(2010);

        np1.setWrapSelectorWheel(false);
        np2.setWrapSelectorWheel(false);
        np3.setWrapSelectorWheel(false);

        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                          @Override
                                          public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                              dayBirth = newVal;
                                          }
                                      }

        );
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                          @Override
                                          public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                              monthBirth = newVal;
                                          }
                                      }

        );
        np3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                          @Override
                                          public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                              yearBirth = newVal;
                                          }
                                      }

        );

        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(prenomField.getText().toString().isEmpty() || nomField.getText().toString().isEmpty() ||
                        mailField.getText().toString().isEmpty() || passwordField.getText().toString().isEmpty() ||
                        phoneField.getText().toString().isEmpty() || loginField.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill all required fields", Toast.LENGTH_LONG).show();
                }
                else if (!mailField.getText().toString().equals(mailCField.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Mail and Mail Confirmation don't match !", Toast.LENGTH_LONG).show();
                } else if (!passwordField.getText().toString().equals(passwordCField.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Password and Password Confirmation don't match !", Toast.LENGTH_LONG).show();
                } else {
                    dateBirth = yearBirth+"-"+monthBirth+"-"+dayBirth;
                    nom = nomField.getText().toString();
                    prenom = prenomField.getText().toString();
                    phone = phoneField.getText().toString();
                    mail = mailField.getText().toString();
                    mailC = mailCField.getText().toString();
                    password = passwordField.getText().toString();
                    passwordC = passwordCField.getText().toString();
                    new WSRequest().execute();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
            pDialog = new ProgressDialog(SignUpActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

            paramsList.add(new BasicNameValuePair("nom", nom));
            paramsList.add(new BasicNameValuePair("prenom", prenom));
            paramsList.add(new BasicNameValuePair("date_naissance", dateBirth));
            paramsList.add(new BasicNameValuePair("login", login));
            paramsList.add(new BasicNameValuePair("mail", mail));
            paramsList.add(new BasicNameValuePair("password", password));
            paramsList.add(new BasicNameValuePair("telephone", phone));

            for (int i=0;i<paramsList.size();i++){
                Log.d("Name (paramsList): ", "> " + paramsList.get(i).getName());
                Log.d("Value (paramsList): ", "> " + paramsList.get(i).getValue());
            }

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET, paramsList);



            if (jsonStr != null) {
                try {

                    JSONObject wholeJson = new JSONObject(jsonStr);

                    inscSuccess = Integer.parseInt(wholeJson.getString("inscription_success"));

                    Log.d("Response (success) : ", "> " + inscSuccess);

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

            if(inscSuccess != 0) {
                Toast.makeText(getApplicationContext(), "You are registered ! Redirecting ...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class/*MainPageActivity.class*/);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Failed to register you, maybe the login or mail already exists", Toast.LENGTH_LONG).show();
            }

        }

    }

}
