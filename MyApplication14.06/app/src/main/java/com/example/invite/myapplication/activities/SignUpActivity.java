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
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.invite.myapplication.utils.ServerAddress;
import com.example.invite.myapplication.R;
import com.example.invite.myapplication.utils.ServiceHandler;

public class SignUpActivity extends Activity {

    private Button accept;
    private Button cancel;

    NumberPicker np1;
    NumberPicker np2;
    NumberPicker np3;

    int dayBirth;
    int monthBirth;
    int yearBirth;
    String dateBirth;

    private TextView textView;
    private EditText loginField;
    private EditText passwordField;
    private EditText passwordCField;
    private EditText prenomField;
    private EditText nomField;
    private EditText mailField;
    private EditText mailCField;
    private EditText phoneField;
    private ProgressDialog progressDialog;


    private String username;
    private String password;
    private String confirmpassword;
    private String firstname;
    private String lastname;
    private String phonenumber;
    private String confirmemail;
    private String email;

    private static String serverIP= ServerAddress.whatIs();
    int inscSuccess = 0;

    private static String url = "http://"+serverIP+":8080/signup";


    private String action = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.NoActionBar);

        setContentView(R.layout.activity_signup);
        textView = (TextView) findViewById(R.id.label);
        accept = (Button) findViewById(R.id.button_validate);
        cancel = (Button) findViewById(R.id.button_cancel);
        loginField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        passwordCField = (EditText) findViewById(R.id.ConfirmPassword);
        mailCField = (EditText) findViewById(R.id.ConfirmEmail);

        prenomField = (EditText) findViewById(R.id.prenom);
        nomField = (EditText) findViewById(R.id.nom);
        phoneField = (EditText) findViewById(R.id.phonenumber);
        mailField = (EditText) findViewById(R.id.email);
        np1 = (NumberPicker) findViewById(R.id.numberPicker);
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

        np1.setValue(1);
        np2.setValue(1);
        np3.setValue(1980);

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
        accept.setOnClickListener(new View.OnClickListener() {

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
                    username = loginField.getText().toString();
                    firstname = prenomField.getText().toString();
                    lastname = nomField.getText().toString();
                    phonenumber = phoneField.getText().toString();
                    email = mailField.getText().toString();
                    confirmemail = mailCField.getText().toString();
                    password = passwordField.getText().toString();
                    confirmpassword = passwordCField.getText().toString();
                    new WSRequest().execute();
                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
                startActivity(i);
                finish();
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

    /**
     * Async task class to get json by making HTTP call
     * */
    private class WSRequest extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(SignUpActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

            paramsList.add(new BasicNameValuePair("nom", lastname));
            paramsList.add(new BasicNameValuePair("prenom", firstname));
            paramsList.add(new BasicNameValuePair("date_naissance", dateBirth));
            paramsList.add(new BasicNameValuePair("login", username));
            paramsList.add(new BasicNameValuePair("mail", email));
            paramsList.add(new BasicNameValuePair("password", password));
            paramsList.add(new BasicNameValuePair("telephone", phonenumber));

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
                Toast.makeText(getApplicationContext(), "Erreur. Vérifiez votre connexion.", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if(inscSuccess != 0) {
                Toast.makeText(getApplicationContext(), "Vous êtes enregistré !", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class/*ThemesActivity.class*/);
                intent.putExtra("username",username);
                intent.putExtra("password",password);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Erreur. Votre login ou mail existe déjà.", Toast.LENGTH_LONG).show();
            }

        }

    }





}
