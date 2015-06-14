package com.example.invite.myapplication.frags;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.invite.myapplication.R;
import com.example.invite.myapplication.utils.ServerAddress;
import com.example.invite.myapplication.utils.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowGroupFragment extends Fragment {

    ProgressDialog pDialog;
    ListView membreView;
    public static ArrayList<HashMap<String, String>> listItem;//array of items
    public static SimpleAdapter adapter1 = null;

    int group_id;
    int user_id;
    int admin_id;
    String getTagUrl;

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

    private static final String TAG_NOM="nom";
    private static final String TAG_PRENOM="prenom";
    private static final String TAG_MSG="message";

    String getTagUserid;
    String getTagDureeh;
    String getTagHoraire;
    String getTagDate;
    String getTagName;
    String getTagId;
    String getTagAdmid;
    String getTagNbmbmax;
    String getTagNbmbins;

    private static String serverIP = ServerAddress.whatIs();

    private static String url;

    JSONArray contacts = null;
    ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

    TextView groupName;
    TextView date;
    TextView horaire;
    TextView prdvz;
    TextView duree;
    TextView nbMembresMax;
    TextView nbMembresIns;

    Button rejoindre;
    Button annuler;
    Button desinscrire;

    public ShowGroupFragment() {
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

        getTagAdmid = getArguments().getString(TAG_ADMID);
        getTagDate = getArguments().getString(TAG_DATE);
        getTagDureeh = getArguments().getString(TAG_DUREEH);
        getTagHoraire = getArguments().getString(TAG_HORAIRE);
        getTagId = getArguments().getString(TAG_ID);
        getTagName = getArguments().getString(TAG_NAME);
        getTagNbmbins = getArguments().getString(TAG_NBMBINS);
        getTagNbmbmax = getArguments().getString(TAG_NBMBMAX);
        getTagUserid = getArguments().getString(TAG_USERID);

        getTagUrl = getArguments().getString(TAG_URL);

        groupName = (TextView) rootView.findViewById(R.id.titre);
        date = (TextView) rootView.findViewById(R.id.date);
        duree = (TextView) rootView.findViewById(R.id.duree);
        horaire = (TextView) rootView.findViewById(R.id.horaire);
        nbMembresIns = (TextView) rootView.findViewById(R.id.nbmaxpersonnes);
        nbMembresMax = (TextView) rootView.findViewById(R.id.titre);

        rejoindre = (Button) rootView.findViewById(R.id.buttonrejoindre);
        desinscrire = (Button) rootView.findViewById(R.id.buttondesinscrire);
        annuler = (Button) rootView.findViewById(R.id.buttonannuler);

        groupName.setText("Nom du groupe : "+getTagName);
        date.setText("Date : "+getTagDate);
        duree.setText("Duree : "+getTagDureeh.substring(0,5));
        horaire.setText("Horaire : "+getTagHoraire.substring(0,5));
        group_id = Integer.parseInt(getTagId);
        user_id = Integer.parseInt(getTagUserid);
        admin_id = Integer.parseInt(getTagAdmid);
        nbMembresIns.setText(getTagNbmbins);
        nbMembresMax.setText(getTagNbmbmax);

        TabHost tabHost = (TabHost) rootView.findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Informations");
        tabSpec.setContent(R.id.Informations);
        tabSpec.setIndicator("Infos");
        tabHost.addTab(tabSpec);

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Informations");
        tabSpec1.setContent(R.id.Programme);
        tabSpec1.setIndicator("Infos");
        tabHost.addTab(tabSpec1);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("Mur");
        tabSpec2.setContent(R.id.Mur);
        tabSpec2.setIndicator("Mur");
        tabHost.addTab(tabSpec2);

        url = "http://" + serverIP + ":8080/showgroup";

        if(user_id == admin_id) {
            desinscrire.setEnabled(false);
            desinscrire.setVisibility(View.GONE);
            rejoindre.setEnabled(false);
            rejoindre.setVisibility(View.GONE);
        }

        rejoindre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        desinscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

            paramsList.add(new BasicNameValuePair("group_id", String.valueOf(group_id)));;

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET, paramsList);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject wholeJson = new JSONObject(jsonStr);

                    JSONArray jsonMembers = wholeJson.getJSONArray("members");
                    JSONArray jsonWall = wholeJson.getJSONArray("wall");

                    Log.d("Response (members) : ", "> " + jsonMembers);
                    Log.d("Response (wall) : ", "> " + jsonWall);

                    int lengthMembers = jsonMembers.length();
                    int lengthWall = jsonWall.length();

                    for (int i = 0; i < jsonMembers.length(); i++) {

                        JSONObject d = jsonMembers.getJSONObject(i);

                        String user_id = d.getString(TAG_USERID);
                        String nom = d.getString(TAG_NOM);
                        String prenom = d.getString(TAG_PRENOM);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        contact.put(TAG_USERID, user_id);
                        contact.put(TAG_NOM, nom);
                        contact.put(TAG_PRENOM, prenom);

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

            ListAdapter adapter2 = new SimpleAdapter(
                    getActivity(), contactList,
                    R.layout.list_members, new String[]{TAG_NOM, TAG_PRENOM, TAG_USERID}, new int[]{
                    R.id.nom_membre_value, R.id.prenom_membre_value, R.id.id_membre_value});

            membreView.setAdapter(adapter2);

        }

    }


    public void onListItemClick(ListView l, View v, int position, long id) {
        HashMap<String, String> myHashMap = (HashMap<String, String>) membreView.getItemAtPosition(position);

        //Intent intent = new Intent(getActivity(), ShowProfileActivity.class);
        //intent.putExtra(TAG_USERID, myHashMap.get(TAG_USERID));
        //startActivity(intent);
    }




}
