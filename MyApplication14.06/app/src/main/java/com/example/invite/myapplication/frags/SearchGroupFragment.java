package com.example.invite.myapplication.frags;


import android.app.Activity;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.example.invite.myapplication.R;
import com.example.invite.myapplication.activities.ShowGroupActivity;
import com.example.invite.myapplication.model.PlaceItem;
import com.example.invite.myapplication.utils.ServiceHandler;
import com.example.invite.myapplication.utils.ServerAddress;

public class SearchGroupFragment extends Fragment {

    ListView groupeView;
    ListAdapter adapter2;
    public static ArrayList<HashMap<String, String>> listItem;//array of items

    public static SimpleAdapter adapter1 = null;

    int debutJ, debutM, debutA, finJ, finM, finA, debutHoraireH, debutHoraireM, finHoraireH, finHoraireM, dureeH, dureeM;
    int completeGroups = 0;
    String datedebut, datefin, horairedebut, horairefin, duree;

    ProgressDialog pDialog;

    private static final String TAG_DUREEH = "duree_parcours_heures";
    private static final String TAG_DATE = "date";
    private static final String TAG_HORAIRE = "horaire";
    private static final String TAG_NBMBMAX = "nombre_membres_max";
    private static final String TAG_NBMBINS = "nombre_membres_inscrits";
    private static final String TAG_NAME = "name";
    private static final String TAG_ID = "group_id";
    private static final String TAG_ADMID = "id_administrateur";
    private static final String TAG_USERID = "user_id";
    private static final String TAG_URL = "url";

    private static String serverIP = ServerAddress.whatIs();

    private static String url;

    JSONArray contacts = null;
    ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

    String user_id;

