package project.smarttrip.mytourguide.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import project.smarttrip.mytourguide.R;
import project.smarttrip.mytourguide.utils.ServerAddress;
import project.smarttrip.mytourguide.utils.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Date;


public class CreateGroupActivity extends Activity {

    public static final String EXTRA_EXTRA_UID = "user_id";

    private String UID;

    private Button button_create;
    private EditText groupName;

    private EditText NbreMaxMembres;
    private ProgressDialog progressDialog;
    private TextView textViewNewGroup;

    NumberPicker np4;
    NumberPicker np5;
    NumberPicker np6;
    NumberPicker np7;
    NumberPicker np8;
    NumberPicker np9;
    NumberPicker np10;

    int jj, mm, aaaa, dureeH, dureeMin, horaireH, horaireMin;
    String date, duree, horaire;

    private String gn, dr, nb, pr;

    private String url_parcours, duree_trajet_s, duree_trajet_text;

    private int duree_format_h, duree_format_min;

    int successCreation = 0;
    int successRegistration = 0;

    private ProgressDialog pDialog;

    private static String serverIP= ServerAddress.whatIs();

    private static String url = "http://"+serverIP+":8080/creategroup";

    ArrayList<String> listOfPlaces = new ArrayList<String>();

    JSONArray jsonPlacesArray;
    JSONObject jsonPlacesObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Intent fromMap = getIntent();

        url_parcours = fromMap.getStringExtra("url_parcours");
        duree_trajet_text = fromMap.getStringExtra("text_duration");
        duree_trajet_s = fromMap.getStringExtra("value_duration");
        UID = fromMap.getStringExtra(EXTRA_EXTRA_UID);
        listOfPlaces = fromMap.getStringArrayListExtra("places_id");

        jsonPlacesObject = new JSONObject();
        jsonPlacesArray = new JSONArray();

        for(int i = 0;i<listOfPlaces.size();i++) {

            try {

                jsonPlacesArray.put( (new JSONObject()).put("id_lieu",listOfPlaces.get(i)));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String format_duree_trajet = String.format("%d heures, %d minutes",
                TimeUnit.SECONDS.toHours(Integer.parseInt(duree_trajet_s)),
                TimeUnit.SECONDS.toMinutes(Integer.parseInt(duree_trajet_s)) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(Integer.parseInt(duree_trajet_s)))
        );
        Toast.makeText(CreateGroupActivity.this, "votre trajet dure "+format_duree_trajet, Toast.LENGTH_SHORT).show();

        duree_format_h = (int) TimeUnit.SECONDS.toHours(Integer.parseInt(duree_trajet_s));

        duree_format_min = (int) ((int) TimeUnit.SECONDS.toMinutes(Integer.parseInt(duree_trajet_s)) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(Integer.parseInt(duree_trajet_s))));

        groupName = (EditText) findViewById(R.id.editTextGroupName);

        //groupName.setText(default text... = groupe de {pseudo utilisateur} ???)

        NbreMaxMembres = (EditText) findViewById(R.id.NombreMax);

        button_create = (Button) findViewById(R.id.addparcours);

        //DureeH
        np4 = (NumberPicker) findViewById(R.id.numberPicker4);

        //DureeM
        np5 = (NumberPicker) findViewById(R.id.numberPicker5);

        //jj
        np6 = (NumberPicker) findViewById(R.id.numberPicker6);

        //mm
        np7 = (NumberPicker) findViewById(R.id.numberPicker7);

        //aaaa
        np8 = (NumberPicker) findViewById(R.id.numberPicker8);

        //horaireH
        np9 = (NumberPicker) findViewById(R.id.numberPicker9);

        //horaireMn
        np10 = (NumberPicker) findViewById(R.id.numberPicker10);

        Calendar c = Calendar.getInstance();
        final int currentyear= c.get(Calendar.YEAR);
        final int currentmonth = c.get(Calendar.MONTH) + 1;
        final int currentday = c.get(Calendar.DAY_OF_MONTH);

        horaireMin = np10.getValue();
        np4.setMinValue(duree_format_h);
        np4.setMaxValue(23);
        np4.setValue(duree_format_h);
        np5.setMinValue(duree_format_min);
        np5.setMaxValue(59);
        np5.setValue(duree_format_min);
        np6.setMinValue(1);
        np6.setMaxValue(31);
        np6.setValue(currentday);
        np7.setMinValue(1);
        np7.setMaxValue(12);
        np7.setValue(currentmonth);
        np8.setMinValue(currentyear);
        np8.setMaxValue(currentyear + 4);
        np8.setValue(currentyear);
        np9.setMinValue(0);
        np9.setMaxValue(23);
        np9.setValue(12);
        np10.setMinValue(0);
        np10.setMaxValue(59);
        np10.setValue(0);

        dureeH = np4.getValue();
        dureeMin = np5.getValue();
        aaaa = np8.getValue();
        mm = np7.getValue();
        jj = np6.getValue();
        horaireH = np9.getValue();

        np4.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                          @Override
                                          public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                              dureeH = newVal;
                                          }
                                      }

        );
        np5.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                          @Override
                                          public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                              dureeMin = newVal;
                                          }
                                      }

        );
        np6.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                          @Override
                                          public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                              jj = newVal;
                                          }
                                      }

        );

        np7.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                          @Override
                                          public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                              mm = newVal;
                                          }
                                      }

        );
        np8.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                          @Override
                                          public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                              aaaa = newVal;
                                          }
                                      }

        );
        np9.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                          @Override
                                          public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                              horaireH = newVal;
                                          }
                                      }

        );

        np10.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               horaireMin = newVal;
                                           }
                                       }

        );


        button_create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                gn = groupName.getText().toString();
                nb = NbreMaxMembres.getText().toString();
                duree = dureeH + ":" + dureeMin;
                date = aaaa + "-" + mm + "-" + jj;
                horaire = horaireH + ":" + horaireMin;
                Date date_format=null;
                Date date_today_format=null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date_format = sdf.parse(date);
                    date_today_format = sdf.parse(currentyear+"-"+currentmonth+"-"+currentday);

                    Log.d("Current date : >",date_today_format.toString());
                    Log.d("Chosen date : >",date_format.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (groupName.getText().toString().isEmpty() || NbreMaxMembres.getText().toString().isEmpty()) {
                    Toast.makeText(CreateGroupActivity.this, "Veuillez remplir tous les champs requis", Toast.LENGTH_SHORT).show();
                } else if (date_format.before(date_today_format)){
                    Toast.makeText(CreateGroupActivity.this, "Date antérieure à aujourd'hui ! Recommencez", Toast.LENGTH_SHORT).show();
                }
                else {
                    new WSRequest().execute();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
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
            pDialog = new ProgressDialog(CreateGroupActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

            paramsList.add(new BasicNameValuePair("url",url_parcours));
            paramsList.add(new BasicNameValuePair("duree_parcours_heures",duree));
            paramsList.add(new BasicNameValuePair("date",date));
            paramsList.add(new BasicNameValuePair("horaire",horaire));
            paramsList.add(new BasicNameValuePair("nombre_membres_max",nb));
            paramsList.add(new BasicNameValuePair("nom_groupe",gn));
            paramsList.add(new BasicNameValuePair("user_id",UID));
            paramsList.add(new BasicNameValuePair("jsonobject",jsonPlacesArray.toString()));

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET, paramsList);


            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject wholeJson = new JSONObject (jsonStr);

                    successCreation = Integer.parseInt(wholeJson.getString("creation_success"));

                    successRegistration = Integer.parseInt(wholeJson.getString("registration_success"));

                    Log.d("Response (success) : ", "> " + successRegistration+" "+successCreation);

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

            if(successCreation != 0 && successRegistration != 0) {
                Toast.makeText(getApplicationContext(), "Groupe créé avec succès !", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CreateGroupActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else {
                Toast.makeText(getApplicationContext(), "Fail !", Toast.LENGTH_LONG).show();
            }

        }

    }


}