    public SearchGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_searchgroup, container, false);

        user_id = getArguments().getString("user_id");
        groupeView = (ListView) rootView.findViewById(R.id.listViewGroupe);
        listItem = new ArrayList<>();

        TabHost tabHost = (TabHost) rootView.findViewById(R.id.tabHost2);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Groupes");
        tabSpec.setContent(R.id.Groupes);
        tabSpec.setIndicator("Groupes");
        tabHost.addTab(tabSpec);

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Filtre");
        tabSpec1.setContent(R.id.Filtre);
        tabSpec1.setIndicator("Filtrer");
        tabHost.addTab(tabSpec1);

        url = "http://" + serverIP + ":8080/searchgroup";

        NumberPicker np11 = (NumberPicker) rootView.findViewById(R.id.numberPicker11);
        NumberPicker np12 = (NumberPicker) rootView.findViewById(R.id.numberPicker12);
        NumberPicker np13 = (NumberPicker) rootView.findViewById(R.id.numberPicker13);
        NumberPicker np15 = (NumberPicker) rootView.findViewById(R.id.numberPicker15);
        NumberPicker np17 = (NumberPicker) rootView.findViewById(R.id.numberPicker17);
        NumberPicker np19 = (NumberPicker) rootView.findViewById(R.id.numberPicker19);
        NumberPicker np21 = (NumberPicker) rootView.findViewById(R.id.numberPicker21);
        NumberPicker np33 = (NumberPicker) rootView.findViewById(R.id.numberPicker33);
        NumberPicker np29 = (NumberPicker) rootView.findViewById(R.id.numberPicker29);
        NumberPicker np23 = (NumberPicker) rootView.findViewById(R.id.numberPicker23);
        NumberPicker np14 = (NumberPicker) rootView.findViewById(R.id.numberPicker14);
        NumberPicker np16 = (NumberPicker) rootView.findViewById(R.id.numberPicker16);

        Calendar c = Calendar.getInstance();
        int currentyear= c.get(Calendar.YEAR);
        int currentmonth = c.get(Calendar.MONTH);
        int currentday = c.get(Calendar.DAY_OF_MONTH);

        //Date début
        np11.setMinValue(1);
        np11.setMaxValue(31);
        np11.setValue(currentday);
        debutJ=np11.getValue();
        np12.setMinValue(1);
        np12.setMaxValue(12);
        np12.setValue(currentmonth + 1);
        debutM=np12.getValue();
        np13.setMinValue(currentyear);
        np13.setMaxValue(currentyear + 3);
        debutA=np13.getValue();
        //Date fin
        np15.setMinValue(1);
        np15.setMaxValue(31);
        np15.setValue(currentday);
        finJ=np15.getValue();
        np17.setMinValue(1);
        np17.setMaxValue(12);
        np17.setValue((currentmonth + 4) % 13);
        finM=np17.getValue();
        np19.setMinValue(currentyear);
        np19.setMaxValue(currentyear + 3);
        np19.setValue(currentyear);
        finA=np19.getValue();
        //Horaire début
        np21.setMinValue(0);
        np21.setMaxValue(23);
        np21.setValue(9);
        debutHoraireH=np21.getValue();
        np23.setMinValue(0);
        np23.setMaxValue(59);
        debutHoraireM=np23.getValue();

        //Horaire fin
        np29.setMinValue(0);
        np29.setMaxValue(23);
        np29.setValue(20);
        finHoraireH=np29.getValue();
        np33.setMinValue(0);
        np33.setMaxValue(59);
        finHoraireM=np23.getValue();
        //Duree max
        np14.setMinValue(0);
        np14.setMaxValue(23);
        np14.setValue(5);
        dureeH=np14.getValue();
        np16.setMinValue(0);
        np16.setMaxValue(59);
        dureeM=np16.getValue();

        np11.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               debutJ = newVal;
                                           }
                                       }

        );
        np12.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               debutM = newVal;
                                           }
                                       }

        );
        np13.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               debutA = newVal;
                                           }
                                       }

        );

        np15.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               finJ = newVal;
                                           }
                                       }

        );
        np17.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               finM = newVal;
                                           }
                                       }

        );
        np19.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               finA = newVal;
                                           }
                                       }

        );

        np21.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               debutHoraireH = newVal;
                                           }
                                       }

        );
        np23.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               debutHoraireM = newVal;
                                           }
                                       }

        );
        np29.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               finHoraireH = newVal;
                                           }
                                       }

        );
        np33.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               finHoraireM = newVal;
                                           }
                                       }

        );

        np14.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               dureeH = newVal;
                                           }
                                       }

        );
        np16.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               dureeM = newVal;
                                           }
                                       }

        );

        datedebut = debutA + "-" + debutM + "-" + debutJ;
        datefin = finA + "-" + finM + "-" + finJ;
        horairedebut = debutHoraireH + ":" + debutHoraireM;
        horairefin = finHoraireH + ":" + finHoraireM;
        duree = dureeH + ":" + dureeM;

        final CheckBox checkBox = (CheckBox) rootView.findViewById(R.id.checkBox);

        if (checkBox.isChecked()) {
            completeGroups = 1;
        } else {
            completeGroups = 0;
        }

        new GetContacts().execute();

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                datedebut = debutA + "-" + debutM + "-" + debutJ;
                datefin = finA + "-" + finM + "-" + finJ;
                horairedebut = debutHoraireH + ":" + debutHoraireM;
                horairefin = finHoraireH + ":" + finHoraireM;
                duree = dureeH + ":" + dureeM;

                new GetContacts().execute();
            }
        });

        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

            paramsList.add(new BasicNameValuePair("horaire_min", horairedebut));
            paramsList.add(new BasicNameValuePair("horaire_max", horairefin));
            paramsList.add(new BasicNameValuePair("date_deb",datedebut));
            paramsList.add(new BasicNameValuePair("date_fin",datefin));
            paramsList.add(new BasicNameValuePair("duree_parcours_heures_max",duree));


            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET, paramsList);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject wholeJson = new JSONObject(jsonStr);

                    String jsonAllGroups = wholeJson.getString("info_allgroups");

                    Log.d("Response (places) : ", "> " + jsonAllGroups);

                    //JSONArray contacts = new JSONArray(jsonStr);
                    JSONArray contacts = new JSONArray(jsonAllGroups);

                    int lengthcontacts = contacts.length();

                    for (int i = 0; i < contacts.length(); i++) {

                        JSONObject d = contacts.getJSONObject(i);

                        String group_id = d.getString(TAG_ID);
                        String id_admin = d.getString(TAG_ADMID);
                        String duree_parc_heures = d.getString(TAG_DUREEH);
                        String date = d.getString(TAG_DATE);
                        String horaire = d.getString(TAG_HORAIRE);
                        String nombre_membres_max = d.getString(TAG_NBMBMAX);
                        String nombre_membres_inscrits = d.getString(TAG_NBMBINS);
                        String name = d.getString(TAG_NAME);
                        String url_parcours = d.getString(TAG_URL);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        contact.put(TAG_ID, group_id);
                        contact.put(TAG_ADMID, id_admin);
                        contact.put(TAG_DUREEH, duree_parc_heures);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_NBMBMAX, nombre_membres_max);
                        contact.put(TAG_NBMBINS, nombre_membres_inscrits);
                        contact.put(TAG_HORAIRE, horaire);
                        contact.put(TAG_DATE, date);
                        contact.put(TAG_URL, url_parcours);

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
                        R.layout.list_groups, new String[] { TAG_NAME, TAG_LAT,
                        TAG_LONG }, new int[] { R.id.name_value,
                        R.id.latitude_value, R.id.longitude_value });
    */

            groupeView.setAdapter(

                    new SimpleAdapter(
                            getActivity(), contactList,
                            R.layout.list_groups, new String[]{/*"Nom du groupe : "+*/TAG_NAME, TAG_ADMID, /*"Date : "+*/TAG_DATE, /*"Durée : "+*/TAG_DUREEH,
                    /*"Horaire : "+*/TAG_HORAIRE, TAG_NBMBINS, TAG_NBMBMAX, TAG_ID, TAG_URL}, new int[]{
                            R.id.name_value, R.id.admin_id_value, R.id.date,
                            R.id.duree_value, R.id.horaire_value, R.id.nombre_membres_inscrits_value,
                            R.id.nombre_membres_max_value, R.id.id_value, R.id.url_value})

            );


        }

    }


    public void onListItemClick(ListView l, View v, int position, long id) {
        HashMap<String, String> myHashMap = (HashMap<String, String>) groupeView.getItemAtPosition(position);

        Intent intent = new Intent(getActivity(), ShowGroupActivity.class);

        Toast.makeText(getActivity(), myHashMap.get(TAG_ID)+ " "
                + myHashMap.get(TAG_ADMID)+ " "
                + myHashMap.get(TAG_DUREEH)+ " "
                + myHashMap.get(TAG_NAME)+ " "
                + myHashMap.get(TAG_NBMBMAX)+ " "
                , Toast.LENGTH_LONG).show();
        intent.putExtra(TAG_ID, myHashMap.get(TAG_ID));
        intent.putExtra(TAG_ADMID, myHashMap.get(TAG_ADMID));
        intent.putExtra(TAG_DUREEH, myHashMap.get(TAG_DUREEH));
        intent.putExtra(TAG_NAME, myHashMap.get(TAG_NAME));
        intent.putExtra(TAG_NBMBMAX, myHashMap.get(TAG_NBMBMAX));
        intent.putExtra(TAG_NBMBINS, myHashMap.get(TAG_NBMBINS));
        intent.putExtra(TAG_HORAIRE, myHashMap.get(TAG_HORAIRE));
        intent.putExtra(TAG_DATE, myHashMap.get(TAG_DATE));
        intent.putExtra(TAG_URL, myHashMap.get(TAG_URL));
        intent.putExtra(TAG_USERID, user_id);


        startActivity(intent);
    }

}

